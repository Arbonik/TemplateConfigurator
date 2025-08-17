package org.example.project.ui

import ConnectionModel
import ZoneGenerationConfig
import IntValueConfig
import TerrainType
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import java.awt.FileDialog
import java.awt.Frame
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Интерактивный экран визуализации зон и связей:
 * - узлы с одинаковым ZoneId отображаются одной точкой
 * - по клику на узел/ребро появляется панель информации
 * - на узлах показан TreasureDensity, на рёбрах GuardStrenght
 * - возможен экспорт в PNG
 */
@Composable
fun ZonesGraphInteractiveScreen(
    zones: List<ZoneGenerationConfig>,
    connections: List<ConnectionModel>,
    modifier: Modifier = Modifier
) {
    // Уникализируем зоны по ZoneId (берём первую встреченную конфигурацию)
    val uniqueZones: List<ZoneGenerationConfig> = remember(zones) {
        zones.groupBy { it.ZoneId }.map { (_, list) -> list.first() }.sortedBy { it.ZoneId }
    }

    // Состояния UI
    var scale by remember { mutableStateOf(1f) }
    var canvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    var selectedZoneId by remember { mutableStateOf<Int?>(null) }
    var selectedConnection by remember { mutableStateOf<ConnectionModel?>(null) }

    // Вычисляем раскладку для оверлеев/хитов
    val positions by remember(uniqueZones, canvasSize, scale) {
        mutableStateOf(
            computeCircularLayout(
                uniqueZones,
                center = Offset(canvasSize.width / 2f, canvasSize.height / 2f),
                radius = ((min(canvasSize.width, canvasSize.height) - 96f) / 2f) * scale
            )
        )
    }

    val nodeRadiusPx by remember(scale) { mutableStateOf(16f * scale.coerceAtLeast(0.75f)) }
    val edgeHitThresholdPx = 8f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Панель управления
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
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
                    val w = if (canvasSize.width > 0) canvasSize.width.toInt() else 1200
                    val h = if (canvasSize.height > 0) canvasSize.height.toInt() else 800
                    saveGraphAsPng(
                        zones = uniqueZones,
                        connections = connections,
                        width = w,
                        height = h,
                        scale = scale
                    )
                }
            ) {
                Text("Сохранить PNG")
            }
        }

        // Граф
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            val density = LocalDensity.current
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(uniqueZones, connections, scale, positions) {
                        detectTapGestures { pos ->
                            // Сброс выделения
                            selectedZoneId = null
                            selectedConnection = null

                            // 1) хит по узлам
                            val hitZone = uniqueZones.firstOrNull { zone ->
                                val p = positions[zone.ZoneId]
                                p != null && (p - pos).getDistance() <= nodeRadiusPx + 6f
                            }
                            if (hitZone != null) {
                                selectedZoneId = hitZone.ZoneId
                                return@detectTapGestures
                            }

                            // 2) хит по рёбрам
                            val seg = connections.minByOrNull { conn ->
                                val a = positions[conn.SourceZoneIndex]
                                val b = positions[conn.DestZoneIndex]
                                if (a == null || b == null) Float.MAX_VALUE
                                else distancePointToSegment(pos, a, b)
                            }
                            seg?.let { c ->
                                val a = positions[c.SourceZoneIndex]
                                val b = positions[c.DestZoneIndex]
                                if (a != null && b != null) {
                                    val d = distancePointToSegment(pos, a, b)
                                    if (d <= edgeHitThresholdPx) {
                                        selectedConnection = c
                                    }
                                }
                            }
                        }
                    }
            ) {
                canvasSize = this.size
                drawZonesGraph(
                    zones = uniqueZones,
                    connections = connections,
                    scale = scale,
                    selectedZoneId = selectedZoneId,
                    selectedConnection = selectedConnection
                )
            }

            // Оверлеи: подписи узлов (ID + TD) и рёбер (GuardStrenght) в виде чипов
            uniqueZones.forEach { zone ->
                val center = positions[zone.ZoneId] ?: return@forEach
                val idText = "ID ${zone.ZoneId}"
                val tdText = "TD: ${formatIntValueConfig(zone.TreasureDensity)}"

                // Чип рядом с узлом
                val offsetPx = Offset(center.x + nodeRadiusPx + 8f, center.y - nodeRadiusPx - 8f)
                ChipOverlay(
                    texts = listOf(idText, tdText),
                    accent = terrainColor(zone.TerrainType),
                    modifier = Modifier.absoluteOffset(
                        x = with(density) { offsetPx.x.toDp() },
                        y = with(density) { offsetPx.y.toDp() }
                    )
                )
            }

            // Подписи рёбер: GuardStrenght как чип
            connections.forEach { conn ->
                val a = positions[conn.SourceZoneIndex]
                val b = positions[conn.DestZoneIndex]
                if (a != null && b != null) {
                    val mid = Offset((a.x + b.x) / 2f, (a.y + b.y) / 2f)
                    val dir = b - a
                    val len = dir.getDistance().coerceAtLeast(1f)
                    val perp = Offset(-dir.y / len, dir.x / len)
                    val labelPos = mid + perp * (12f + 8f * scale)

                    val gs = conn.GuardStrenght?.toString() ?: "-"
                    val guardedAccent = if (conn.Guarded == true) Color(0xFFD32F2F) else Color(0xFF607D8B)

                    ChipOverlay(
                        texts = listOf("GS: $gs"),
                        accent = guardedAccent,
                        modifier = Modifier.absoluteOffset(
                            x = with(density) { labelPos.x.toDp() },
                            y = with(density) { labelPos.y.toDp() }
                        )
                    )
                }
            }

            // Легенда
            LegendOverlay(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            )
        }

        Divider()

        // Панель информации о выбранном
        if (selectedZoneId != null) {
            val z = uniqueZones.firstOrNull { it.ZoneId == selectedZoneId }
            if (z != null) {
                InfoZonePanel(z)
            }
        } else if (selectedConnection != null) {
            InfoConnectionPanel(selectedConnection!!)
        } else {
            Text(
                "Нажмите на зону или связь для просмотра информации",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InfoZonePanel(zone: ZoneGenerationConfig) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Зона: ${zone.ZoneId}", style = MaterialTheme.typography.titleSmall)
        Text("Террейн: ${zone.TerrainType}", style = MaterialTheme.typography.bodySmall)
        Text("TreasureDensity: ${formatIntValueConfig(zone.TreasureDensity)}", style = MaterialTheme.typography.bodySmall)
        zone.Size?.let { Text("Size: ${formatIntValueConfig(it)}", style = MaterialTheme.typography.bodySmall) }
        zone.Town?.let { Text("Town: $it", style = MaterialTheme.typography.bodySmall) }
    }
}

