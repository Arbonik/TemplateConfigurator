import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import project.ui.ListWithDialog
import project.ui.common.AddIcon
import project.ui.common.DecimalInputField
import project.ui.common.DeleteIcon

@Composable
fun PandoraBoxConfigEditor(
    config: PandoraBoxConfig,
    onConfigChanged: (PandoraBoxConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf<PandoraBoxConfigField?>(null) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PandoraConfigTab(
                label = "Gold",
                isActive = activeTab == PandoraBoxConfigField.GOLD,
                onClick = {
                    activeTab = PandoraBoxConfigField.GOLD
                }
            )

            PandoraConfigTab(
                label = "Exp",
                isActive = activeTab == PandoraBoxConfigField.EXP,
                onClick = {
                    activeTab = PandoraBoxConfigField.EXP
                }
            )

            PandoraConfigTab(
                label = "Artifacts",
                isActive = activeTab == PandoraBoxConfigField.ARTIFACTS,
                onClick = {
                    activeTab = PandoraBoxConfigField.ARTIFACTS
                }
            )

            PandoraConfigTab(
                label = "Creatures",
                isActive = activeTab == PandoraBoxConfigField.CREATURES,
                onClick = {
                    activeTab = PandoraBoxConfigField.CREATURES
                }
            )

            PandoraConfigTab(
                label = "Spells",
                isActive = activeTab == PandoraBoxConfigField.SPELLS,
                onClick = {
                    activeTab = PandoraBoxConfigField.SPELLS
                }
            )

            PandoraConfigTab(
                label = "Resources",
                isActive = activeTab == PandoraBoxConfigField.RESOURCES,
                onClick = {
                    activeTab = PandoraBoxConfigField.RESOURCES
                }
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            when (activeTab) {
                PandoraBoxConfigField.GOLD -> GoldAmountEditor(
                    amounts = config.GoldAmount,
                    onAmountsChanged = { newAmounts ->
                        onConfigChanged(config.copy(GoldAmount = newAmounts))
                    }
                )

                PandoraBoxConfigField.EXP -> GoldAmountEditor(
                    amounts = config.ExpAmount,
                    onAmountsChanged = { newAmounts ->
                        onConfigChanged(config.copy(ExpAmount = newAmounts))
                    }
                )

                PandoraBoxConfigField.ARTIFACTS -> ArtifactsEditor(
                    artifacts = config.Artifacts,
                    onArtifactsChanged = { newArtifacts ->
                        onConfigChanged(config.copy(Artifacts = newArtifacts))
                    }
                )

                PandoraBoxConfigField.CREATURES -> CreaturesEditor(
                    creatures = config.PandoraCreatureConfig,
                    onCreaturesChanged = { newCreatures ->
                        onConfigChanged(config.copy(PandoraCreatureConfig = newCreatures))
                    }
                )

                PandoraBoxConfigField.SPELLS -> SpellsEditor(
                    spells = config.Spells,
                    onSpellsChanged = { newSpells ->
                        onConfigChanged(config.copy(Spells = newSpells))
                    }
                )

                PandoraBoxConfigField.RESOURCES -> ResourcesEditor(
                    resources = config.Resources,
                    onResourcesChanged = { newResources ->
                        onConfigChanged(config.copy(Resources = newResources))
                    }
                )

                null -> Text("Select a configuration type above")
            }
        }
    }
}

@Composable
private fun PandoraConfigTab(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(label)
    }
}

@Composable
private fun GoldAmountEditor(
    amounts: List<Long>,
    onAmountsChanged: (List<Long>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        AddIcon {
            onAmountsChanged(amounts + 0L)
        }
        amounts.forEachIndexed { index, amount ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                DecimalInputField(
                    title = "$index",
                    value = amount.toString(),
                    onValueChange = {
                        val newAmount = it.toLongOrNull() ?: 0L
                        onAmountsChanged(amounts.toMutableList().apply {
                            this[index] = newAmount
                        })
                    },
                )
                DeleteIcon {
                    val newAmounts = amounts.toMutableList()
                    newAmounts.removeAt(index)
                    onAmountsChanged(newAmounts)
                }
            }
        }
    }
}

@Composable
private fun ArtifactsEditor(
    artifacts: List<PandoraArtifactConfig>,
    onArtifactsChanged: (List<PandoraArtifactConfig>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        AddIcon {
            onArtifactsChanged(
                artifacts + PandoraArtifactConfig(
                    Artifacts = emptyList(),
                    ArtifactCategories = emptyList(),
                    ArtifactSlots = emptyList(),
                    CostRanges = emptyList()
                )
            )
        }
        Column(modifier = modifier) {
            artifacts.forEachIndexed { index, artifact ->
                ArtifactConfigEditor(
                    config = artifact,
                    onConfigChanged = { newConfig ->
                        val newList = artifacts.toMutableList()
                        newList[index] = newConfig
                        onArtifactsChanged(newList)
                    },
                    onRemove = {
                        val newList = artifacts.toMutableList()
                        newList.removeAt(index)
                        onArtifactsChanged(newList)
                    }
                )
            }
        }
    }
}

