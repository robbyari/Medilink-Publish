package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.robbyari.monitoring.R
import com.robbyari.monitoring.presentation.theme.Blue

@Composable
fun TextFieldWithIcons(
    icon: ImageVector,
    label: String,
    placeholder: String,
    trailingIcon: ImageVector? = null,
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordVisible: Boolean = false,
    modifier: Modifier
) {
    var passwordVisible by remember { mutableStateOf(isPasswordVisible) }
    var passwordIcon by remember { mutableStateOf(trailingIcon) }

    OutlinedTextField(
        value = value,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = R.string.icon_text_field)
            )
        },
        visualTransformation = if (passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (trailingIcon != null) {
                passwordIcon = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Default.Visibility
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    },
                ) {
                    passwordIcon?.let { Icon(imageVector = it, contentDescription = stringResource(id = R.string.password)) }
                }
            }
        },
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.Black
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 16.sp,
                color = Color.Black
            )
        },
        modifier = modifier,
        shape = RoundedCornerShape(20),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Blue,
            focusedBorderColor = Blue,
            focusedLabelColor = Blue,
        ),
        singleLine = true,
    )
}