import IntValueConfig
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import updateConfig

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun IntValueConfigWidget(
    config: IntValueConfig?,
    onConfigChanged: (IntValueConfig) -> Unit,
    label: String = "",
    modifier: Modifier = Modifier
) {
    var isRandomized by remember(config) { mutableStateOf(config?.MinValue != config?.MaxValue) }
    var minValueText by remember(config) { mutableStateOf(TextFieldValue(config?.MinValue?.toString() ?: "")) }
    var maxValueText by remember(config) { mutableStateOf(TextFieldValue(config?.MaxValue?.toString() ?: "")) }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(120.dp)
            ) {
                Checkbox(
                    checked = isRandomized,
                    onCheckedChange = { newValue ->
                        isRandomized = newValue
                        if (!newValue) {
                            // Синхронизация значений при отключении рандома
                            val commonValue = minValueText.text.ifEmpty { maxValueText.text }
                            minValueText = TextFieldValue(commonValue)
                            maxValueText = TextFieldValue(commonValue)
                            updateConfig(commonValue, commonValue, onConfigChanged)
                        }
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Рандом",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            AnimatedContent(
                targetState = isRandomized,
                transitionSpec = {
                    if (targetState) {
                        // Переход к двум полям
                        (fadeIn() + slideInHorizontally { it }).togetherWith(
                            fadeOut() + slideOutHorizontally { -it }
                        )
                    } else {
                        // Переход к одному полю
                        (fadeIn() + slideInHorizontally { -it }).togetherWith(
                            fadeOut() + slideOutHorizontally { it }
                        )
                    }.using(SizeTransform(clip = false))
                },
                modifier = Modifier.weight(1f)
            ) { randomized ->
                if (randomized) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = minValueText,
                            onValueChange = { newValue ->
                                if (newValue.text.isEmpty() || newValue.text.toIntOrNull() != null) {
                                    minValueText = newValue
                                    updateConfig(newValue.text, maxValueText.text, onConfigChanged)
                                }
                            },
                            label = { Text("Min") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = maxValueText,
                            onValueChange = { newValue ->
                                if (newValue.text.isEmpty() || newValue.text.toIntOrNull() != null) {
                                    maxValueText = newValue
                                    updateConfig(minValueText.text, newValue.text, onConfigChanged)
                                }
                            },
                            label = { Text("Max") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = minValueText,
                        onValueChange = { newValue ->
                            if (newValue.text.isEmpty() || newValue.text.toIntOrNull() != null) {
                                minValueText = newValue
                                maxValueText = newValue
                                updateConfig(newValue.text, newValue.text, onConfigChanged)
                            }
                        },
                        label = { Text("Value") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

private fun updateConfig(
    minText: String,
    maxText: String,
    onConfigChanged: (IntValueConfig) -> Unit
) {
    val minValue = minText.toIntOrNull()
    val maxValue = maxText.toIntOrNull()

    when {
        minValue != null && maxValue != null -> onConfigChanged(IntValueConfig(minValue, maxValue))
        minValue != null -> onConfigChanged(IntValueConfig(minValue, minValue))
        maxValue != null -> onConfigChanged(IntValueConfig(maxValue, maxValue))
        else -> onConfigChanged(IntValueConfig(null, null))
    }
}