package ru.sicampus.bootcamp2026.ui.screens.profile

import android.Manifest
import android.provider.MediaStore.Images.Media
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.repository.ImageRepository
import ru.sicampus.bootcamp2026.ui.camera.CameraViewModel
import ru.sicampus.bootcamp2026.ui.camera.CameraViewModelFactory
import ru.sicampus.bootcamp2026.ui.components.ProfileField
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.White

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel<ProfileViewModel>(),
) {
    val state by viewModel.uiState.collectAsState()
    val repository = remember { ImageRepository() }
    val cameraViewModel: CameraViewModel = viewModel<CameraViewModel>(
        factory = CameraViewModelFactory(repository))

    when(val currentState = state) {
        is ProfileState.Error -> {
            Box(Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = currentState.reason)
                    Button(
                        onClick = { viewModel.getData() },
                        modifier = Modifier.background(Blue)
                            .clip(RoundedCornerShape(20.dp))
                    ) {
                        Text("Обновить")
                    }
                }
            }
        }
        is ProfileState.Content -> {
            ProfileContent(currentState, viewModel, cameraViewModel)
        }
        is ProfileState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Blue)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    state: ProfileState.Content,
    viewModel: ProfileViewModel,
    cameraViewModel: CameraViewModel
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imageBitmap by cameraViewModel.imageBitmap.collectAsState()
    val imageUri by cameraViewModel.imageUri.collectAsState()
    val imageUrl by cameraViewModel.imageUrl.collectAsState()
    val isUploading by cameraViewModel.isUploading.collectAsState()

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()) {
        bitmap -> cameraViewModel.setImage(bitmap, null)
    }
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                granted ->
            {
                if (granted) cameraLauncher.launch()
            }
        }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        uri -> uri ?: return@rememberLauncherForActivityResult
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            Media.getBitmap(context.contentResolver, uri)
        }
        cameraViewModel.setImage(null, uri)

    }

    Text("Профиль", modifier = Modifier.fillMaxSize().wrapContentHeight())

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val fraction = scrollBehavior.state.collapsedFraction
    var name by remember { mutableStateOf(state.currentUser.name) }
    var phone by remember { mutableStateOf(state.currentUser.phone) }
    var email by remember { mutableStateOf(state.currentUser.email) }
    var photoUrl by remember { mutableStateOf(state.currentUser.photoUrl) }
    var info by remember { mutableStateOf(state.currentUser.info) }
    var show by remember {mutableStateOf(false)}

    var isEditable by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    if (isEditable) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = White,
                                unfocusedTextColor = White,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )
                    } else {
                        Text(
                            text = name,
                            color = White,
                            fontSize = lerp(24.sp, 20.sp, fraction),
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Blue
                ),
                actions = {
                    IconButton(onClick = {isEditable = !isEditable}) {
                        Icon(
                            painterResource(R.drawable.edit),
                            contentDescription = "Edit",
                            tint = White
                        )
                    }
                },
                scrollBehavior = scrollBehavior,

            )
        },
        containerColor = White
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap!!.asImageBitmap(),
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(lerp(390.dp, 0.dp, fraction))
                            .alpha(1f - fraction)

                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.default_im),
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(lerp(390.dp, 0.dp, fraction))
                            .alpha(1f - fraction)

                    )
                }

                FloatingActionButton(onClick = { show = true },
                    containerColor = Blue,
                    modifier = Modifier
                        .height(85.dp)
                        .width(85.dp)
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                        .alpha(1f - fraction)
                        .offset(x = (-16.dp), y = 350.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Icon(painter = painterResource(R.drawable.camera),
                        contentDescription = "Смена изображения",
                        tint = Color.White)
                    if (show) {
                        AlertDialog(
                            onDismissRequest = {show = false},
                            title = { Text("Способ загрузки фото", fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold)))},
                            confirmButton = {},
                            text = {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TextButton(
                                            onClick = {
                                                show = false
                                                val hasPermission = ContextCompat.checkSelfPermission(
                                                    context, Manifest.permission.CAMERA
                                                ) == PackageManager.PERMISSION_GRANTED
                                                if (hasPermission) cameraLauncher.launch()
                                                else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                            }
                                        ) {
                                            Text("Сделать фото",
                                                fontSize = 12.sp,
                                                fontFamily = FontFamily(Font(R.font.montserrat_bold)))
                                        }

                                        TextButton(
                                            onClick = {
                                                show = false
                                                galleryLauncher.launch("image/*")
                                            }
                                        ) {
                                            Text("Из галереи",
                                                fontSize = 12.sp,
                                                fontFamily = FontFamily(Font(R.font.montserrat_bold)))
                                        }
                                    }
                                    Button(
                                        onClick = {cameraViewModel.uploadImage(context)},
                                        enabled = imageBitmap != null && !isUploading,
                                        modifier = Modifier.background(Blue)
                                            .fillMaxWidth().padding(8.dp)
                                            .height(15.dp).clip(RoundedCornerShape(20.dp))
                                    ) {
                                        Text(if (isUploading) "Загрузка" else "Загрузить в облако")
                                    }
                                }
                            }
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(padding)
                        .padding(horizontal = 24.dp)
                        .offset( y = lerp(240.dp, 6.dp, fraction))
                ) {
                    ProfileField("Телефон", phone, isEditable = isEditable)
                    ProfileField("Email", email, isEditable = isEditable)
                    ProfileField("Информация", info, isEditable = isEditable)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(modifier = Modifier.height(63.dp).width(216.dp)
                , onClick = {
                    viewModel.updateData(
                        name = name,
                        phone = phone,
                        email = email,
                        info = info,
                        photoUrl = photoUrl
                    )
                    isEditable = false
                },
            ) {
                Text(text = "Сохранить изменения",
                    color= White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)))
            }

        }
    }
}
