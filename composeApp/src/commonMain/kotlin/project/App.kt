package project

import FilePickerScreen
import TemplateConfigEditor
import TemplateGenerationConfig
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import project.Files.openFile
import project.ui.theme.AppTheme


@OptIn(ExperimentalSerializationApi::class)
val json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false   // не требовать явных null для отсутствующих полей
    coerceInputValues = true // преобразовывать некорректные значения к значениям по умолчанию
    decodeEnumsCaseInsensitive = true
}

@Composable
fun App() {
    AppTheme {
        var error by remember { mutableStateOf("") }
        if (error.isEmpty())
            Column(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                var config: TemplateGenerationConfig? by remember { mutableStateOf(null) }

                var isFileOpen by remember { mutableStateOf(false) }

                if (isFileOpen) {
                    TemplateConfigEditor(
                        config = config!!,
                        onConfigChanged = { newConfig ->
                            config = newConfig
                        }
                    )
                } else {
                    FilePickerScreen(
                        openFile = {
                            openFile({ text ->
                                config = json.decodeFromString<TemplateGenerationConfig>(
                                    text ?: ""
                                )
                                isFileOpen = true
                            }, onError = { error = it })
                        },
                        saveFile = null
                    )
                }
            }
        else {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = error)
            }
        }
    }
}