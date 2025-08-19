package project

expect object Files {

    // Открытие файла
    fun openFile(readerText: (String) -> Unit, onError : (String) -> Unit)

    // Сохранение файла
    fun saveFile(content: String)
}