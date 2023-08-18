package com.xeniac.ethereumwalletdemo.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.ethereumwalletdemo.R

@Composable
fun CustomBigButton(
    btnText: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    loadingText: String = stringResource(id = R.string.btn_loading_text),
    horizontalPadding: Dp = 14.dp,
    verticalPadding: Dp = 14.dp,
    fontSize: TextUnit = 14.sp,
    onClick: () -> Unit
) {
    Button(
        enabled = !isLoading,
        onClick = onClick,
        contentPadding = PaddingValues(
            horizontal = horizontalPadding,
            vertical = verticalPadding
        ),
        modifier = modifier
    ) {
        Text(
            text = if (isLoading) loadingText else btnText,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            fontSize = fontSize,
            maxLines = 1
        )
    }
}