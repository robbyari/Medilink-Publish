package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.robbyari.monitoring.presentation.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
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
    var isPasswordVisible by remember { mutableStateOf(isPasswordVisible) }
    var passwordIcon by remember { mutableStateOf(trailingIcon) }

    OutlinedTextField(
        value = value,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "Icon Text field"
            )
        },
        visualTransformation = if (isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (trailingIcon != null) {
                passwordIcon = if (isPasswordVisible) Icons.Outlined.VisibilityOff else Icons.Default.Visibility
                IconButton(
                    onClick = {
                        isPasswordVisible = !isPasswordVisible
                    },
                ) {
                    passwordIcon?.let { Icon(imageVector = it, contentDescription = "Icon Trailing") }
                }
            }
        },
        onValueChange = onValueChange,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        modifier = modifier,
        shape = RoundedCornerShape(20),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Blue,
            cursorColor = Blue,
            focusedLabelColor = Blue
        ),
        singleLine = true,
    )
}