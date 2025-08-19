package project

expect object Files {

    // Открытие файла
    fun openFile(readerText: (String) -> Unit, onError : (String) -> Unit)
//    {
//        try {
//            val dialog = FileDialog(null as Frame?, "Выберите файл", FileDialog.LOAD)
//            dialog.isVisible = true
//            val file = dialog.file?.let { File(dialog.directory, it) }
//            readerText(file?.readText() ?: "")
//        } catch (e: Exception) {
//            onError(e.localizedMessage)
//            println(e)
//        }
//    }
// Сохранение файла
    fun saveFile(content: String)
}