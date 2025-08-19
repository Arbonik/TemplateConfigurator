import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.*
import kotlin.math.pow
import kotlin.math.sqrt

import androidx.compose.ui.graphics.*
import kotlin.invoke
import kotlin.math.*


// Модели данных
data class CircleNode(
    val id: Int,
    var center: Offset,
    var radius: Float = 40f,
    var color: Color = Color.Blue,
    var label: String = ""
)

data class Connection(
    val id: Int,
    val fromId: Int,
    val toId: Int,
    var color: Color = Color.Black,
    var width: Float = 2f
)

data class AppState(
    val circles: List<CircleNode>,
    val connections: List<Connection>
)

//@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
//fun main() = application {
//    // Состояние приложения
//    val undoStack = remember { mutableStateListOf<AppState>() }
//    val redoStack = remember { mutableStateListOf<AppState>() }
//    var appState by remember { mutableStateOf(AppState(emptyList(), emptyList())) }
//    var nextCircleId by remember { mutableStateOf(1) }
//    var nextConnectionId by remember { mutableStateOf(1) }
//
//    // Выбранные элементы
//    var selectedCircleId by remember { mutableStateOf<Int?>(null) }
//    var selectedConnectionId by remember { mutableStateOf<Int?>(null) }
//    var draggedCircleId by remember { mutableStateOf<Int?>(null) }
//
//    // Настройки UI
//    var showSettings by remember { mutableStateOf(false) }
//    var currentColor by remember { mutableStateOf(Color.Blue) }
//    var currentRadius by remember { mutableStateOf(40f) }
//    var currentLabel by remember { mutableStateOf("") }
//    var connectionWidth by remember { mutableStateOf(2f) }
//
//    // Сохраняем состояние в историю
//    fun saveState() {
//        undoStack.add(
//            appState.copy(
//                circles = appState.circles.map { it.copy() },
//                connections = appState.connections.map { it.copy() }
//            ))
//        redoStack.clear()
//    }
//
//    // Отмена действия
//    fun undo() {
//        if (undoStack.isNotEmpty()) {
//            redoStack.add(appState)
//            appState = undoStack.removeLast()
//        }
//    }
//
//    // Повтор действия
//    fun redo() {
//        if (redoStack.isNotEmpty()) {
//            undoStack.add(appState)
//            appState = redoStack.removeLast()
//        }
//    }
//
//    // Удаление выбранных элементов
//    fun deleteSelected() {
//        saveState()
//
//        selectedCircleId?.let { circleId ->
//            appState = appState.copy(
//                circles = appState.circles.filter { it.id != circleId },
//                connections = appState.connections.filter {
//                    it.fromId != circleId && it.toId != circleId
//                }
//            )
//            selectedCircleId = null
//        }
//
//        selectedConnectionId?.let { connectionId ->
//            appState = appState.copy(
//                connections = appState.connections.filter { it.id != connectionId }
//            )
//            selectedConnectionId = null
//        }
//    }
//
//
//
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "Advanced Circle Connector",
//        onKeyEvent = { keyEvent ->
//            when {
//                (keyEvent.isCtrlPressed && keyEvent.key == Key.Z) -> {
//                    undo()
//                    true
//                }
//
//                (keyEvent.isCtrlPressed && keyEvent.key == Key.Y) -> {
//                    redo()
//                    true
//                }
//
//                (keyEvent.key == Key.Delete) -> {
//                    deleteSelected()
//                    true
//                }
//
//                else -> false
//            }
//        }
//    ) {
//        MaterialTheme {
//            Box(modifier = Modifier.fillMaxSize()) {
//                // Основное поле для рисования
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.White)
//                        .pointerInput(Unit) {
//                            // ... (обработка жестов остается без изменений)
//                        }
//                ) {
//                    Canvas(modifier = Modifier.fillMaxSize()) {
//                        // Сначала рисуем соединения
//                        appState.connections.groupBy { it.fromId to it.toId }.forEach { (key, connections) ->
//                            val (fromId, toId) = key
//                            val fromCircle = appState.circles.find { it.id == fromId }
//                            val toCircle = appState.circles.find { it.id == toId }
//
//                            if (fromCircle != null && toCircle != null) {
//                                val totalConnections = connections.size
//                                connections.forEachIndexed { index, connection ->
//                                    val isFirstConnection = index == 0
//                                    val curvature = if (isFirstConnection) 0f else 0.3f * (index + 1) / 2f
//
//                                    drawConnection(
//                                        from = fromCircle.center,
//                                        to = toCircle.center,
//                                        connection = connection,
//                                        curvature = curvature,
//                                        isSelected = connection.id == selectedConnectionId,
//                                        isFirstConnection = isFirstConnection
//                                    )
//                                }
//                            }
//                        }
//
//                        // Затем рисуем круги (чтобы они были поверх соединений)
//                        appState.circles.forEach { circle ->
//                            drawCircle(
//                                color = circle.color,
//                                center = circle.center,
//                                radius = circle.radius,
//                                style = Stroke(width = if (circle.id == selectedCircleId) 5f else 3f)
//                            )
//
//                            if (circle.label.isNotBlank()) {
//                                // ... (логика отрисовки текста)
//                            }
//                        }
//                    }
//                }
//
//                // ... (панель управления остается без изменений)
//            }
//        }
//
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "Advanced Circle Connector",
//        onKeyEvent = { keyEvent ->
//            when {
//                (keyEvent.isCtrlPressed && keyEvent.key == Key.Z) -> {
//                    undo()
//                    true
//                }
//                (keyEvent.isCtrlPressed && keyEvent.key == Key.Y) -> {
//                    redo()
//                    true
//                }
//                (keyEvent.key == Key.Delete) -> {
//                    deleteSelected()
//                    true
//                }
//                else -> false
//            }
//        }
//    ) {
//        MaterialTheme {
//            Box(modifier = Modifier.fillMaxSize()) {
//                // Основное поле для рисования
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.White)
//                        .pointerInput(Unit) {
//                            detectTapGestures(
//                                onTap = { tapOffset ->
//                                    // Сбрасываем выбор при клике на пустое место
//                                    if (selectedCircleId != null && selectedConnectionId != null) {
//                                        selectedCircleId = null
//                                        selectedConnectionId = null
//                                        return@detectTapGestures
//                                    }
//
//                                    // Проверяем клик по соединению
//                                    val tappedConnection = appState.connections.find { connection ->
//                                        val from = appState.circles.find { it.id == connection.fromId }?.center
//                                        val to = appState.circles.find { it.id == connection.toId }?.center
//                                        if (from != null && to != null) {
//                                            isPointNearLine(tapOffset, from, to, 10f)
//                                        } else false
//                                    }
//
//                                    if (tappedConnection != null) {
//                                        selectedConnectionId = tappedConnection.id
//                                        selectedCircleId = null
//                                        return@detectTapGestures
//                                    }
//
//                                    // Проверяем клик по кругу
//                                    val tappedCircle = appState.circles.find { circle ->
//                                        val distance = sqrt(
//                                            (tapOffset.x - circle.center.x).pow(2) +
//                                                    (tapOffset.y - circle.center.y).pow(2)
//                                        )
//                                        distance <= circle.radius
//                                    }
//
//                                    if (tappedCircle != null) {
//                                        if (selectedCircleId == null) {
//                                            selectedCircleId = tappedCircle.id
//                                            selectedConnectionId = null
//                                        } else if (selectedCircleId != tappedCircle.id) {
//                                            saveState()
//                                            appState = appState.copy(
//                                                connections = appState.connections + Connection(
//                                                    id = nextConnectionId++,
//                                                    fromId = selectedCircleId!!,
//                                                    toId = tappedCircle.id,
//                                                    color = currentColor,
//                                                    width = connectionWidth
//                                                )
//                                            )
//                                        }
//                                    } else {
//                                        // Добавляем новый круг
//                                        saveState()
//                                        appState = appState.copy(
//                                            circles = appState.circles + CircleNode(
//                                                id = nextCircleId++,
//                                                center = tapOffset,
//                                                radius = currentRadius,
//                                                color = currentColor,
//                                                label = currentLabel
//                                            )
//                                        )
//                                        selectedCircleId = null
//                                        selectedConnectionId = null
//                                    }
//                                },
//                                onDoubleTap = { tapOffset ->
//                                    // Удаление по двойному клику
//                                    val tappedCircle = appState.circles.find { circle ->
//                                        val distance = sqrt(
//                                            (tapOffset.x - circle.center.x).pow(2) +
//                                                    (tapOffset.y - circle.center.y).pow(2)
//                                        )
//                                        distance <= circle.radius
//                                    }
//
//                                    if (tappedCircle != null) {
//                                        saveState()
//                                        appState = appState.copy(
//                                            circles = appState.circles.filter { it.id != tappedCircle.id },
//                                            connections = appState.connections.filter {
//                                                it.fromId != tappedCircle.id && it.toId != tappedCircle.id
//                                            }
//                                        )
//                                    }
//                                }
//                            )
//                        }
//                        .pointerInput(Unit) {
//                            detectDragGestures(
//                                onDragStart = { dragOffset ->
//                                    draggedCircleId = appState.circles.find { circle ->
//                                        val distance = sqrt(
//                                            (dragOffset.x - circle.center.x).pow(2) +
//                                                    (dragOffset.y - circle.center.y).pow(2)
//                                        )
//                                        distance <= circle.radius
//                                    }?.id
//                                },
//                                onDrag = { _, dragAmount ->
//                                    draggedCircleId?.let { circleId ->
//                                        saveState()
//                                        appState = appState.copy(
//                                            circles = appState.circles.map { circle ->
//                                                if (circle.id == circleId) {
//                                                    circle.copy(
//                                                        center = Offset(
//                                                            circle.center.x + dragAmount.x,
//                                                            circle.center.y + dragAmount.y
//                                                        )
//                                                    )
//                                                } else circle
//                                            }
//                                        )
//                                    }
//                                },
//                                onDragEnd = {
//                                    draggedCircleId = null
//                                }
//                            )
//                        }
//                ) {
//                    Canvas(modifier = Modifier.fillMaxSize()) {
//                        // Рисуем соединения
//                        appState.connections.forEach { connection ->
//                            val fromCircle = appState.circles.find { it.id == connection.fromId }
//                            val toCircle = appState.circles.find { it.id == connection.toId }
//
//                            if (fromCircle != null && toCircle != null) {
//                                drawLine(
//                                    color = if (connection.id == selectedConnectionId) Color.Red else connection.color,
//                                    start = fromCircle.center,
//                                    end = toCircle.center,
//                                    strokeWidth = connection.width.dp.toPx()
//                                )
//                            }
//                        }
//
//                        // Рисуем круги
//                        appState.circles.forEach { circle ->
//                            drawCircle(
//                                color = circle.color,
//                                center = circle.center,
//                                radius = circle.radius,
//                                style = Stroke(width = if (circle.id == selectedCircleId) 5f else 3f)
//                            )
//
//                            if (circle.label.isNotBlank()) {
//                                // Здесь должна быть логика отрисовки текста
//                                // В реальном приложении используйте drawText или Text composable
//                            }
//                        }
//                    }
//                }
//
//                // Панель управления
//                Column(
//                    modifier = Modifier
//                        .width(200.dp)
//                        .fillMaxHeight()
//                        .background(Color.LightGray.copy(alpha = 0.7f))
//                        .padding(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Button(
//                        onClick = { showSettings = !showSettings },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(if (showSettings) "Скрыть настройки" else "Показать настройки")
//                    }
//
//                    if (showSettings) {
//                        Card(
//                            modifier = Modifier.fillMaxWidth(),
//                            elevation = 4.dp,
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Column(modifier = Modifier.padding(8.dp)) {
//                                Text("Настройки круга:", style = MaterialTheme.typography.subtitle1)
//
//                                // Выбор цвета
//                                Text("Цвет круга:")
//                                ColorPicker(
//                                    selectedColor = currentColor,
//                                    onColorSelected = { currentColor = it }
//                                )
//
//                                // Размер круга
//                                Text("Радиус круга: ${currentRadius.toInt()}")
//                                Slider(
//                                    value = currentRadius,
//                                    onValueChange = { currentRadius = it },
//                                    valueRange = 20f..100f
//                                )
//
//                                // Надпись
//                                TextField(
//                                    value = currentLabel,
//                                    onValueChange = { currentLabel = it },
//                                    label = { Text("Надпись на круге") },
//                                    modifier = Modifier.fillMaxWidth()
//                                )
//
//                                Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//                                Text("Настройки линии:", style = MaterialTheme.typography.subtitle1)
//
//                                // Толщина линии
//                                Text("Толщина линии: ${connectionWidth.toInt()}")
//                                Slider(
//                                    value = connectionWidth,
//                                    onValueChange = { connectionWidth = it },
//                                    valueRange = 1f..10f
//                                )
//
//                                // Применить изменения к выбранному элементу
//                                if (selectedCircleId != null || selectedConnectionId != null) {
//                                    Button(
//                                        onClick = {
//                                            saveState()
//                                            appState = appState.copy(
//                                                circles = appState.circles.map { circle ->
//                                                    if (circle.id == selectedCircleId) {
//                                                        circle.copy(
//                                                            color = currentColor,
//                                                            radius = currentRadius,
//                                                            label = currentLabel
//                                                        )
//                                                    } else circle
//                                                },
//                                                connections = appState.connections.map { connection ->
//                                                    if (connection.id == selectedConnectionId) {
//                                                        connection.copy(
//                                                            color = currentColor,
//                                                            width = connectionWidth
//                                                        )
//                                                    } else connection
//                                                }
//                                            )
//                                        },
//                                        modifier = Modifier.fillMaxWidth()
//                                    ) {
//                                        Text("Применить к выбранному")
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // Управление историей
//                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                        Button(
//                            onClick = { undo() },
//                            enabled = undoStack.isNotEmpty(),
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            Text("Отменить (Ctrl+Z)")
//                        }
//                        Button(
//                            onClick = { redo() },
//                            enabled = redoStack.isNotEmpty(),
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            Text("Повторить (Ctrl+Y)")
//                        }
//                    }
//
//                    Button(
//                        onClick = { deleteSelected() },
//                        enabled = selectedCircleId != null || selectedConnectionId != null,
//                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Удалить (Delete)")
//                    }
//
//                    // Статистика
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        elevation = 4.dp
//                    ) {
//                        Column(modifier = Modifier.padding(8.dp)) {
//                            Text("Статистика:", style = MaterialTheme.typography.subtitle1)
//                            Text("Кругов: ${appState.circles.size}")
//                            Text("Соединений: ${appState.connections.size}")
//                            Text("История: ${undoStack.size}")
//                        }
//                    }
//                }
//            }
//        }
//    }
//    }
//}

    // Компонент выбора цвета
    @Composable
    fun ColorPicker(selectedColor: Color, onColorSelected: (Color) -> Unit) {
        val colors = listOf(
            Color.Red, Color.Green, Color.Blue,
            Color.Yellow, Color.Cyan, Color.Magenta,
            Color.Black, Color.Gray
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(color)
                        .border(
                            width = if (color == selectedColor) 3.dp else 1.dp,
                            color = if (color == selectedColor) Color.White else Color.Black
                        )
                        .clickable { onColorSelected(color) }
                )
            }
        }
    }

    // Проверка близости точки к линии
    fun isPointNearLine(point: Offset, lineStart: Offset, lineEnd: Offset, tolerance: Float): Boolean {
        val lineLength = sqrt((lineEnd.x - lineStart.x).pow(2) + (lineEnd.y - lineStart.y).pow(2))
        if (lineLength == 0f) return false

        val u = ((point.x - lineStart.x) * (lineEnd.x - lineStart.x) +
                (point.y - lineStart.y) * (lineEnd.y - lineStart.y)) /
                (lineLength * lineLength)

        val closestPoint = when {
            u < 0 -> lineStart
            u > 1 -> lineEnd
            else -> Offset(
                lineStart.x + u * (lineEnd.x - lineStart.x),
                lineStart.y + u * (lineEnd.y - lineStart.y)
            )
        }

        val distance = sqrt((point.x - closestPoint.x).pow(2) + (point.y - closestPoint.y).pow(2))
        return distance <= tolerance
    }


    // Новая функция для отрисовки соединения с регулируемой кривизной
    fun DrawScope.drawConnection(
        from: Offset,
        to: Offset,
        connection: Connection,
        curvature: Float = 0f,
        isSelected: Boolean = false,
        isFirstConnection: Boolean = false
    ) {
        val color = if (isSelected) Color.Red else connection.color
        val strokeWidth = connection.width.dp.toPx()

        // Рассчитываем направление линии
        val direction = to - from
        val length = sqrt(direction.x.pow(2) + direction.y.pow(2))
        if (length == 0f) return

        // Нормализованный вектор направления
        val directionNormalized = direction / length

        // Рассчитываем точки входа и выхода с учетом радиуса кругов
        val fromAdjusted = from + directionNormalized * 40f // 40 - радиус круга
        val toAdjusted = to - directionNormalized * 40f

        if (curvature != 0f) {
            // Кривая Безье для соединения
            val perpendicular = Offset(-directionNormalized.y, directionNormalized.x)
            val controlPoint = (fromAdjusted + toAdjusted) / 2f + perpendicular * length * curvature

            // Рисуем кривую Безье
            drawPath(
                path = Path().apply {
                    moveTo(fromAdjusted.x, fromAdjusted.y)
                    quadraticBezierTo(controlPoint.x, controlPoint.y, toAdjusted.x, toAdjusted.y)
                },
                color = color,
                style = Stroke(width = strokeWidth)
            )

            // Рисуем стрелку в конце кривой
//        drawArrow(toAdjusted, directionNormalized, color, strokeWidth)
        } else {
            // Прямая линия (для первого соединения)
            drawLine(
                color = color,
                start = fromAdjusted,
                end = toAdjusted,
                strokeWidth = strokeWidth
            )

            // Рисуем стрелку в конце прямой линии
//        drawArrow(toAdjusted, directionNormalized, color, strokeWidth)
        }
    }


// ... (остальные функции остаются без изменений: drawArrow, isPointNearConnection, calculateBezierPoint, isPointNearLine, ColorPicker)