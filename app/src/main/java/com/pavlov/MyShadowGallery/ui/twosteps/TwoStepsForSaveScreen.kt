package com.pavlov.MyShadowGallery.ui.twosteps

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlov.MyShadowGallery.R
import com.pavlov.MyShadowGallery.theme.uiComponents.CustomButtonOne
import com.pavlov.MyShadowGallery.theme.uiComponents.MatrixBackground

@Composable
fun TwoStepsForSaveScreen(
    viewModel: TwoStepsForSaveViewModel = hiltViewModel(),
    onNavigateToSetPassword: () -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateToKeyInput: () -> Unit
) {
    val step by viewModel.step.collectAsState()
    val navigateToSetPassword by viewModel.navigateToSetPassword.collectAsState()
    val navigateToMain by viewModel.navigateToMain.collectAsState()
    val navigateToKeyInput by viewModel.navigateToKeyInput.collectAsState()
//    val context = LocalContext.current

    LaunchedEffect(navigateToSetPassword) {
        if (navigateToSetPassword) {
            onNavigateToSetPassword()
        }
    }

    LaunchedEffect(navigateToMain) {
        if (navigateToMain) {
            onNavigateToMain()
        }
    }

    LaunchedEffect(navigateToKeyInput) {
        if (navigateToKeyInput) {
            onNavigateToKeyInput()
        }
    }

    Scaffold(
        content = { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                MatrixBackground()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (step) {
                        0 -> {
                            Text(
                                text = stringResource(id = R.string.new_about_app),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier
                                    .padding(20.dp)
                            )
                            CustomButtonOne(
                                onClick = { viewModel.onNextButtonClicked() },
                                text = stringResource(id = R.string.resume),
                                icon = R.drawable.login_30dp
                            )
                        }

                        1 -> {
                            Text(stringResource(id = R.string.set_password))
                            Row {
                                CustomButtonOne(
                                    onClick = { viewModel.onYesClicked() },
                                    text = "Да"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                CustomButtonOne(
                                    onClick = { viewModel.onNoClicked() },
                                    text = "Нет"
                                )
                            }
                        }

//                        3 -> {
//                            Text("Установить ключ шифрования?")
//                            Row {
//                                CustomButtonOne(
//                                    onClick = { viewModel.onYesClicked() },
//                                    text = "Да"
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                CustomButtonOne(
//                                    onClick = { viewModel.onNoClicked() },
//                                    text = "Нет"
//                                )
//                            }
//                        }
                    }
                }
            }
        }
    )
}