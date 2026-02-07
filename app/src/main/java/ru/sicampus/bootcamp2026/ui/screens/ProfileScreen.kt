package ru.sicampus.bootcamp2026.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images.Media
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.decodeBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.camera.ImageRepository
import ru.sicampus.bootcamp2026.ui.theme.BackgroundColor
import ru.sicampus.bootcamp2026.ui.utils.customDashedBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    val repository = remember { ImageRepository() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var profileImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var showPickerDialog by remember { mutableStateOf(false) }


    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) profileImageBitmap = bitmap
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) cameraLauncher.launch(null)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        profileImageBitmap = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(context.contentResolver, uri)
                )
            } else {
                Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            null
        }
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Профиль", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked, modifier = Modifier.size(48.dp)) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BackgroundColor
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable { showPickerDialog = true }
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    profileImageBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Фото профиля",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Text(
                        text = "И",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .offset(y = (-8).dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable { showPickerDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text("📷", fontSize = 14.sp)
                }
            }

            if (profileImageBitmap != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            isUploading = true
                            try {
                                val url = repository.uploadImage(profileImageBitmap!!)
                                profileImageUrl = url
                                profileImageBitmap = null
                                // TODO: Сохранить URL в профиль через API
                            } catch (e: Exception) {
                                // TODO: Snackbar с ошибкой
                            } finally {
                                isUploading = false
                            }
                        }
                    },
                    enabled = !isUploading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (isUploading) "Загрузка..." else "Сохранить фото профиля",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                profileImageUrl?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = Color.Blue,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            ProfileField(title = "ФИО", value = "Иванов Иван Сергеевич")
            Spacer(modifier = Modifier.height(12.dp))
            ProfileField(title = "Телефон", value = "+7-xxx-xxx-xx-xx")
            Spacer(modifier = Modifier.height(12.dp))
            ProfileField(title = "Почта", value = "email@gmail.com")
            Spacer(modifier = Modifier.height(12.dp))
            ProfileField(title = "День рождения", value = "хх мес. хххх (? лет/года)")
            Spacer(modifier = Modifier.height(12.dp))
            ProfileField(title = "Описание", value = "Менеджер и разработчик")

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onLogoutClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFE5E5),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    "Выйти из аккаунта",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(8.dp),
                    color = Color.Black.copy(alpha = 0.8f)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showPickerDialog) {
        AlertDialog(
            onDismissRequest = { showPickerDialog = false },
            title = { Text("Выбрать фото профиля") },
            text = { Text("Откуда взять фото?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                        if (hasPermission) {
                            cameraLauncher.launch(null)
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                        showPickerDialog = false
                    }
                ) {
                    Text("Камера")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        galleryLauncher.launch("image/*")
                        showPickerDialog = false
                    }
                ) {
                    Text("Галерея")
                }
            }
        )
    }
}

@Composable
fun ProfileField(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .customDashedBorder()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { /* TODO */ },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onBackClicked = {}, onLogoutClicked = {})
}
