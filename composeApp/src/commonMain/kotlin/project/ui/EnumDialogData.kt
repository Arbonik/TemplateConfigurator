package project.ui

// Dialog data class
data class EnumDialogData<T>(
    val title: String,
    val items: List<T>,
    val currentSelected: T,
    val onSelected: (T) -> Unit
)