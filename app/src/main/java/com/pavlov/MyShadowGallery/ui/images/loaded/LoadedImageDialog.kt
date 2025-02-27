package com.pavlov.MyShadowGallery.ui.images.loaded

import com.pavlov.MyShadowGallery.R
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pavlov.MyShadowGallery.theme.uiComponents.CustomCircularProgressIndicator
import com.pavlov.MyShadowGallery.theme.uiComponents.MyStyledDialog
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.pavlov.MyShadowGallery.theme.My3
import com.pavlov.MyShadowGallery.theme.My7
import com.pavlov.MyShadowGallery.theme.uiComponents.CustomButtonOne
import com.pavlov.MyShadowGallery.theme.uiComponents.MyStyledDialogWithTitle
import com.pavlov.MyShadowGallery.ui.images.ImagesViewModel
import com.pavlov.MyShadowGallery.ui.images.ZoomableImage
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.pavlov.MyShadowGallery.util.ToastExt

/** ИСПОЛЬЗУЮ ЭТОТ ЭКРАН НА ОТКРЫТОЕ ИЗ ХРАНИЛИЩА*/

@Composable
fun LoadedImageDialog (
    uri: Uri,
    viewModel: ImagesViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    isItNew: Boolean = false,
    onSave: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val actualImageFile = viewModel.uriToFile(uri)

    if (actualImageFile == null || !actualImageFile.exists()) {
        LaunchedEffect(Unit) {
            ToastExt.show(context.getString(R.string.loaded_dialog_file_not_found))
            onDismiss()
        }
        return
    }

    val date = viewModel.getPhotoDate(actualImageFile.name)
    val name = viewModel.getFileNameWithoutExtension(actualImageFile.name)
    val actualUri = viewModel.getFileUri(actualImageFile.name)

    if (actualUri == null) {
        LaunchedEffect(Unit) {
            ToastExt.show(context.getString(R.string.loaded_dialog_file_not_found))
            onDismiss()
        }
        return
    }

    var showShareOptions by remember { mutableStateOf(false) }
    var showMemeSelection by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var hiddenImageUri by remember { mutableStateOf<Uri?>(null) }

    // Наблюдаем за списком прогресса из ViewModel
    val steganographyProgress by viewModel.steganographyProgress.collectAsState()

    MyStyledDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.h6)
            Text(text = date, style = MaterialTheme.typography.subtitle2, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            ZoomableImage(
                uri = hiddenImageUri ?: actualUri,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isItNew) { // кейс с 4 кнопками
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CustomButtonOne(
                            onClick = {
                                onSave?.invoke()
                                onDismiss()
                            },
                            text = stringResource(R.string.loaded_dialog_save),
                            textColor = My7,
                            iconColor = My7,
                            icon = Icons.Default.Save
                        )
                        CustomButtonOne(
                            onClick = { showShareOptions = true },
                            text = stringResource(R.string.loaded_dialog_send),
                            textColor = My7,
                            iconColor = My7,
                            icon = Icons.Default.Share
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CustomButtonOne(
                            onClick = onDelete,
                            text = stringResource(R.string.loaded_dialog_delete),
                            textColor = My7,
                            iconColor = My7,
                            icon = Icons.Default.Delete
                        )

                        CustomButtonOne(
                            onClick = onDismiss,
                            text = stringResource(R.string.loaded_dialog_close),
                            textColor = My7,
                            iconColor = My7,
                            icon = Icons.Default.Close
                        )
                    }
                } else { // кейс с 3 кнопками
                    CustomButtonOne(
                        onClick = { showShareOptions = true },
                        text = stringResource(R.string.loaded_dialog_share_image),
                        textColor = My7,
                        iconColor = My7,
                        icon = Icons.Default.Share
                    )
                    CustomButtonOne(
                        onClick = onDelete,
                        text = stringResource(R.string.loaded_dialog_delete),
                        textColor = My7,
                        iconColor = My7,
                        icon = Icons.Default.Delete
                    )
                    CustomButtonOne(
                        onClick = onDismiss,
                        text = stringResource(R.string.loaded_dialog_close),
                        textColor = My7,
                        iconColor = My7,
                        icon = Icons.Default.Close
                    )
                }
            }
        }
    }

    if (showShareOptions) {    // Диалог выбора способа поделиться
        MyStyledDialogWithTitle(
            onDismissRequest = { showShareOptions = false },
            title = {
                Text(
                    text = stringResource(R.string.loaded_dialog_sharing_method),
                    style = MaterialTheme.typography.h6,
                )
            },
            gap = 0,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomButtonOne(
                        onClick = {
                            val shareUri = hiddenImageUri ?: actualUri
                            if (shareUri != null) {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "image/jpeg"
                                    putExtra(Intent.EXTRA_STREAM, shareUri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        context.getString(R.string.loaded_dialog_share_image)
                                    )
                                )
                            } else {
                                ToastExt.show(context.getString(R.string.loaded_dialog_file_not_found))
                            }
                            showShareOptions = false
                        },
                        text = stringResource(R.string.loaded_dialog_share_original),
                        textColor = My7,
                        iconColor = My7,
                        icon = Icons.Default.InsertPhoto
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomButtonOne( // поделиться шифровкой в мемчик
                        onClick = {
                            showMemeSelection = true
                            showShareOptions = false
                        },
                        text = stringResource(R.string.loaded_dialog_encrypt_meme),
                        textColor = My7,
                        iconColor = My7,
                        icon = Icons.Default.HideImage
                    )
                }
            }
        )
    }

    if (showMemeSelection) { // Диалог выбора мема
        MemeSelectionDialog(
            onMemeSelected = { memeResId ->
                isProcessing = true
                viewModel.shareImageWithHiddenOriginal(
                    originalImageFile = actualImageFile,
                    memeResId = memeResId,
                    onResult = { uri ->
                        isProcessing = false
                        if (uri != null) {
                            hiddenImageUri = uri
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "file/jpeg"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(
                                Intent.createChooser(
                                    shareIntent,
                                    context.getString(R.string.loaded_dialog_share_image)
                                )
                            )
                            onDismiss()
                        } else {
                            ToastExt.show(context.getString(R.string.loaded_dialog_image_creation_failed))
                        }
                    }
                )
                showMemeSelection = false
            },
            onDismiss = {
                showMemeSelection = false
            }
        )
    }

    if (isProcessing) { // Диалог загрузки с описанием процесса шифрования
        MyStyledDialog(onDismissRequest = {}) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.Transparent),
            ) {
                Text(text = stringResource(R.string.loaded_dialog_encryption_processing), color = My3)
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                        .background(Color.Transparent)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    steganographyProgress.forEach { step ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            val icon: ImageVector = if (step.startsWith(stringResource(R.string.error))) Icons.Default.Error else Icons.Default.Cable
                            val tint: Color = if (step.startsWith(stringResource(R.string.error))) Color.Red else My3
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = tint,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = step,
                                color = tint
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                    CustomCircularProgressIndicator()
                }
            }
        }
    }
}
