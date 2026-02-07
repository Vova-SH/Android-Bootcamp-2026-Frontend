package ru.sicampus.bootcamp2026.ui.screens

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ru.sicampus.bootcamp2026.ui.theme.BackgroundColor
import ru.sicampus.bootcamp2026.ui.theme.PrimaryPurple
import ru.sicampus.bootcamp2026.ui.utils.PhoneVisualTransformation
import ru.sicampus.bootcamp2026.ui.utils.customDashedBorder
import ru.sicampus.bootcamp2026.ui.viewmodel.ProfileViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    var showPhotoDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.uploadAvatar(context, null, it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let { viewModel.uploadAvatar(context, it, null) }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) cameraLauncher.launch(null)
        else Toast.makeText(context, "Разрешите доступ к камере.", Toast.LENGTH_SHORT).show()
    }

    if (user == null && isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryPurple)
        }
        return
    }

    if (showPhotoDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text("Сменить фото") },
            text = { Text("Выберите источник") },
            confirmButton = {
                TextButton(onClick = {
                    showPhotoDialog = false
                    galleryLauncher.launch("image/*")
                }) { Text("Галерея") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPhotoDialog = false
                    val hasPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    if (hasPerm) cameraLauncher.launch(null)
                    else permissionLauncher.launch(Manifest.permission.CAMERA)
                }) { Text("Камера") }
            }
        )
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Профиль", fontWeight = FontWeight.SemiBold, fontSize = 24.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BackgroundColor)
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

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { showPhotoDialog = true },
                contentAlignment = Alignment.Center
            ) {
                if (!user?.avatarUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = user!!.avatarUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = user?.name?.take(1) ?: "",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.BottomCenter
                ) {}
            }
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.width(120.dp).padding(top = 8.dp), color = PrimaryPurple)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Приветствуем, ${user?.name?.split(" ")?.firstOrNull() ?: ""}!",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(32.dp))

            EditableProfileField(
                title = "ФИО",
                initialValue = user?.name ?: "",
                onSave = { viewModel.updateField(name = it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            PhoneProfileField(
                initialValue = user?.phone ?: "",
                onSave = { viewModel.updateField(phone = it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            EditableProfileField(
                title = "Почта",
                initialValue = user?.email ?: "",
                readOnly = true,
                onSave = { }
            )

            Spacer(modifier = Modifier.height(12.dp))

            val rawDate = user?.birthDate
            val displayDate = try {
                if (!rawDate.isNullOrBlank()) {
                    val date = LocalDate.parse(rawDate)
                    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))
                    date.format(formatter)
                } else "Не указано"
            } catch (e: Exception) { rawDate ?: "" }

            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val newDate = LocalDate.of(year, month + 1, dayOfMonth)
                    viewModel.updateField(birthDate = newDate.toString())
                },
                2000, 0, 1
            )

            ProfileDateItem(
                title = "День рождения",
                value = displayDate,
                onEditClick = { datePickerDialog.show() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            EditableProfileField(
                title = "Описание",
                initialValue = user?.position ?: "",
                onSave = { viewModel.updateField(position = it) }
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onLogoutClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFE5E5),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
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
}

@Composable
fun EditableProfileField(
    title: String,
    initialValue: String,
    readOnly: Boolean = false,
    onSave: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(initialValue) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(initialValue) {
        if (!isEditing) text = initialValue
    }

    LaunchedEffect(isEditing) {
        if (isEditing) focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .customDashedBorder(color = if (isEditing) PrimaryPurple else Color(0xFFEBECEE))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            if (isEditing && !readOnly) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    singleLine = true
                )
            } else {
                Text(
                    text = if (text.isBlank()) "Не указано" else text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 1
                )
            }

            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }

        if (!readOnly) {
            IconButton(
                onClick = {
                    if (isEditing) {
                        onSave(text)
                        isEditing = false
                    } else {
                        isEditing = true
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isEditing) PrimaryPurple.copy(alpha = 0.1f) else Color.Transparent)
            ) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Check else Icons.Outlined.Edit,
                    contentDescription = "Edit",
                    tint = if (isEditing) PrimaryPurple else Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun PhoneProfileField(
    initialValue: String,
    onSave: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(initialValue) {
        if (!isEditing) {
            val digits = initialValue.filter { it.isDigit() }

            text = if (digits.length == 11 && (digits.startsWith("7") || digits.startsWith("8"))) {
                digits.substring(1)
            } else {
                digits.take(10)
            }
        }
    }

    LaunchedEffect(isEditing) {
        if (isEditing) focusRequester.requestFocus()
    }

    val mask = "+7 (###) ###-##-##"
    val visualTransformation = remember { PhoneVisualTransformation(mask, '#') }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .customDashedBorder(color = if (isEditing) PrimaryPurple else Color(0xFFEBECEE))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            if (isEditing) {
                BasicTextField(
                    value = text,
                    onValueChange = { input ->
                        if (input.length <= 10 && input.all { it.isDigit() }) {
                            text = input
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = visualTransformation
                )
            } else {
                val display = if (text.isBlank()) "Не указано" else {
                    val transformed = visualTransformation.filter(AnnotatedString(text))
                    transformed.text.text
                }

                Text(
                    text = display,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 1
                )
            }

            Text(
                text = "Телефон",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }

        IconButton(
            onClick = {
                if (isEditing) {
                    onSave(text)
                    isEditing = false
                } else {
                    isEditing = true
                }
            },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (isEditing) PrimaryPurple.copy(alpha = 0.1f) else Color.Transparent)
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.Check else Icons.Outlined.Edit,
                contentDescription = "Edit Phone",
                tint = if (isEditing) PrimaryPurple else Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ProfileDateItem(
    title: String,
    value: String,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .customDashedBorder(color = Color(0xFFEBECEE))
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
                color = Color.Black,
                maxLines = 1
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }

        IconButton(
            onClick = onEditClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit Date",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}