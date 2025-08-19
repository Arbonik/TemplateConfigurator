package project.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.max

@Composable
fun<T> PickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    items: List<T>,
    text : (T) -> String,
    onBuildingSelected: (T) -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.8f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Select",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    _root_ide_package_.project.ui.components.DynamicColumnTable(
                        data = items,
                    ) { item ->
                        _root_ide_package_.project.ui.components.PickerItem(
                            text = text(item),
                            onClick = { onBuildingSelected(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PickerItem(
    text : String,
    onClick: () -> Unit
){
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
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
            .fillMaxSize()
            .onSizeChanged { size -> containerWidth = size.width }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier.fillMaxSize(),
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