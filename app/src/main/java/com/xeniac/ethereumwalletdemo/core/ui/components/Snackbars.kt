package com.xeniac.ethereumwalletdemo.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    fontSize: TextUnit = 14.sp,
    messageFontWeight: FontWeight = FontWeight.Medium,
    actionFontWeight: FontWeight = FontWeight.Bold
) {
    SnackbarHost(hostState = snackbarHostState) { data ->
        val message = data.visuals.message
        val actionLabel = data.visuals.actionLabel

        Box(
            modifier = modifier.padding(8.dp)
        ) {
            Snackbar(
                shape = shape,
                action = if (actionLabel != null) {
                    {
                        TextButton(onClick = { data.performAction() }) {
                            Text(
                                text = actionLabel,
                                fontSize = fontSize,
                                fontWeight = actionFontWeight,
                            )
                        }
                    }
                } else null,
            ) {
                Text(
                    text = message,
                    fontSize = fontSize,
                    fontWeight = messageFontWeight
                )
            }
        }
    }
}