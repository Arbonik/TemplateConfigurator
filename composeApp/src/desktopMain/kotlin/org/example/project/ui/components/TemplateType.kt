package org.example.project.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.data.enums.TemplateType
import org.example.project.ui.common.Dropdown

@Composable
fun TemplateTypeDropdown(){
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var selected by remember {
            mutableStateOf(TemplateType.Uni_S)
        }

        Text(
            text = "Выберите тип шаблона:",
            style = MaterialTheme.typography.h6
        )
        Dropdown(
            items = TemplateType.values().toList(),
            onDrawItem = { type ->
                Text(
                    text = type.name,
                    style = MaterialTheme.typography.body1
                )
            },
            selectedType = selected,
            onTypeSelected = { selected = it }
        )
    }
}