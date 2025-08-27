import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun IntValueConfigWidget(
    config: IntValueConfig,
    onConfigChanged: (IntValueConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRandomized by remember { mutableStateOf(config.MinValue != config.MaxValue) }
    var minValueText by remember { mutableStateOf(config.MinValue?.toString() ?: "") }
    var maxValueText by remember { mutableStateOf(config.MaxValue?.toString() ?: "") }

    val isError by remember(minValueText, maxValueText) {
        derivedStateOf {
            (maxValueText.toIntOrNull() ?: 0) < (minValueText.toIntOrNull() ?: 0)
        }
    }

    Column(modifier = modifier) {
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
                        if (isRandomized) {
                            // Синхронизация значений при отключении рандома
                            minValueText = (config.MinValue ?: config.MaxValue ?: 0).toString()
                            maxValueText = (config.MaxValue ?: config.MinValue ?: 0).toString()
                            onConfigChanged(
                                IntValueConfig(
                                    MinValue = minValueText.toIntOrNull() ?: 0,
                                    MaxValue = maxValueText.toIntOrNull() ?: 0
                                )
                            )
                        } else {
                            minValueText = config.MinValue?.toString() ?: ""
                            maxValueText = config.MinValue?.toString() ?: ""
                            onConfigChanged(IntValueConfig(config.MinValue))
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
                                if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                                    minValueText = newValue
                                    onConfigChanged(config.copy(MinValue = newValue.toIntOrNull()))
                                }
                            },
                            isError = minValueText.toIntOrNull() == null || isError,
                            label = { Text("Min") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = maxValueText,
                            onValueChange = { newValue ->
                                if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                                    maxValueText = newValue
                                    onConfigChanged(config.copy(MaxValue = newValue.toIntOrNull()))
                                }
                            },
                            isError = maxValueText.toIntOrNull() == null || isError,
                            label = { Text("Max") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = minValueText,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                                minValueText = newValue
                                maxValueText = newValue
                                onConfigChanged(config.copy(MinValue = newValue.toIntOrNull(), MaxValue = newValue.toIntOrNull()))
                            }
                        },
                        isError = minValueText.toIntOrNull() == null,
                        label = { Text("Value") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
