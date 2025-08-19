import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun FilePickerScreen(
    openFile: () -> Unit,
    saveFile: (() -> Unit)?,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            // Кнопка "Открыть файл"
            Box(contentAlignment = Alignment.Center) {
                FileButton(
                    text = "Открыть файл",
                    onClick = {
                        openFile()
                    }
                )
            }

            if (saveFile != null) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = Color(0x55FFFFFF)
                )
                // Кнопка "Сохранить файл"
                Box(contentAlignment = Alignment.Center) {
                    FileButton(
                        text = "Сохранить файл",
                        onClick = {
                            saveFile()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FileButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFB71C1C), // Красный вместо фиолетового
            contentColor = Color.White
        ),
        modifier = modifier.size(width = 200.dp, height = 60.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )
    }
}