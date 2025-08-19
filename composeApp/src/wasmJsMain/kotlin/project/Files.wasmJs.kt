package project

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.deprecated.openFileSaver
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.download
import io.github.vinceglb.filekit.withScopedAccess
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import org.w3c.files.FileReader

actual object Files {
    actual fun openFile(readerText: (String) -> Unit, onError: (String) -> Unit) {
        try {
            val input = document.createElement("input") as HTMLInputElement
            input.type = "file"
            input.accept = "text/plain,.txt,.md,.log,.json,.csv,.yaml,.yml"
            input.style.display = "none"
            document.body?.appendChild(input)

            input.onchange = {
                val file = input.files?.item(0)
                if (file != null) {
                    val reader = FileReader()
                    reader.onload = { _: Event ->
                        val result = reader.result?.toString() ?: ""
                        readerText(result)
                        input.remove()
                    }
                    reader.onerror = { _: Event ->
                        onError(reader.error.toString() ?: "Не удалось прочитать файл")
                        input.remove()
                    }
                    reader.readAsText(file)
                } else {
                    onError("Файл не выбран")
                    input.remove()
                }
            }

            input.click()
        } catch (t: Throwable) {
            onError(t.message ?: "Неизвестная ошибка при открытии файла")
        }
    }

    actual fun saveFile(content: String) {
        GlobalScope.launch {
            FileKit.download(content.encodeToByteArray(), "file.json")
        }
    }
}