@Composable
private fun ArtifactConfigEditor(
    config: PandoraArtifactConfig,
    onConfigChanged: (PandoraArtifactConfig) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            ListWithDialog(
                label = "Artifact Types",
                itemTitle = { it.description },
                allItems = ArtifactType.entries,
                currentItems = config.Artifacts,
                onConfigChanged = { newSelection ->
                    onConfigChanged(config.copy(Artifacts = newSelection))
                },
            )

            ListWithDialog(
                label = "Artifact Categories",
                itemTitle = { it.description },
                allItems = ArtifactCategory.entries,
                currentItems = config.ArtifactCategories,
                onConfigChanged = { newSelection ->
                    onConfigChanged(config.copy(ArtifactCategories = newSelection))
                },
            )

            ListWithDialog(
                label = "Artifact Slots",
                itemTitle = { it.description },
                allItems = ArtifactSlot.entries,
                currentItems = config.ArtifactSlots,
                onConfigChanged = { newSelection ->
                    onConfigChanged(config.copy(ArtifactSlots = newSelection))
                },
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("CostRanges")
                AddIcon {
                    onConfigChanged(config.copy(CostRanges = config.CostRanges + IntValueConfig(0)))
                }
            }
            FlowColumn {
                config.CostRanges.forEachIndexed { index, range ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NullableIntValueConfigEditor(
                            label = "Cost Range",
                            range,
                            onConfigChanged = {
                                if (it != null)
                                    onConfigChanged(
                                        config.copy(
                                            CostRanges = config.CostRanges.toMutableList().apply {
                                                set(index, it)
                                            }
                                        )
                                    )
                            }
                        )
                        DeleteIcon {
                            onConfigChanged(
                                config.copy(
                                    CostRanges = config.CostRanges.toMutableList().apply { removeAt(index) })
                            )
                        }
                    }
                }
            }

            // Count
            NumberInput(
                value = config.Count,
                onValueChanged = { newValue ->
                    onConfigChanged(config.copy(Count = newValue))
                },
                label = "Count (optional)"
            )

            Button(
                onClick = onRemove,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Remove This Config")
            }
        }
    }
}

@Composable
private fun CreaturesEditor(
    creatures: List<PandoraCreatureConfig>,
    onCreaturesChanged: (List<PandoraCreatureConfig>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AddIcon {
            onCreaturesChanged(
                creatures + PandoraCreatureConfig(
                    TiersPool = emptyList(),
                    PlayerType = PlayerType.ANY,
                    CreatureIds = emptyList()
                )
            )
        }

        creatures.forEachIndexed { index, creature ->
            CreatureConfigEditor(
                config = creature,
                onConfigChanged = { newConfig ->
                    val newList = creatures.toMutableList()
                    newList[index] = newConfig
                    onCreaturesChanged(newList)
                },
                onRemove = {
                    val newList = creatures.toMutableList()
                    newList.removeAt(index)
                    onCreaturesChanged(newList)
                }
            )
        }
    }
}

