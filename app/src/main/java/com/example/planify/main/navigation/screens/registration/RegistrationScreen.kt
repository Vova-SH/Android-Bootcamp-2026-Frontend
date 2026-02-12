package com.example.planify.main.navigation.screens.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.CaretRight
import com.adamglin.phosphoricons.regular.Eye
import com.adamglin.phosphoricons.regular.StarFour
import com.example.planify.R
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.withShapeBackground
import com.example.planify.main.features.auth.domain.schemas.RegisterUserSchema
import com.example.planify.main.navigation.screens.backgrounds.CloudyBackground

@Composable
fun RegistrationScreen(
    navHostController: NavHostController
) {
    RegistrationScreen(
        viewModel = hiltViewModel(),
        navHostController = navHostController,
    )
}

@Composable
private fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    navHostController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { route ->
            navHostController.navigate(route.route)
        }
    }

    val colors = MaterialTheme.colorScheme

    var firstNameQuery by remember { mutableStateOf("") }
    var lastNameQuery by remember { mutableStateOf("") }
    var usernameQuery by remember { mutableStateOf("") }
    var emailQuery by remember { mutableStateOf("") }
    var passwordQuery by remember { mutableStateOf("") }
    var repeatPasswordQuery by remember { mutableStateOf("") }

    CloudyBackground(
        modifier = Modifier.fillMaxSize(),
        animate = true
    )

    LaunchedEffect(passwordQuery, repeatPasswordQuery) {
        viewModel.resetFocusedColor()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Transparent
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(Locals.spacing.l),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.welcome),
                    style = MaterialTheme.typography.displayLarge,
                    color = colors.onBackground
                )

                Spacer(modifier = Modifier.height(Locals.spacing.l))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(Locals.icons.small),
                        imageVector = PhosphorIcons.Regular.StarFour,
                        contentDescription = null,
                        tint = colors.secondary
                    )

                    Spacer(modifier = Modifier.width(Locals.spacing.xs))

                    Text(
                        text = stringResource(R.string.register_to_start),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(Locals.spacing.m))

                LabeledTextField(
                    label = stringResource(R.string.firstName),
                    value = firstNameQuery,
                    uiState = uiState,
                    onValueChange = { firstNameQuery = it },
                    placeholder = stringResource(R.string.firstName_placeholder)
                )

                Spacer(modifier = Modifier.height(Locals.spacing.m))

                LabeledTextField(
                    label = stringResource(R.string.lastName),
                    value = lastNameQuery,
                    uiState = uiState,
                    onValueChange = { lastNameQuery = it },
                    placeholder = stringResource(R.string.lastName_placeholder)
                )

                Spacer(modifier = Modifier.height(Locals.spacing.m))

                LabeledTextField(
                    label = stringResource(R.string.username_reg),
                    value = usernameQuery,
                    uiState = uiState,
                    onValueChange = { usernameQuery = it },
                    placeholder = stringResource(R.string.username_placeholder)
                )

                Spacer(modifier = Modifier.height(Locals.spacing.m))

                LabeledTextField(
                    label = stringResource(R.string.email),
                    value = emailQuery,
                    uiState = uiState,
                    onValueChange = { emailQuery = it },
                    placeholder = stringResource(R.string.email_example)
                )

                Spacer(modifier = Modifier.height(Locals.spacing.m))

                LabeledTextField(
                    label = stringResource(R.string.password),
                    value = passwordQuery,
                    uiState = uiState,
                    onValueChange = { passwordQuery = it },
                    placeholder = stringResource(R.string.enter_password),
                    icon = PhosphorIcons.Regular.Eye
                )

                Spacer(modifier = Modifier.height(Locals.spacing.m))

                LabeledTextField(
                    label = stringResource(R.string.repeat_password),
                    value = repeatPasswordQuery,
                    uiState = uiState,
                    onValueChange = { repeatPasswordQuery = it },
                    placeholder = stringResource(R.string.repeat_password_placeholder),
                    icon = PhosphorIcons.Regular.Eye
                )


                Spacer(modifier = Modifier.height(Locals.spacing.m))

                Spacer(modifier = Modifier.height(Locals.spacing.xl))

            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = Locals.spacing.l)
                    .height(Locals.dimens.authButtonHeight)
                    .background(
                        brush = Locals.gradients.blue,
                        shape = Locals.shapes.mediumShape
                    )
                    .align(Alignment.BottomCenter),
                shape = Locals.shapes.mediumShape,
                onClick = {
                    if(passwordQuery != repeatPasswordQuery) {
                        viewModel.onIncorrectPassword()
                    } else {
                        val shema = RegisterUserSchema(
                            firstName = firstNameQuery,
                            lastName = lastNameQuery,
                            username = usernameQuery,
                            email = emailQuery,
                            password = passwordQuery
                        )

                        viewModel.register(shema)
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Locals.spacing.xxxs),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.register),
                        style = MaterialTheme.typography.displaySmall,
                        color = colors.onPrimary
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        modifier = Modifier
                            .size(Locals.icons.smallPlus),
                        imageVector = PhosphorIcons.Regular.CaretRight,
                        contentDescription = null,
                        tint = colors.onPrimary
                    )
                }
            }
        }

    }
}



@Composable
private fun LabeledTextField(
    label: String,
    value: String,
    uiState: UIState,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    val colors = MaterialTheme.colorScheme
    val shape = Locals.shapes.mediumShape

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = colors.onBackground,
            fontWeight = FontWeight.Medium
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .withShapeBackground(
                    color = colors.background,
                    shape = shape
                ),
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colors.surface,
                unfocusedContainerColor = colors.surface,
                cursorColor = colors.primary,
                focusedIndicatorColor = if (uiState == UIState.DATA_INCORRECT) colors.error
                else colors.primary,
                unfocusedIndicatorColor = if (uiState == UIState.DATA_INCORRECT) colors.error
                else colors.surface
            ),
            shape = shape,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )
    }
}

@Composable
private fun LabeledTextField(
    label: String,
    value: String,
    uiState: UIState,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector
) {
    val colors = MaterialTheme.colorScheme
    val shape = Locals.shapes.mediumShape

    var isVisible by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = colors.onBackground,
            fontWeight = FontWeight.Medium
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .withShapeBackground(
                    color = colors.background,
                    shape = shape
                ),
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colors.surface,
                unfocusedContainerColor = colors.surface,
                cursorColor = colors.primary,
                focusedIndicatorColor = if (uiState == UIState.INCORRECT_PASSWORD) colors.error
                else colors.primary,
                unfocusedIndicatorColor = if (uiState == UIState.INCORRECT_PASSWORD) colors.error
                else colors.surface
            ),
            shape = shape,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .size(Locals.icons.smallPlus)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    isVisible = true
                                    try {
                                        awaitRelease()
                                    } finally {
                                        isVisible = false
                                    }
                                }
                            )
                        },
                    imageVector = icon,
                    contentDescription = null
                )
            },
            visualTransformation = if (isVisible) VisualTransformation.None
            else PasswordVisualTransformation()
        )
    }
}