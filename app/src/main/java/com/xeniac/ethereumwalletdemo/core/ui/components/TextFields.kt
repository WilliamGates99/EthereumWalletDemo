package com.xeniac.ethereumwalletdemo.core.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.ethereumwalletdemo.R

@Composable
fun CustomOutlinedTextField(
    value: String,
    title: String?,
    hint: String,
    errorText: String?,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
    supportingText: String? = null,
    isPasswordTextField: Boolean = false,
    maxLines: Int = 1,
    shape: Shape = RoundedCornerShape(12.dp),
    titleColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    leadingIcon: Painter? = null,
    leadingIconContentDescription: String? = null,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    keyboardAction: () -> Unit = {}
) {
    val hasError = errorText != null
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val isSingleLine = maxLines == 1

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        title?.let { title ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                fontSize = 14.sp,
                color = titleColor,
                maxLines = 1
            )

            Spacer(Modifier.height(4.dp))
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
            },
            readOnly = isReadOnly,
            isError = hasError,
            singleLine = isSingleLine,
            maxLines = maxLines,
            shape = shape,
            placeholder = {
                Text(
                    text = hint,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    maxLines = maxLines
                )
            },
            supportingText = if (supportingText != null) {
                {
                    Text(
                        text = supportingText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                }
            } else null,
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = leadingIconContentDescription,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else null,
            visualTransformation = if (isPasswordTextField && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPasswordTextField) {
                    val icon = if (isPasswordVisible)
                        painterResource(id = R.drawable.ic_password_toggle_visible)
                    else painterResource(id = R.drawable.ic_password_toggle_invisible)

                    val contentDescription = if (isPasswordVisible)
                        stringResource(id = R.string.textfield_content_description_password_toggle_hide)
                    else
                        stringResource(id = R.string.textfield_content_description_password_toggle_show)

                    IconButton(onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }) {
                        Icon(
                            painter = icon,
                            contentDescription = contentDescription
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = keyboardCapitalization,
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions {
                defaultKeyboardAction(imeAction)
                keyboardAction()
            }
        )

        if (hasError) {
            Column(
                Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorText.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Red,
                    maxLines = 1
                )
            }
        }
    }
}