@Composable
private fun CreatureConfigEditor(
    config: PandoraCreatureConfig,
    onConfigChanged: (PandoraCreatureConfig) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Tiers pool
            AllowedTiersInput(
                label = "Tiers Pool",
                AllowedTiers = config.TiersPool ?: emptyList(),
                onConfigChanged = { newTiers ->
                    onConfigChanged(config.copy(TiersPool = newTiers))
                },
            )

            // Boolean flags
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = config.NoGrades == true,
                    onCheckedChange = { checked ->
                        onConfigChanged(config.copy(NoGrades = if (checked) true else null))
                    }
                )
                Text("No Grades")

                Spacer(modifier = Modifier.width(8.dp))

                Checkbox(
                    checked = config.Grades == true,
                    onCheckedChange = { checked ->
                        onConfigChanged(config.copy(Grades = if (checked) true else null))
                    }
                )
                Text("Grades")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = config.Neutrals == true,
                    onCheckedChange = { checked ->
                        onConfigChanged(config.copy(Neutrals = if (checked) true else null))
                    }
                )
                Text("Neutrals")

                Spacer(modifier = Modifier.width(8.dp))

                Checkbox(
                    checked = config.PlayerFactions,
                    onCheckedChange = { checked ->
                        onConfigChanged(config.copy(PlayerFactions = checked))
                    }
                )
                Text("PlayerFactions")
            }

            // Player type
            EnumDropdownRow(
                label = "Player Type",
                currentValue = config.PlayerType,
                itemTitle = { it?.description?.ifEmpty { it.name } ?: "" },
                values = PlayerType.entries - config.PlayerType,
                onValueSelected = { newType ->
                    onConfigChanged(config.copy(PlayerType = newType))
                },
            )
            Text("TerrainTypes")
            AddIcon {
                onConfigChanged(
                    config.copy(TerrainTypes = (config.TerrainTypes ?: emptyList()) + TerrainType.Terrain1)
                )
            }
            config.TerrainTypes?.forEachIndexed { index, terrainType ->
                EnumDropdownRow(
                    label = "",
                    currentValue = terrainType,
                    itemTitle = { it.name },
                    values = TerrainType.entries - terrainType,
                    onValueSelected = { newType ->
                        onConfigChanged(
                            config.copy(
                                TerrainTypes = config.TerrainTypes.toMutableList().apply { set(index, newType) })
                        )
                    }
                )
            }

            // Creature IDs
            Text("Creature IDs")
            config.CreatureIds?.forEachIndexed { index, id ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = id,
                        onValueChange = { newId ->
                            val newIds = config.CreatureIds.toMutableList()
                            newIds[index] = newId
                            onConfigChanged(config.copy(CreatureIds = newIds))
                        },
                        label = { Text("Creature ID") },
                        modifier = Modifier.weight(1f)
                    )

                    DeleteIcon {
                        val newIds = config.CreatureIds.toMutableList()
                        newIds.removeAt(index)
                        onConfigChanged(config.copy(CreatureIds = newIds))
                    }
                }
            }

            AddIcon {
                val newIds = config.CreatureIds?.toMutableList()
                newIds?.add("")
                onConfigChanged(config.copy(CreatureIds = newIds))
            }

            NumberInput(
                value = config.GrowMultiplier,
                onValueChanged = { newValue ->
                    onConfigChanged(config.copy(GrowMultiplier = newValue))
                },
                label = "Grow Multiplier (optional)"
            )

            Button(
                onClick = onRemove,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Remove This Config")
            }
        }
    }
}

@Composable
private fun SpellsEditor(
    spells: List<PandoraSpellConfig>,
    onSpellsChanged: (List<PandoraSpellConfig>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AddIcon {
            onSpellsChanged(
                spells + PandoraSpellConfig(
                    Spells = emptyList(),
                    MagicSchools = emptyList(),
                    MagicTiers = emptyList(),
                    RuneTiers = emptyList(),
                    WarCryTiers = emptyList()
                )
            )
        }
        spells.forEachIndexed { index, spell ->
            SpellConfigEditor(
                config = spell,
                onConfigChanged = { newConfig ->
                    val newList = spells.toMutableList()
                    newList[index] = newConfig
                    onSpellsChanged(newList)
                },
                onRemove = {
                    val newList = spells.toMutableList()
                    newList.removeAt(index)
                    onSpellsChanged(newList)
                }
            )
        }
    }
}

@Composable
private fun SpellConfigEditor(
    config: PandoraSpellConfig,
    onConfigChanged: (PandoraSpellConfig) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Spells selection
            ListWithDialog(
                label = "Spell Types",
                itemTitle = { it.description },
                allItems = SpellType.entries,
                currentItems = config.Spells,
                onConfigChanged = { newSelection ->
                    onConfigChanged(config.copy(Spells = newSelection))
                },
            )

            // Magic schools selection
            ListWithDialog(
                label = "Magic Schools",
                itemTitle = { it.description },
                allItems = MagicSchool.entries,
                currentItems = config.MagicSchools,
                onConfigChanged = { newSelection ->
                    onConfigChanged(config.copy(MagicSchools = newSelection))
                },
            )

            // Magic tiers
            AllowedTiersInput(
                label = "Magic Tiers",
                allTiers = (1L..5L).toList(),
                AllowedTiers = config.MagicTiers,
                onConfigChanged = { newTiers ->
                    onConfigChanged(config.copy(MagicTiers = newTiers))
                }
            )

            // Rune tiers
            AllowedTiersInput(
                label = "Rune Tiers",
                allTiers = (1L..3L).toList(),
                AllowedTiers = config.RuneTiers,
                onConfigChanged = { newTiers ->
                    onConfigChanged(config.copy(RuneTiers = newTiers))
                },
            )

            // War cry tiers
            AllowedTiersInput(
                label = "War Cry Tiers",
                allTiers = (1L..3L).toList(),
                AllowedTiers = config.WarCryTiers,
                onConfigChanged = { newTiers ->
                    onConfigChanged(config.copy(WarCryTiers = newTiers))
                },
            )

            // Count
            NumberInput(
                value = config.Count,
                onValueChanged = { newValue ->
                    onConfigChanged(config.copy(Count = newValue))
                },
                label = "Count (optional)"
            )

            Button(
                onClick = onRemove,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Remove This Config")
            }
        }
    }
}

