package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier.clip(RoundedCornerShape(20.dp))
) {
    return OutlinedTextField(
        value = value,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "Icon Text field"
            )
        },
        trailingIcon = {
            if (trailingIcon != null) {
                Icon(imageVector = trailingIcon, contentDescription = "Icon Trailing")
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
        singleLine = true
    )
}