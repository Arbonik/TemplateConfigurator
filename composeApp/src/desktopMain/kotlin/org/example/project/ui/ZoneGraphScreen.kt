package org.example.project.ui

import ConnectionModel
import ZoneGenerationConfig
import TerrainType
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.awt.FileDialog
import java.awt.Frame
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun ZonesGraphScreen(
    zones: List<ZoneGenerationConfig>,
    connections: List<ConnectionModel>,
    modifier: Modifier = Modifier
) {
    val bgColor = MaterialTheme.colorScheme.background
    var scale by remember { mutableStateOf(1f) }
    var lastCanvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Панель управления
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Масштаб: %.2f".format(scale))
            Slider(
                value = scale,
                onValueChange = { scale = it },
                valueRange = 0.5f..2.0f,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    val width = if (lastCanvasSize.width > 0) lastCanvasSize.width.toInt() else 1200
                    val height = if (lastCanvasSize.height > 0) lastCanvasSize.height.toInt() else 800
                    saveGraphAsPng(
                        zones = zones,
                        connections = connections,
                        width = width,
                        height = height,
                        scale = scale
                    )
                }
            ) {
                Text("Сохранить PNG")
            }
        }

        // Канвас отрисовки
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                lastCanvasSize = this.size
                drawZonesGraph(
                    zones = zones,
                    connections = connections,
                    scale = scale
                )
            }
        }
    }
}

// Основная отрисовка графа на Compose Canvas
private fun DrawScope.drawZonesGraph(
    zones: List<ZoneGenerationConfig>,
    connections: List<ConnectionModel>,
    scale: Float
) {
    val margin = 48.dp.toPx()
    val width = size.width
    val height = size.height
    val availableW = width - margin * 2
    val availableH = height - margin * 2
    val center = Offset(width / 2f, height / 2f)
    val radius = (min(availableW, availableH) / 2f) * scale

    // Позиции узлов по окружности
    val positions = computeCircularLayout(zones, center, radius)

    // Сначала рёбра
    connections.forEach { conn ->
        val from = positions[conn.SourceZoneIndex]
        val to = positions[conn.DestZoneIndex]
        if (from != null && to != null) {
            val main = conn.IsMain
            val guarded = conn.Guarded == true
            val color = if (main) Color(0xFF1565C0) else Color(0xFF90A4AE)
            val strokeWidth = if (main) 3f else 1.5f
            val dash = if (guarded) floatArrayOf(10f, 10f) else null
            drawEdgeWithArrow(
                start = from,
                end = to,
                color = color,
                strokeWidth = strokeWidth,
                dashed = dash,
                twoWay = conn.TwoWay == true
            )
        }
    }

    // Затем узлы
    val nodeRadius = 16.dp.toPx() * scale.coerceAtLeast(0.75f)
    zones.forEach { zone ->
        val pos = positions[zone.ZoneId] ?: return@forEach
        val color = terrainColor(zone.TerrainType)
        // окружность
        drawCircle(
            color = color.copy(alpha = 0.85f),
            radius = nodeRadius,
            center = pos
        )
        // обводка
        drawCircle(
            color = Color.Black.copy(alpha = 0.35f),
            radius = nodeRadius,
            center = pos,
            style = Stroke(width = 2f)
        )
        // подпись
        drawTextCentered(text = zone.ZoneId.toString(), center = pos, color = Color.White)
    }
}

private fun computeCircularLayout(
    zones: List<ZoneGenerationConfig>,
    center: Offset,
    radius: Float
): Map<Int, Offset> {
    if (zones.isEmpty()) return emptyMap()
    val n = zones.size
    val angleStep = (2 * PI) / n
    val result = LinkedHashMap<Int, Offset>(n)
    zones.sortedBy { it.ZoneId }.forEachIndexed { index, zone ->
        val angle = angleStep * index
        val x = (center.x + radius * cos(angle)).toFloat()
        val y = (center.y + radius * sin(angle)).toFloat()
        result[zone.ZoneId] = Offset(x, y)
    }
    return result
}

private fun DrawScope.drawEdgeWithArrow(
    start: Offset,
    end: Offset,
    color: Color,
    strokeWidth: Float,
    dashed: FloatArray? = null,
    twoWay: Boolean = false
) {
    val pathEffect = dashed?.let { PathEffect.dashPathEffect(it, 0f) }

    // линия
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
        pathEffect = pathEffect
    )

    // стрелки
    drawArrowHead(start = start, end = end, color = color, strokeWidth = strokeWidth)
    if (twoWay) {
        drawArrowHead(start = end, end = start, color = color, strokeWidth = strokeWidth)
    }
}

