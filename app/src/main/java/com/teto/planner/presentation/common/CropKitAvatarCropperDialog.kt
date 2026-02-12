package com.teto.planner.presentation.common

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tanishranjan.cropkit.CropOptions
import com.tanishranjan.cropkit.CropShape
import com.tanishranjan.cropkit.GridLinesType
import com.tanishranjan.cropkit.GridLinesVisibility
import com.tanishranjan.cropkit.ImageCropper
import com.tanishranjan.cropkit.rememberCropController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

@Composable
fun CropKitAvatarCropperDialog(
    imageUri: Uri,
    onDismiss: () -> Unit,
    onCropSuccess: (ByteArray) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(imageUri) {
        withContext(Dispatchers.IO) {
            bitmap = loadBitmapFromUri(context, imageUri, maxDimension = 2048)
            isLoading = false
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator(color = Color.White)
                bitmap != null -> {
                    CropKitEditor(
                        bitmap = bitmap!!,
                        onDismiss = onDismiss,
                        onSave = { croppedBitmap ->
                            if (isSaving) return@CropKitEditor
                            isSaving = true
                            scope.launch(Dispatchers.IO) {
                                val bytes = croppedBitmap.toJpegByteArray(quality = 85)
                                withContext(Dispatchers.Main) {
                                    onCropSuccess(bytes)
                                    isSaving = false
                                }
                            }
                        },
                        isSaving = isSaving
                    )
                }
                else -> Text("Не удалось загрузить изображение", color = Color.White)
            }
        }
    }
}

@Composable
private fun CropKitEditor(
    bitmap: Bitmap,
    onDismiss: () -> Unit,
    onSave: (Bitmap) -> Unit,
    isSaving: Boolean
) {
    val cropController = rememberCropController(
        bitmap = bitmap,
        cropOptions = CropOptions(
            cropShape = CropShape.AspectRatio(1f),
            contentScale = ContentScale.Fit,

            gridLinesVisibility = GridLinesVisibility.ON_TOUCH,
            gridLinesType = GridLinesType.CIRCLE,

            handleRadius = 10.dp,
            touchPadding = 16.dp
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        ImageCropper(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            cropController = cropController
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = onDismiss, enabled = !isSaving) {
                Text("Отмена")
            }

            Button(
                onClick = {
                    val cropped = cropController.crop()
                    onSave(cropped)
                },
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Сохранить")
                }
            }
        }
    }
}

private fun Bitmap.toJpegByteArray(quality: Int): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, quality, stream)
    return stream.toByteArray()
}

private fun loadBitmapFromUri(context: android.content.Context, uri: Uri, maxDimension: Int): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val options = android.graphics.BitmapFactory.Options().apply { inJustDecodeBounds = true }
        android.graphics.BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        var inSampleSize = 1
        if (options.outHeight > maxDimension || options.outWidth > maxDimension) {
            val halfHeight = options.outHeight / 2
            val halfWidth = options.outWidth / 2
            while (halfHeight / inSampleSize >= maxDimension && halfWidth / inSampleSize >= maxDimension) {
                inSampleSize *= 2
            }
        }

        val activeStream = context.contentResolver.openInputStream(uri)
        val bitmap = android.graphics.BitmapFactory.decodeStream(activeStream, null, android.graphics.BitmapFactory.Options().apply {
            this.inSampleSize = inSampleSize
        })
        activeStream?.close()
        bitmap
    } catch (e: Exception) { null }
}
