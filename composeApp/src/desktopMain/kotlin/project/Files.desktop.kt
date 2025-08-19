package project

import java.awt.FileDialog
import java.awt.Frame
import java.io.File

actual object Files {

    // Открытие файла
    actual fun openFile(readerText: (String) -> Unit, onError : (String) -> Unit) {
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

    actual fun saveFile(content: String) {
        val dialog = FileDialog(null as Frame?, "Сохранить файл", FileDialog.SAVE)
        dialog.isVisible = true
        val file = dialog.file?.let { File(dialog.directory, it) }
        file?.writeText(content)
    }
}