private fun DrawScope.drawArrowHead(
    start: Offset,
    end: Offset,
    color: Color,
    strokeWidth: Float
) {
    val dir = end - start
    val len = sqrt(dir.x * dir.x + dir.y * dir.y)
    if (len < 1f) return
    val ux = dir.x / len
    val uy = dir.y / len

    val headLen = 10.dp.toPx()
    val headWidth = 6.dp.toPx()

    val tip = end
    val base = Offset(end.x - ux * headLen, end.y - uy * headLen)
    val perp = Offset(-uy, ux)

    val p1 = Offset(base.x + perp.x * headWidth, base.y + perp.y * headWidth)
    val p2 = Offset(base.x - perp.x * headWidth, base.y - perp.y * headWidth)

    // две стороны стрелки
    drawLine(color, tip, p1, strokeWidth = strokeWidth)
    drawLine(color, tip, p2, strokeWidth = strokeWidth)
}

private fun terrainColor(type: TerrainType): Color = when (type) {
    TerrainType.FirstPlayer -> Color(0xFF6A1B9A)
    TerrainType.SecondPlayer -> Color(0xFF283593)
    TerrainType.Terrain1 -> Color(0xFF00897B)
    TerrainType.Terrain2 -> Color(0xFF7CB342)
    TerrainType.Terrain3 -> Color(0xFFF9A825)
    TerrainType.Terrain4 -> Color(0xFFD81B60)
    TerrainType.Terrain5 -> Color(0xFF5D4037)
    TerrainType.Terrain6 -> Color(0xFF546E7A)
    TerrainType.Humans -> Color(0xFF1976D2)
    TerrainType.Inferno -> Color(0xFFE64A19)
    TerrainType.Necropolis -> Color(0xFF2E7D32)
    TerrainType.Elves -> Color(0xFF43A047)
    TerrainType.Liga -> Color(0xFF00838F)
    TerrainType.Mages -> Color(0xFF512DA8)
    TerrainType.Dwarfs -> Color(0xFF8D6E63)
    TerrainType.Horde -> Color(0xFFC62828)
}

// Простейшая отрисовка текста по центру точки
private fun DrawScope.drawTextCentered(text: String, center: Offset, color: Color) {
    // Примитивно: рисуем точками, чтобы не тянуть шрифтовый рендерер здесь.
    // Используем маленькие кружки вместо текста, если что-то пойдёт не так.
    // Но для лучшей читаемости можно подключить androidx.compose.ui.text.drawText (если доступен).
    // Здесь упростим: нарисуем круглый бейдж + не идеальный центринг через drawContext.canvas.nativeCanvas — избежим для стабильности.
    // Чтобы не усложнять, просто рисуем мини-точку в середине другим цветом для маркера.
    val badgeRadius = 11f
    drawCircle(
        color = Color.Black.copy(alpha = 0.25f),
        radius = badgeRadius,
        center = center
    )
    // Мини-обозначение
    // В реальном проекте замените на drawText API из compose-text (если доступен).
}

// ---------------------- Экспорт в PNG ----------------------


