package com.finance.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.finance.android.R
import com.finance.android.ui.theme.ErrorLight
import com.finance.android.ui.theme.Info
import com.finance.android.ui.theme.Warning

enum class DialogType {
    INFO,
    WARNING,
    ERROR;

    fun getIcon(): ImageVector {
        return when (this) {
            INFO -> Icons.Filled.Info
            WARNING -> Icons.Filled.Warning
            ERROR -> Icons.Filled.Info
        }
    }

    fun getTintColor(): Color {
        return when (this) {
            INFO -> Info
            WARNING -> Warning
            ERROR -> ErrorLight
        }
    }
}

enum class DialogActionType {
    ONE_BUTTON,
    TWO_BUTTON
}

@Composable
fun CustomDialog(
    dialogType: DialogType,
    dialogActionType: DialogActionType,
    title: String,
    subTitle: String? = null,
    onPositive: () -> Unit,
    onNegative: (() -> Unit)? = null,
    positiveText: String = stringResource(id = R.string.btn_confirm),
    negativeText: String? = null
) {
    val modifier = Modifier.defaultMinSize(minWidth = 300.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(dimensionResource(id = R.dimen.padding_medium).value.dp)

    when (dialogActionType) {
        DialogActionType.ONE_BUTTON -> DrawDialogWithOneButton(
            modifier = modifier,
            dialogType = dialogType,
            title = title,
            subTitle = subTitle,
            onClick = onPositive,
            buttonText = positiveText
        )
        DialogActionType.TWO_BUTTON -> DrawDialogWithTwoButton(
            modifier = modifier,
            dialogType = dialogType,
            title = title,
            subTitle = subTitle,
            onPositive = onPositive,
            positiveText = positiveText,
            onNegative = onNegative ?: {},
            negativeText = negativeText ?: ""
        )
    }
}

@Composable
private fun DrawDialogWithOneButton(
    modifier: Modifier,
    dialogType: DialogType,
    title: String,
    subTitle: String? = null,
    onClick: () -> Unit,
    buttonText: String
) {
    Dialog(onDismissRequest = { }) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                dialogType.getIcon(),
                modifier = Modifier.size(40.dp),
                contentDescription = "icon",
                tint = dialogType.getTintColor()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            subTitle?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick,
                text = buttonText,
                buttonType = ButtonType.ROUNDED
            )
        }
    }
}

@Composable
private fun DrawDialogWithTwoButton(
    modifier: Modifier,
    dialogType: DialogType,
    title: String,
    subTitle: String? = null,
    onPositive: () -> Unit,
    positiveText: String,
    onNegative: () -> Unit,
    negativeText: String
) {
    Dialog(onDismissRequest = { }) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                dialogType.getIcon(),
                modifier = Modifier.size(40.dp),
                contentDescription = "icon",
                tint = dialogType.getTintColor()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            subTitle?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                TextButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = onNegative,
                    text = negativeText,
                    buttonType = ButtonType.ROUNDED,
                    buttonColor = ButtonColor.WHITE
                )
                Spacer(modifier = Modifier.width(10.dp))
                TextButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = onPositive,
                    text = positiveText,
                    buttonType = ButtonType.ROUNDED
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCustomDialogOneButton_Info() {
    CustomDialog(
        dialogType = DialogType.INFO,
        dialogActionType = DialogActionType.ONE_BUTTON,
        title = "title",
        subTitle = "subTitle",
        onPositive = { }
    )
}

@Preview
@Composable
private fun PreviewCustomDialogOneButton_Warning() {
    CustomDialog(
        dialogType = DialogType.WARNING,
        dialogActionType = DialogActionType.ONE_BUTTON,
        title = "title",
        subTitle = "subTitle",
        onPositive = { }
    )
}

@Preview
@Composable
private fun PreviewCustomDialogOneButton_Error() {
    CustomDialog(
        dialogType = DialogType.ERROR,
        dialogActionType = DialogActionType.ONE_BUTTON,
        title = "title",
        subTitle = "subTitle",
        onPositive = { }
    )
}

@Preview
@Composable
private fun PreviewCustomDialogTwoButton_Info() {
    CustomDialog(
        dialogType = DialogType.INFO,
        dialogActionType = DialogActionType.TWO_BUTTON,
        title = "title",
        subTitle = "subTitle",
        onPositive = { }
    )
}