@Composable
private fun InfoConnectionPanel(conn: ConnectionModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Связь: ${conn.SourceZoneIndex} -> ${conn.DestZoneIndex}", style = MaterialTheme.typography.titleSmall)
        Text("Main: ${conn.IsMain}", style = MaterialTheme.typography.bodySmall)
        Text("Guarded: ${conn.Guarded ?: false}", style = MaterialTheme.typography.bodySmall)
        Text("GuardStrenght: ${conn.GuardStrenght ?: "-"}", style = MaterialTheme.typography.bodySmall)
        Text("TwoWay: ${conn.TwoWay ?: false}", style = MaterialTheme.typography.bodySmall)
        conn.RoadType?.let { Text("RoadType: $it", style = MaterialTheme.typography.bodySmall) }
    }
}

@Composable
private fun ChipOverlay(
    texts: List<String>,
    accent: Color,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Surface(
        tonalElevation = 2.dp,
        shape = androidx.compose.material3.MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, accent.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(accent, shape = androidx.compose.foundation.shape.CircleShape)
            )
            texts.forEach { t ->
                Text(
                    t,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun LegendOverlay(modifier: Modifier = Modifier) {
    androidx.compose.material3.Surface(
        tonalElevation = 1.dp,
        shape = androidx.compose.material3.MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Легенда", style = MaterialTheme.typography.labelMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                LegendItem(label = "Main", lineColor = Color(0xFF1565C0), dashed = false, thick = true)
                LegendItem(label = "Secondary", lineColor = Color(0xFF90A4AE), dashed = false, thick = false)
                LegendItem(label = "Guarded", lineColor = Color(0xFFD32F2F), dashed = true, thick = true)
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, lineColor: Color, dashed: Boolean, thick: Boolean) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(width = 36.dp, height = 12.dp)) {
            val y = size.height / 2f
            val stroke = if (thick) 3f else 1.5f
            val effect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f) else null
            drawLine(
                color = lineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = stroke,
                cap = StrokeCap.Round,
                pathEffect = effect
            )
        }
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

// ---------------------- Отрисовка на Canvas ----------------------

private fun DrawScope.drawZonesGraph(
    zones: List<ZoneGenerationConfig>,
    connections: List<ConnectionModel>,
    scale: Float,
    selectedZoneId: Int?,
    selectedConnection: ConnectionModel?
) {
    val margin = 48f
    val width = size.width
    val height = size.height
    val center = Offset(width / 2f, height / 2f)
    val radius = (min(width - margin * 2, height - margin * 2) / 2f) * scale

    // Фоновая тонкая сетка
    val gridStep = 32.dp.toPx()
    val gridColor = Color.Black.copy(alpha = 0.04f)
    for (x in 0..(width / gridStep).toInt()) {
        val xPos = x * gridStep
        drawLine(gridColor, start = Offset(xPos, 0f), end = Offset(xPos, height), strokeWidth = 1f)
    }
    for (y in 0..(height / gridStep).toInt()) {
        val yPos = y * gridStep
        drawLine(gridColor, start = Offset(0f, yPos), end = Offset(width, yPos), strokeWidth = 1f)
    }

    // Позиции узлов
    val positions = computeCircularLayout(zones, center, radius)

    // Рёбра
    connections.forEach { conn ->
        val from = positions[conn.SourceZoneIndex]
        val to = positions[conn.DestZoneIndex]
        if (from != null && to != null) {
            val main = conn.IsMain
            val guarded = conn.Guarded == true
            val isSelected = selectedConnection === conn
            val color = when {
                isSelected -> Color(0xFF00BCD4)
                main -> Color(0xFF1565C0)
                else -> Color(0xFF90A4AE)
            }
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

    // Узлы
    val nodeRadius = 16.dp.toPx() * scale.coerceAtLeast(0.75f)
    zones.forEach { zone ->
        val pos = positions[zone.ZoneId] ?: return@forEach
        val color = terrainColor(zone.TerrainType)
        val isSelected = selectedZoneId == zone.ZoneId

        // "тень"
        drawCircle(
            color = Color.Black.copy(alpha = 0.08f),
            radius = nodeRadius + 3f,
            center = Offset(pos.x + 1.5f, pos.y + 1.5f)
        )
        // заполнение
        drawCircle(
            color = color.copy(alpha = if (isSelected) 1.0f else 0.9f),
            radius = nodeRadius,
            center = pos
        )
        // обводка
        drawCircle(
            color = if (isSelected) Color(0xFF00BCD4) else Color.Black.copy(alpha = 0.35f),
            radius = nodeRadius + if (isSelected) 2f else 0f,
            center = pos,
            style = Stroke(width = if (isSelected) 3f else 2f)
        )
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

    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
        pathEffect = pathEffect
    )

    drawArrowHead(start = start, end = end, color = color, strokeWidth = strokeWidth)
    if (twoWay) drawArrowHead(start = end, end = start, color = color, strokeWidth = strokeWidth)
}

private fun DrawScope.drawArrowHead(
    start: Offset,
    end: Offset,
    color: Color,
    strokeWidth: Float
) {
    val dir = end - start
    val len = dir.getDistance()
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

    drawLine(color, tip, p1, strokeWidth = strokeWidth)
    drawLine(color, tip, p2, strokeWidth = strokeWidth)
}

// ---------------------- Вспомогательные функции ----------------------

private fun Offset.getDistance(): Float = sqrt(x * x + y * y)

private fun distancePointToSegment(p: Offset, a: Offset, b: Offset): Float {
    val ab = b - a
    val ap = p - a
    val ab2 = ab.x * ab.x + ab.y * ab.y
    if (ab2 == 0f) return (p - a).getDistance()
    val t = ((ap.x * ab.x + ap.y * ab.y) / ab2).coerceIn(0f, 1f)
    val proj = Offset(a.x + ab.x * t, a.y + ab.y * t)
    return (p - proj).getDistance()
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

private fun formatIntValueConfig(v: IntValueConfig?): String {
    if (v == null) return "-"
    val min = v.MinValue
    val max = v.MaxValue
    return when {
        min == null && max == null -> "-"
        min != null && max != null && min == max -> "$min"
        else -> "${min ?: "?"}-${max ?: "?"}"
    }
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
        g2.color = java.awt.Color(245, 245, 247)
        g2.fillRect(0, 0, width, height)

        // раскладка
        val margin = 48
        val centerX = width / 2.0
        val centerY = height / 2.0
        val radius = (min(width - margin * 2, height - margin * 2) / 2.0) * scale
        val positions = computeCircularLayoutAwt(zones, centerX, centerY, radius)

        // рёбра + подпись GuardStrenght
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

                // подпись GuardStrenght
                val midX = (from.first + to.first) / 2.0
                val midY = (from.second + to.second) / 2.0
                val dx = to.first - from.first
                val dy = to.second - from.second
                val len = kotlin.math.max(1.0, kotlin.math.sqrt(dx * dx + dy * dy))
                val px = -dy / len
                val py = dx / len
                val labelX = midX + px * (10.0 + 6.0 * scale)
                val labelY = midY + py * (10.0 + 6.0 * scale)

                g2.color = java.awt.Color(33, 33, 33)
                val text = (conn.GuardStrenght ?: "-").toString()
                val fm = g2.fontMetrics
                g2.drawString(text, (labelX - fm.stringWidth(text) / 2.0).toInt(), (labelY + fm.ascent / 3.0).toInt())
            }
        }

        // узлы + подпись TreasureDensity
        val nodeRadius = (16 * scale.coerceAtLeast(0.75f)).toInt()
        zones.forEach { zone ->
            val pos = positions[zone.ZoneId] ?: return@forEach
            val c = terrainAwtColor(zone.TerrainType)
            g2.color = c
            g2.fillOval((pos.first - nodeRadius).toInt(), (pos.second - nodeRadius).toInt(), nodeRadius * 2, nodeRadius * 2)
            g2.color = java.awt.Color(0, 0, 0, (0.35f * 255).toInt())
            g2.stroke = java.awt.BasicStroke(2f)
            g2.drawOval((pos.first - nodeRadius).toInt(), (pos.second - nodeRadius).toInt(), nodeRadius * 2, nodeRadius * 2)

            // подпись TreasureDensity рядом
            val td = formatIntValueConfig(zone.TreasureDensity)
            g2.color = java.awt.Color(33, 33, 33)
            val fm = g2.fontMetrics
            val label = "TD: $td"
            val labelX = pos.first + nodeRadius + 6.0
            val labelY = pos.second - nodeRadius - 6.0
            g2.drawString(label, labelX.toInt(), (labelY + fm.ascent / 3.0).toInt())
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

private fun terrainAwtColor(type: TerrainType): java.awt.Color = when (type) {
    TerrainType.FirstPlayer -> java.awt.Color(0x6A, 0x1B, 0x9A)
    TerrainType.SecondPlayer -> java.awt.Color(0x28, 0x35, 0x93)
    TerrainType.Terrain1 -> java.awt.Color(0x00, 0x89, 0x7B)
    TerrainType.Terrain2 -> java.awt.Color(0x7C, 0xB3, 0x42)
    TerrainType.Terrain3 -> java.awt.Color(0xF9, 0xA8, 0x25)
    TerrainType.Terrain4 -> java.awt.Color(0xD8, 0x1B, 0x60)
    TerrainType.Terrain5 -> java.awt.Color(0x5D, 0x40, 0x37)
    TerrainType.Terrain6 -> java.awt.Color(0x54, 0x6E, 0x7A)
    TerrainType.Humans -> java.awt.Color(0x19, 0x76, 0xD2)
    TerrainType.Inferno -> java.awt.Color(0xE6, 0x4A, 0x19)
    TerrainType.Necropolis -> java.awt.Color(0x2E, 0x7D, 0x32)
    TerrainType.Elves -> java.awt.Color(0x43, 0xA0, 0x47)
    TerrainType.Liga -> java.awt.Color(0x00, 0x83, 0x8F)
    TerrainType.Mages -> java.awt.Color(0x51, 0x2D, 0xA8)
    TerrainType.Dwarfs -> java.awt.Color(0x8D, 0x6E, 0x63)
    TerrainType.Horde -> java.awt.Color(0xC6, 0x28, 0x28)
}