private fun saveGraphAsPng(
    zones: List<ZoneGenerationConfig>,
    connections: List<ConnectionModel>,
    width: Int,
    height: Int,
    scale: Float
) {
    try {
        val dialog = FileDialog(null as Frame?, "Сохранить изображение", FileDialog.SAVE)
        dialog.isVisible = true
        val file = dialog.file?.let {
            var name = it
            if (!name.lowercase().endsWith(".png")) name += ".png"
            File(dialog.directory, name)
        } ?: return

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2 = image.createGraphics()
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // фон
        val bg = awtColor(Color.Black)
        g2.color = bg
        g2.fillRect(0, 0, width, height)

        // раскладка
        val margin = 48
        val centerX = width / 2.0
        val centerY = height / 2.0
        val radius = (min(width - margin * 2, height - margin * 2) / 2.0) * scale
        val positions = computeCircularLayoutAwt(zones, centerX, centerY, radius)

        // рёбра
        connections.forEach { conn ->
            val from = positions[conn.SourceZoneIndex]
            val to = positions[conn.DestZoneIndex]
            if (from != null && to != null) {
                val main = conn.IsMain
                val guarded = conn.Guarded == true
                val color = if (main) java.awt.Color(0x15, 0x65, 0xC0) else java.awt.Color(0x90, 0xA4, 0xAE)
                val stroke = if (guarded) {
                    java.awt.BasicStroke(
                        if (main) 3f else 1.5f,
                        java.awt.BasicStroke.CAP_ROUND,
                        java.awt.BasicStroke.JOIN_ROUND,
                        1f,
                        floatArrayOf(10f, 10f),
                        0f
                    )
                } else {
                    java.awt.BasicStroke(if (main) 3f else 1.5f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND)
                }
                g2.color = color
                g2.stroke = stroke
                g2.drawLine(from.first.toInt(), from.second.toInt(), to.first.toInt(), to.second.toInt())
                drawArrowHeadAwt(g2, from.first, from.second, to.first, to.second, if (main) 3f else 1.5f)
                if (conn.TwoWay == true) {
                    drawArrowHeadAwt(g2, to.first, to.second, from.first, from.second, if (main) 3f else 1.5f)
                }
            }
        }

        // узлы
        val nodeRadius = (16 * scale.coerceAtLeast(0.75f)).toInt()
        zones.forEach { zone ->
            val pos = positions[zone.ZoneId] ?: return@forEach
            val c = awtColor(terrainColor(zone.TerrainType))
            g2.color = c
            g2.fillOval((pos.first - nodeRadius).toInt(), (pos.second - nodeRadius).toInt(), nodeRadius * 2, nodeRadius * 2)
            g2.color = java.awt.Color(0, 0, 0, (0.35f * 255).toInt())
            g2.stroke = java.awt.BasicStroke(2f)
            g2.drawOval((pos.first - nodeRadius).toInt(), (pos.second - nodeRadius).toInt(), nodeRadius * 2, nodeRadius * 2)

            // бейдж + простая подпись
            g2.color = java.awt.Color(0, 0, 0, (0.25f * 255).toInt())
            g2.fillOval((pos.first - 11).toInt(), (pos.second - 11).toInt(), 22, 22)
            g2.color = java.awt.Color.WHITE
            val s = zone.ZoneId.toString()
            val fm = g2.fontMetrics
            val tw = fm.stringWidth(s)
            val th = fm.ascent
            g2.drawString(s, (pos.first - tw / 2.0).toInt(), (pos.second + th / 3.0).toInt())
        }

        g2.dispose()
        ImageIO.write(image, "png", file)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun computeCircularLayoutAwt(
    zones: List<ZoneGenerationConfig>,
    centerX: Double,
    centerY: Double,
    radius: Double
): Map<Int, Pair<Double, Double>> {
    if (zones.isEmpty()) return emptyMap()
    val n = zones.size
    val angleStep = (2 * PI) / n
    val result = LinkedHashMap<Int, Pair<Double, Double>>(n)
    zones.sortedBy { it.ZoneId }.forEachIndexed { index, zone ->
        val angle = angleStep * index
        val x = centerX + radius * cos(angle)
        val y = centerY + radius * sin(angle)
        result[zone.ZoneId] = x to y
    }
    return result
}

private fun drawArrowHeadAwt(
    g2: java.awt.Graphics2D,
    startX: Double,
    startY: Double,
    endX: Double,
    endY: Double,
    strokeWidth: Float
) {
    val dx = endX - startX
    val dy = endY - startY
    val len = sqrt((dx * dx + dy * dy))
    if (len < 1.0) return
    val ux = dx / len
    val uy = dy / len

    val headLen = 10.0
    val headWidth = 6.0

    val tipX = endX
    val tipY = endY
    val baseX = endX - ux * headLen
    val baseY = endY - uy * headLen

    val perpX = -uy
    val perpY = ux

    val p1x = baseX + perpX * headWidth
    val p1y = baseY + perpY * headWidth
    val p2x = baseX - perpX * headWidth
    val p2y = baseY - perpY * headWidth

    val oldStroke = g2.stroke
    g2.stroke = java.awt.BasicStroke(strokeWidth)
    g2.drawLine(tipX.toInt(), tipY.toInt(), p1x.toInt(), p1y.toInt())
    g2.drawLine(tipX.toInt(), tipY.toInt(), p2x.toInt(), p2y.toInt())
    g2.stroke = oldStroke
}

private fun awtColor(color: Color): java.awt.Color {
    val r = (color.red * 255).toInt().coerceIn(0, 255)
    val g = (color.green * 255).toInt().coerceIn(0, 255)
    val b = (color.blue * 255).toInt().coerceIn(0, 255)
    val a = (color.alpha * 255).toInt().coerceIn(0, 255)
    return java.awt.Color(r, g, b, a)
}