@Composable
private fun ResourcesEditor(
    resources: List<ResourcesConfig>,
    onResourcesChanged: (List<ResourcesConfig>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AddIcon {
            onResourcesChanged(
                resources + ResourcesConfig(
                    Wood = null,
                    Ore = null,
                    Mercury = null,
                    Crystals = null,
                    Sulfur = null,
                    Gems = null,
                    Gold = null
                )
            )
        }

        resources.forEachIndexed { index, resource ->
            ResourceConfigEditor(
                config = resource,
                onConfigChanged = { newConfig ->
                    val newList = resources.toMutableList()
                    newList[index] = newConfig
                    onResourcesChanged(newList)
                },
                onRemove = {
                    val newList = resources.toMutableList()
                    newList.removeAt(index)
                    onResourcesChanged(newList)
                }
            )
        }
    }
}

@Composable
private fun ResourceConfigEditor(
    config: ResourcesConfig,
    onConfigChanged: (ResourcesConfig) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {

            DeleteIcon(onRemove)
            // Wood
            NullableIntValueConfigEditor(
                label = "Wood",
                config = config.Wood,
                onConfigChanged = { newRange ->
                    onConfigChanged(config.copy(Wood = newRange))
                }
            )

            // Ore
            NullableIntValueConfigEditor(
                config = config.Ore,
                onConfigChanged = { newRange ->
                    onConfigChanged(config.copy(Ore = newRange))
                },
                label = "Ore",
            )

            // Mercury
            NullableIntValueConfigEditor(
                config = config.Mercury,
                onConfigChanged = { newRange ->
                    onConfigChanged(config.copy(Mercury = newRange))
                },
                label = "Mercury",
            )

            // Crystals
            NullableIntValueConfigEditor(
                config = config.Crystals,
                onConfigChanged = { newRange ->
                    onConfigChanged(config.copy(Crystals = newRange))
                },
                label = "Crystals",
            )

            // Sulfur
            NullableIntValueConfigEditor(
                config = config.Sulfur,
                onConfigChanged = { newRange ->
                    onConfigChanged(config.copy(Sulfur = newRange))
                },
                label = "Sulfur",
            )

            // Gems
            NullableIntValueConfigEditor(
                config = config.Gems,
                onConfigChanged = { newRange ->
                    onConfigChanged(config.copy(Gems = newRange))
                },
                label = "Gems",
            )

            // Gold
            NullableIntValueConfigEditor(
                config = config.Gold,
                onConfigChanged = { newRange ->
                    onConfigChanged(config.copy(Gold = newRange))
                },
                label = "Gold",
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T : Enum<T>> EnumDropdown(
    items: List<T>,
    selectedItem: T,
    onSelectionChanged: (T) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedItem.name,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
            modifier = Modifier.fillMaxWidth().onClick {
                expanded = true
            },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onSelectionChanged(item)
                    expanded = false
                }, text = {
                    Text(item.name)
                })
            }
        }
    }
}

@Composable
fun NumberListEditor(
    numbers: List<Long>,
    onNumbersChanged: (List<Long>) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(label)

        numbers.forEachIndexed { index, number ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = number.toString(),
                    onValueChange = { newValue ->
                        val newNumbers = numbers.toMutableList()
                        newNumbers[index] = newValue.toLongOrNull() ?: 0
                        onNumbersChanged(newNumbers)
                    },
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {
                    val newNumbers = numbers.toMutableList()
                    newNumbers.removeAt(index)
                    onNumbersChanged(newNumbers)
                }) {
                    Icon(Icons.Default.Delete, "Remove")
                }
            }
        }

        Button(onClick = {
            onNumbersChanged(numbers + 0)
        }) {
            Text("Add Number")
        }
    }
}

@Composable
private fun IntRangeEditor(
    range: IntValueConfig?,
    onRangeChanged: (IntValueConfig?) -> Unit,
    label: String,
    onRemove: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label)

            if (onRemove != null) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, "Remove")
                }
            }
        }

        if (range != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = range.MinValue?.toString() ?: "",
                    onValueChange = { newValue ->
                        onRangeChanged(range.copy(MinValue = newValue.toIntOrNull()))
                    },
                    label = { Text("Min") },
                    modifier = Modifier.weight(1f)
                )

                Text("to", modifier = Modifier.padding(horizontal = 8.dp))

                OutlinedTextField(
                    value = range.MaxValue?.toString() ?: "",
                    onValueChange = { newValue ->
                        onRangeChanged(range.copy(MaxValue = newValue.toIntOrNull()))
                    },
                    label = { Text("Max") },
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Button(onClick = {
                onRangeChanged(IntValueConfig())
            }) {
                Text("Add $label Range")
            }
        }
    }
}

private enum class PandoraBoxConfigField {
    GOLD, EXP, ARTIFACTS, CREATURES, SPELLS, RESOURCES
}