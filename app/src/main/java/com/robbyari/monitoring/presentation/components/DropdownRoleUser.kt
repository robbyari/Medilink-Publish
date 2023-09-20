package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robbyari.monitoring.R
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownRoleUser(
    isExpanded: Boolean,
    onIsExpandedChange: (Boolean) -> Unit,
    role: String,
    onRoleChange: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { onIsExpandedChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        OutlinedTextField(
            value = role,
            onValueChange = {},
            readOnly = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Badge,
                    contentDescription = stringResource(R.string.icon_text_field)
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            shape = RoundedCornerShape(20),
            label = {
                Text(
                    text = stringResource(R.string.division),
                    fontSize = 16.sp,
                    color = Color.Black
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.silahkan_pilih_divisi),
                    fontSize = 16.sp,
                    color = Color.Black
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Blue,
                focusedBorderColor = Blue,
                focusedLabelColor = Blue,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp * 0.6f
        Column(
            modifier = Modifier
                .width(screenWidth)
                .height(65.dp)
                .offset(x = screenWidth),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onIsExpandedChange(!isExpanded) },
                modifier = Modifier
                    .width(100.dp)
                    .background(LightBlue)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.teknisi),
                            textAlign = TextAlign.End,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onRoleChange("Teknisi")
                        onIsExpandedChange(false)
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.user),
                            fontSize = 16.sp,
                            textAlign = TextAlign.End,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onRoleChange("User")
                        onIsExpandedChange(false)
                    },
                )
            }
        }
    }
}
