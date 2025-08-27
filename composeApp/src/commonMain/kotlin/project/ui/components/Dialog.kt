package project.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun PickerItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shadowElevation = 2.dp
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            softWrap = true,
            overflow = TextOverflow.Visible
        )
    }
}


@Composable
fun<T> DynamicColumnTable(
    data: List<T>, // Ваши данные
    minColumnWidth: Dp = 150.dp, // Минимальная ширина столбца
    contentPadding: PaddingValues = PaddingValues(8.dp),
    contentDraw: @Composable (T) -> Unit // Композируемое содержимое ячейки
) {
    var containerWidth by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    // Рассчитываем количество столбцов
    val columns : Int by derivedStateOf {
        with(density) {
            val minWidthPx = minColumnWidth.toPx()
            if (minWidthPx <= 0 || containerWidth <= 0) {
                1
            } else {
                max(1, (containerWidth / minWidthPx).toInt())
            }
        }
    }

    Box(
        modifier = Modifier
            .onSizeChanged { size -> containerWidth = size.width }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(data) { item ->
                contentDraw(item)
            }
        }
    }
}