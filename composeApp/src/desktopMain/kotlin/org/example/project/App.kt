package org.example.project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.serialization.json.Json
import org.example.project.data.TemplateGenerationConfig
import org.example.project.ui.TemplateConfigEditor
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.io.File

private val json = Json {
    explicitNulls = false   // не требовать явных null для отсутствующих полей
    coerceInputValues = true // преобразовывать некорректные значения к значениям по умолчанию
}

@Composable
@Preview
fun App() {
    val text = File(
        "/Users/arbonik/KMPProject/TemplateConfigurator/composeApp/src/commonMain/resources/config.json"
    ).readText()

    val config = json.decodeFromString<TemplateGenerationConfig>(text)

    println(config)

    MaterialTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var config by remember { mutableStateOf(config) }

            TemplateConfigEditor(
                config = config,
                onConfigChanged = { newConfig ->
                    config = newConfig
                    // You might want to save the config here
                }
            )
        }
    }
}