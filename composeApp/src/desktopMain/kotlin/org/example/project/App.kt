package org.example.project

import FilePickerScreen
import TemplateConfigEditor
import TemplateGenerationConfig
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
val json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false   // не требовать явных null для отсутствующих полей
    coerceInputValues = true // преобразовывать некорректные значения к значениям по умолчанию
    decodeEnumsCaseInsensitive = true
}

// Открытие файла
fun openFile(readerText: (String) -> Unit, onError : (String) -> Unit) {
    try {
        val dialog = FileDialog(null as Frame?, "Выберите файл", FileDialog.LOAD)
        dialog.isVisible = true
        val file = dialog.file?.let { File(dialog.directory, it) }
        readerText(file?.readText() ?: "")
    } catch (e: Exception) {
        onError(e.localizedMessage)
        println(e)
    }
}
// Открытие файла
fun openFileUi(readerText: (String) -> Unit, onError : (String) -> Unit) {
    try {
        val file = File("/Users/arbonik/KMPProject/TemplateConfigurator/composeApp/src/commonMain/resources/Moon_Mega_Treasure.json")
        readerText(file?.readText() ?: "")
    } catch (e: Exception) {
        onError(e.localizedMessage)
        println(e)
    }
}

// Сохранение файла
fun saveFile(content: String) {
    val dialog = FileDialog(null as Frame?, "Сохранить файл", FileDialog.SAVE)
    dialog.isVisible = true
    val file = dialog.file?.let { File(dialog.directory, it) }
    file?.writeText(content)
}

@Composable
@Preview
fun App() {
    MaterialTheme {
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

                LaunchedEffect(Unit){
                    openFileUi({ text ->
                        config = json.decodeFromString<TemplateGenerationConfig>(text ?: "")
                        isFileOpen = true
                    }, onError = { error = it })
                }
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
                                config = json.decodeFromString<TemplateGenerationConfig>(text ?: "")
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