package ru.sicampus.bootcamp2026.ui.screen.profile

import android.Manifest
import kotlinx.coroutines.delay
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.ImageRepository
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.ui.camera.CameraViewModal
import ru.sicampus.bootcamp2026.ui.camera.CameraViewModalFactory
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.Black50
import ru.sicampus.bootcamp2026.ui.theme.BluePrimary
import ru.sicampus.bootcamp2026.ui.theme.CustomTypography
import ru.sicampus.bootcamp2026.ui.theme.LightGray
import ru.sicampus.bootcamp2026.ui.theme.MediumGray
import ru.sicampus.bootcamp2026.ui.theme.PlashkaColor
import ru.sicampus.bootcamp2026.ui.theme.Red
import ru.sicampus.bootcamp2026.ui.theme.SineyIney
import ru.sicampus.bootcamp2026.ui.theme.SoftWhite
import ru.sicampus.bootcamp2026.ui.theme.VeryDarkGrey


@Composable
fun ProfileScreen( onExitClick: () -> Unit , onMeetClick:() -> Unit, profileViewModel: ProfileViewModel = viewModel()) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showImagePickerDialog by remember { mutableStateOf(false) }


    val context = LocalContext.current


    val repository = remember { ImageRepository() }
    val viewModel: CameraViewModal = viewModel(
        factory = CameraViewModalFactory(repository)
    )

    val user = remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        val currentUser = AuthLocalDataSource.getCurrentUser()
        user.value = currentUser

        currentUser?.id?.let { id ->
            viewModel.setUserId(id)
            viewModel.loadProfileImage()
        }
    }

    val emailState by profileViewModel.uiState.collectAsState()

    when (emailState) {
        is ProfileState.Content -> {

        }
        is ProfileState.Error -> Toast.makeText(context,"Ошибка обновления почты", Toast.LENGTH_LONG).show()
        ProfileState.Loading -> {}
    }

    var refreshKey by remember { mutableStateOf(0) }
    val imageBitmap by viewModel.imageBitmap.collectAsState()
    val imageUrl by viewModel.imageUrl.collectAsState()

    LaunchedEffect(imageUrl) {
        viewModel.loadProfileImage()
    }

    val imageUrlWithTimestamp = remember(imageUrl, refreshKey) {
        imageUrl?.let { url ->
            if (url.contains("?")) {
                "$url&refresh=${refreshKey}_${System.currentTimeMillis()}"
            } else {
                "$url?refresh=${refreshKey}_${System.currentTimeMillis()}"
            }
        }
    }

    LaunchedEffect(viewModel.isUploading.collectAsState().value, viewModel.imageUrl.collectAsState().value) {
        if (!viewModel.isUploading.value && viewModel.imageUrl.value != null) {
            delay(500)
            refreshKey++
            Log.d("REFRESH", "Force refresh triggered, key: $refreshKey")
        }
    }

    // Лончеры для камеры и галереи
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        Log.d("DEBUG", "Camera launcher callback, bitmap = ${bitmap != null}")
        if (bitmap != null) {
            viewModel.loadProfileImage()
            viewModel.setImage(bitmap, null)
            viewModel.uploadImage(context)
            viewModel.loadProfileImage()
        } else (Log.d("DEBUG", "Camera returned null bitmap"))
        showImagePickerDialog = false
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        }
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        Log.d("DEBUG", "Gallery launcher callback, uri = $uri")
        if (uri != null) {
            Log.d("DEBUG", "Converting URI to bitmap...")
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source =
                    android.graphics.ImageDecoder.createSource(context.contentResolver, uri)
                android.graphics.ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            viewModel.setImage(bitmap, uri)
            viewModel.uploadImage(context)
        }
        showImagePickerDialog = false
    }

    val backgroundColor = SoftWhite
    val blackColor = Black
    val containerColor = BluePrimary
    val grayContainerColor = LightGray
    val whiteColor = SoftWhite
    val redColor = Red
    val semiTransparentBlack = Black50

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 48.dp
                )
        ) {
            Text(
                text = "профиль",
                color = blackColor,
                style = CustomTypography.displayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .size(149.dp)
                                .clip(RoundedCornerShape(100.dp))
                        ) {
                            if (imageUrlWithTimestamp != null) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(imageUrlWithTimestamp)
                                        .memoryCachePolicy(CachePolicy.DISABLED) // Отключаем кэш памяти
                                        .diskCachePolicy(CachePolicy.DISABLED)   // Отключаем кэш диска
                                        .build(),
                                    contentDescription = "Фото профиля",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(id = R.drawable.person),
                                    error = painterResource(id = R.drawable.person)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .background(containerColor)
                                .align(Alignment.BottomEnd)
                        ) {
                            IconButton(
                                onClick = {
                                    showImagePickerDialog = true
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.camera_plus),
                                    contentDescription = "Изменить фото",
                                    tint = whiteColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = user.value?.fullName ?: "Неизвестно",
                        fontSize = 18.sp,
                        color = blackColor,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Default,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .background(containerColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 24.dp, bottom = 24.dp, end = 24.dp)
                ) {
                    Text(
                        text = "аккаунт",
                        fontSize = 16.sp,
                        color = blackColor,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Default,
                        fontWeight = FontWeight.Medium,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "почта",
                                fontSize = 14.sp,
                                color = semiTransparentBlack,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Default,
                                fontWeight = FontWeight.Medium,
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = user.value?.email ?: "Неизвестно",
                                fontSize = 16.sp,
                                color = blackColor,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Default,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        IconButton(
                            onClick = { showEditDialog = true },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.pen),
                                contentDescription = "Редактировать",
                                tint = whiteColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(BluePrimary)
                    .clickable { onMeetClick() }
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "созданные Вами встречи",
                        color = Black,
                        style = CustomTypography.bodyMedium
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = { onMeetClick() },
                        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_left),
                            contentDescription = "Перейти",
                            tint = SoftWhite,
                            modifier = Modifier.size(24.dp).rotate(180f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth().height(56.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(grayContainerColor).clickable { onExitClick() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = "Выйти",
                            tint = Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(text = "выйти из аккаунта", color = redColor, style = CustomTypography.bodyMedium)
                }
            }
        }

        if (showEditDialog) {
            EditDialog(
                currentEmail = user.value?.email ?: "Неизвестно",
                onDismiss = { showEditDialog = false },
                profileViewModel = profileViewModel,
                user = user,
                onExitClick = onExitClick
            )
        }

        if (showImagePickerDialog) {
            ImageSourceDialog(
                onCameraClick = {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        cameraLauncher.launch()
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                onGalleryClick = {
                    galleryLauncher.launch("image/*")
                },
                onDismiss = { showImagePickerDialog = false }
            )
        }
    }
}

@Composable
fun ImageSourceDialog(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black.copy(alpha = 0.4f))
            .clickable(onClick = onDismiss)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 4.dp)
                .background(Black.copy(alpha = 0.4f))
        )

        Box(
            modifier = Modifier
                .width(288.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(30.dp))
                .background(PlashkaColor)
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        top = 20.dp,
                        bottom = 32.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "выбрать фото",
                        color = SoftWhite,
                        style = CustomTypography.bodyMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(44.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cross),
                            contentDescription = "Закрыть",
                            tint = SoftWhite.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(SineyIney)
                            .clickable {
                                onCameraClick()
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.camera_plus),
                                contentDescription = "Камера",
                                tint = SoftWhite,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "cделать фото",
                                style = CustomTypography.bodyMedium.copy(
                                    color = SoftWhite,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(SineyIney)
                            .clickable {
                                onGalleryClick()
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.gallery),
                                contentDescription = "Галерея",
                                tint = SoftWhite,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "выбрать из галереи",
                                style = CustomTypography.bodyMedium.copy(
                                    color = SoftWhite,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditDialog(
    currentEmail: String,
    onDismiss: () -> Unit,
    profileViewModel: ProfileViewModel,
    user : MutableState<UserDto?>,
    onExitClick: () -> Unit
) {
    var newEmail by remember { mutableStateOf(currentEmail) }

    Box(
        modifier = Modifier.fillMaxSize().background(Black.copy(alpha = 0.4f)).clickable(onClick = onDismiss)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().blur(radius = 4.dp).background(Black.copy(alpha = 0.4f))
        )

        Box(
            modifier = Modifier
                .width(288.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(30.dp))
                .background(PlashkaColor)
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(top = 20.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "изменить почту",
                        color = SoftWhite,
                        style = CustomTypography.labelMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(44.dp).align(Alignment.TopEnd)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cross),
                            contentDescription = "Закрыть",
                            tint = SoftWhite.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    BasicTextField(
                        value = newEmail,
                        onValueChange = { newEmail = it },
                        textStyle = CustomTypography.bodyMedium.copy(
                            color = SoftWhite
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(VeryDarkGrey)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )

                    if (newEmail.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                                .background(VeryDarkGrey)
                        ) {
                            Text(
                                text = "Введите новую почту",
                                style = CustomTypography.bodyMedium.copy(
                                    color = SoftWhite.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                val id = user.value?.id?: 0
                val fullname = user.value?.fullName ?: "Неизвестно"
                val email = user.value?.email ?: "Неизвестно"
                val context = LocalContext.current
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(SineyIney)
                            .clickable {
                                if (newEmail.isEmpty() || newEmail==email){
                                    Toast.makeText(context,"Введите новую почту", Toast.LENGTH_LONG).show()
                                } else{
                                    profileViewModel.onIntent(ProfileIntent.Send(id, newEmail, fullname ))
                                    user.value = user.value?.copy(email = newEmail)
                                    onDismiss()
                                    Toast.makeText(context,"Вы успешно изменили почту", Toast.LENGTH_LONG).show()
                                    onExitClick()
                                }
                            }
                    ) {
                        Text(
                            text = "сохранить",
                            style = CustomTypography.bodyMedium.copy(
                                color = SoftWhite,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
//@Composable
//fun ProfileScreenPreview() {
//    MaterialTheme(
//        typography = CustomTypography
//    ) {
//        ProfileScreen()
//    }
//}