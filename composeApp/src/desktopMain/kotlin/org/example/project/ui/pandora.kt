import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PandoraBoxConfigEditor(
    config: PandoraBoxConfig,
    onConfigChanged: (PandoraBoxConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf<PandoraBoxConfigField?>(null) }

    Column(modifier = modifier) {
        // Tab selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PandoraConfigTab(
                label = "Gold",
                isActive = activeTab == PandoraBoxConfigField.GOLD,
                onClick = {
                    activeTab = PandoraBoxConfigField.GOLD
                    onConfigChanged(
                        config.copy(
                            ExpAmount = emptyList(),
                            Artifacts = emptyList(),
                            PandoraCreatureConfig = emptyList(),
                            Spells = emptyList(),
                            Resources = emptyList()
                        )
                    )
                }
            )

            PandoraConfigTab(
                label = "Exp",
                isActive = activeTab == PandoraBoxConfigField.EXP,
                onClick = {
                    activeTab = PandoraBoxConfigField.EXP
                    onConfigChanged(
                        config.copy(
                            GoldAmount = emptyList(),
                            Artifacts = emptyList(),
                            PandoraCreatureConfig = emptyList(),
                            Spells = emptyList(),
                            Resources = emptyList()
                        )
                    )
                }
            )

            PandoraConfigTab(
                label = "Artifacts",
                isActive = activeTab == PandoraBoxConfigField.ARTIFACTS,
                onClick = {
                    activeTab = PandoraBoxConfigField.ARTIFACTS
                    onConfigChanged(
                        config.copy(
                            GoldAmount = emptyList(),
                            ExpAmount = emptyList(),
                            PandoraCreatureConfig = emptyList(),
                            Spells = emptyList(),
                            Resources = emptyList()
                        )
                    )
                }
            )

            PandoraConfigTab(
                label = "Creatures",
                isActive = activeTab == PandoraBoxConfigField.CREATURES,
                onClick = {
                    activeTab = PandoraBoxConfigField.CREATURES
                    onConfigChanged(
                        config.copy(
                            GoldAmount = emptyList(),
                            ExpAmount = emptyList(),
                            Artifacts = emptyList(),
                            Spells = emptyList(),
                            Resources = emptyList()
                        )
                    )
                }
            )

            PandoraConfigTab(
                label = "Spells",
                isActive = activeTab == PandoraBoxConfigField.SPELLS,
                onClick = {
                    activeTab = PandoraBoxConfigField.SPELLS
                    onConfigChanged(
                        config.copy(
                            GoldAmount = emptyList(),
                            ExpAmount = emptyList(),
                            Artifacts = emptyList(),
                            PandoraCreatureConfig = emptyList(),
                            Resources = emptyList()
                        )
                    )
                }
            )

            PandoraConfigTab(
                label = "Resources",
                isActive = activeTab == PandoraBoxConfigField.RESOURCES,
                onClick = {
                    activeTab = PandoraBoxConfigField.RESOURCES
                    onConfigChanged(
                        config.copy(
                            GoldAmount = emptyList(),
                            ExpAmount = emptyList(),
                            Artifacts = emptyList(),
                            PandoraCreatureConfig = emptyList(),
                            Spells = emptyList()
                        )
                    )
                }
            )
        }

        // Content area
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            when (activeTab) {
                PandoraBoxConfigField.GOLD -> GoldAmountEditor(
                    amounts = config.GoldAmount,
                    onAmountsChanged = { newAmounts ->
                        onConfigChanged(config.copy(GoldAmount = newAmounts))
                    }
                )

                PandoraBoxConfigField.EXP -> ExpAmountEditor(
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
            backgroundColor = if (isActive) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
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
    // Implement gold amount editing UI
    // For example, a list of number inputs
}

@Composable
private fun ExpAmountEditor(
    amounts: List<Long>,
    onAmountsChanged: (List<Long>) -> Unit,
    modifier: Modifier = Modifier
) {
    // Implement exp amount editing UI
}

@Composable
private fun ArtifactsEditor(
    artifacts: List<PandoraArtifactConfig>,
    onArtifactsChanged: (List<PandoraArtifactConfig>) -> Unit,
    modifier: Modifier = Modifier
) {
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

        Button(onClick = {
            onArtifactsChanged(
                artifacts + PandoraArtifactConfig(
                    Artifacts = emptyList(),
                    ArtifactCategories = emptyList(),
                    ArtifactSlots = emptyList(),
                    CostRanges = emptyList()
                )
            )
        }) {
            Text("Add Artifact Config")
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
            // Artifacts selection
            SearchableEnumDropdown(
                items = ArtifactType.values().toList(),
                selectedItems = config.Artifacts,
                onSelectionChanged = { newSelection ->
                    onConfigChanged(config.copy(Artifacts = newSelection))
                },
                label = "Artifact Types"
            )

            // Categories selection
            SearchableEnumDropdown(
                items = ArtifactCategory.values().toList(),
                selectedItems = config.ArtifactCategories,
                onSelectionChanged = { newSelection ->
                    onConfigChanged(config.copy(ArtifactCategories = newSelection))
                },
                label = "Artifact Categories"
            )

            // Slots selection
            SearchableEnumDropdown(
                items = ArtifactSlot.values().toList(),
                selectedItems = config.ArtifactSlots,
                onSelectionChanged = { newSelection ->
                    onConfigChanged(config.copy(ArtifactSlots = newSelection))
                },
                label = "Artifact Slots"
            )

            config.CostRanges.forEachIndexed { index, range ->
                IntRangeEditor(
                    range = range,
                    onRangeChanged = { newRange ->
                        val newRanges = config.CostRanges.toMutableList()
                        newRanges[index] = newRange ?: IntValueConfig(0)
                        onConfigChanged(config.copy(CostRanges = newRanges))
                    },
                    label = "CostRange $index",
                    onRemove = {
                        val newRanges = config.CostRanges.toMutableList()
                        newRanges.removeAt(index)
                        onConfigChanged(config.copy(CostRanges = newRanges))
                    }
                )
            }

            Button(onClick = {
                val newRanges = config.CostRanges.toMutableList()
                newRanges.add(IntValueConfig())
                onConfigChanged(config.copy(CostRanges = newRanges))
            }) {
                Text("Add Cost Range")
            }

            // Count
            NumberInput(
                value = config.Count?.toString() ?: "",
                onValueChanged = { newValue ->
                    onConfigChanged(config.copy(Count = newValue.toIntOrNull()))
                },
                label = "Count (optional)"
            )

            Button(
                onClick = onRemove,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
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

        Button(onClick = {
            onCreaturesChanged(
                creatures + PandoraCreatureConfig(
                    TiersPool = emptyList(),
                    PlayerType = PlayerType.ANY,
                    CreatureIds = emptyList()
                )
            )
        }) {
            Text("Add Creature Config")
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
            NumberListEditor(
                numbers = config.TiersPool,
                onNumbersChanged = { newTiers ->
                    onConfigChanged(config.copy(TiersPool = newTiers))
                },
                label = "Tiers Pool"
            )

            // Boolean flags
            Row {
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

            Row {
                Checkbox(
                    checked = config.Neutrals == true,
                    onCheckedChange = { checked ->
                        onConfigChanged(config.copy(Neutrals = if (checked) true else null))
                    }
                )
                Text("Neutrals")

                Spacer(modifier = Modifier.width(8.dp))

                Checkbox(
                    checked = config.NonPlayerFactions == true,
                    onCheckedChange = { checked ->
                        onConfigChanged(config.copy(NonPlayerFactions = if (checked) true else null))
                    }
                )
                Text("Non-Player Factions")
            }

            Checkbox(
                checked = config.PlayerFactions == true,
                onCheckedChange = { checked ->
                    onConfigChanged(config.copy(PlayerFactions = if (checked) true else null))
                }
            )
            Text("Player Factions")

            // Player type
            EnumDropdown(
                items = PlayerType.values().toList(),
                selectedItem = config.PlayerType,
                onSelectionChanged = { newType ->
                    onConfigChanged(config.copy(PlayerType = newType))
                },
                label = "Player Type"
            )

            // Creature IDs
            Text("Creature IDs", style = MaterialTheme.typography.h6)
            config.CreatureIds.forEachIndexed { index, id ->
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

                    IconButton(onClick = {
                        val newIds = config.CreatureIds.toMutableList()
                        newIds.removeAt(index)
                        onConfigChanged(config.copy(CreatureIds = newIds))
                    }) {
                        Icon(Icons.Default.Delete, "Remove")
                    }
                }
            }

            Button(onClick = {
                val newIds = config.CreatureIds.toMutableList()
                newIds.add("")
                onConfigChanged(config.copy(CreatureIds = newIds))
            }) {
                Text("Add Creature ID")
            }

            // Grow multiplier
            NumberInput(
                value = config.GrowMultiplier?.toString() ?: "",
                onValueChanged = { newValue ->
                    onConfigChanged(config.copy(GrowMultiplier = newValue.toDoubleOrNull()))
                },
                label = "Grow Multiplier (optional)"
            )

            Button(
                onClick = onRemove,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
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

        Button(onClick = {
            onSpellsChanged(
                spells + PandoraSpellConfig(
                    Spells = emptyList(),
                    MagicSchools = emptyList(),
                    MagicTiers = emptyList(),
                    RuneTiers = emptyList(),
                    WarCryTiers = emptyList()
                )
            )
        }) {
            Text("Add Spell Config")
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
    Card(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Spells selection
            SearchableEnumDropdown(
                items = SpellType.values().toList(),
                selectedItems = config.Spells,
                onSelectionChanged = { newSelection ->
                    onConfigChanged(config.copy(Spells = newSelection))
                },
                label = "Spell Types"
            )

            // Magic schools selection
            SearchableEnumDropdown(
                items = MagicSchool.values().toList(),
                selectedItems = config.MagicSchools,
                onSelectionChanged = { newSelection ->
                    onConfigChanged(config.copy(MagicSchools = newSelection))
                },
                label = "Magic Schools"
            )

            // Magic tiers
            NumberListEditor(
                numbers = config.MagicTiers,
                onNumbersChanged = { newTiers ->
                    onConfigChanged(config.copy(MagicTiers = newTiers))
                },
                label = "Magic Tiers"
            )

            // Rune tiers
            NumberListEditor(
                numbers = config.RuneTiers,
                onNumbersChanged = { newTiers ->
                    onConfigChanged(config.copy(RuneTiers = newTiers))
                },
                label = "Rune Tiers"
            )

            // War cry tiers
            NumberListEditor(
                numbers = config.WarCryTiers,
                onNumbersChanged = { newTiers ->
                    onConfigChanged(config.copy(WarCryTiers = newTiers))
                },
                label = "War Cry Tiers"
            )

            // Count
            NumberInput(
                value = config.Count?.toString() ?: "",
                onValueChanged = { newValue ->
                    onConfigChanged(config.copy(Count = newValue.toIntOrNull()))
                },
                label = "Count (optional)"
            )

            Button(
                onClick = onRemove,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
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

        Button(onClick = {
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
        }) {
            Text("Add Resource Config")
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
            // Wood
            IntRangeEditor(
                range = config.Wood,
                onRangeChanged = { newRange ->
                    onConfigChanged(config.copy(Wood = newRange))
                },
                label = "Wood",
                onRemove = {
                    onConfigChanged(config.copy(Wood = null))
                }
            )

            // Ore
            IntRangeEditor(
                range = config.Ore,
                onRangeChanged = { newRange ->
                    onConfigChanged(config.copy(Ore = newRange))
                },
                label = "Ore",
                onRemove = {
                    onConfigChanged(config.copy(Ore = null))
                }
            )

            // Mercury
            IntRangeEditor(
                range = config.Mercury,
                onRangeChanged = { newRange ->
                    onConfigChanged(config.copy(Mercury = newRange))
                },
                label = "Mercury",
                onRemove = {
                    onConfigChanged(config.copy(Mercury = null))
                }
            )

            // Crystals
            IntRangeEditor(
                range = config.Crystals,
                onRangeChanged = { newRange ->
                    onConfigChanged(config.copy(Crystals = newRange))
                },
                label = "Crystals",
                onRemove = {
                    onConfigChanged(config.copy(Crystals = null))
                }
            )

            // Sulfur
            IntRangeEditor(
                range = config.Sulfur,
                onRangeChanged = { newRange ->
                    onConfigChanged(config.copy(Sulfur = newRange))
                },
                label = "Sulfur",
                onRemove = {
                    onConfigChanged(config.copy(Sulfur = null))
                }
            )

            // Gems
            IntRangeEditor(
                range = config.Gems,
                onRangeChanged = { newRange ->
                    onConfigChanged(config.copy(Gems = newRange))
                },
                label = "Gems",
                onRemove = {
                    onConfigChanged(config.copy(Gems = null))
                }
            )

            // Gold
            IntRangeEditor(
                range = config.Gold,
                onRangeChanged = { newRange ->
                    onConfigChanged(config.copy(Gold = newRange))
                },
                label = "Gold",
                onRemove = {
                    onConfigChanged(config.copy(Gold = null))
                }
            )

            Button(
                onClick = onRemove,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
            ) {
                Text("Remove This Config")
            }
        }
    }
}

// Helper components

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T : Enum<T>> SearchableEnumDropdown(
    items: List<T>,
    selectedItems: List<T>,
    onSelectionChanged: (List<T>) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = if (selectedItems.isEmpty()) "Select $label" else selectedItems.joinToString { it.name },
            onValueChange = {

            },
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
            // Search field
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search $label") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )

            // Filtered items
            val filteredItems = items.filter {
                it.name.contains(searchText, ignoreCase = true)
            }

            filteredItems.forEach { item ->
                DropdownMenuItem(onClick = {
                    val newSelection = if (selectedItems.contains(item)) {
                        selectedItems - item
                    } else {
                        selectedItems + item
                    }
                    onSelectionChanged(newSelection)
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedItems.contains(item),
                            onCheckedChange = null
                        )
                        Text(item.name)
                    }
                }
            }
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
                }) {
                    Text(item.name)
                }
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
        Text(label, style = MaterialTheme.typography.h6)

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
            Text(label, style = MaterialTheme.typography.subtitle1)

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

@Composable
private fun NumberInput(
    value: String,
    onValueChanged: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth()
    )
}

private enum class PandoraBoxConfigField {
    GOLD, EXP, ARTIFACTS, CREATURES, SPELLS, RESOURCES
}