import CastleTypePickerDialog
import ConfigItem
import ConfigSectionHeader
import HeroConfigItem
import HeroType
import HeroTypePickerDialog
import PlayerConfigItem
import PlayerType
import PlayerTypePickerDialog
import RaceConfigItem
import SelectionDialog
import SpellItem
import SpellPickerDialog
import SpellSelectionItem
import SpellType
import SpellsList
import SpellsPreview
import StartSpellsByHero
import StartSpellsByPlayer
import StartSpellsByRace
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.serialization.Serializable
import project.data.enums.CastleType

@Composable
fun StartSpellsConfigEditor(
    config: StartSpellsConfig?,
    onConfigChanged: (StartSpellsConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var showGlobalSpellsPicker by remember { mutableStateOf(false) }
    var showPlayerSpellsPicker by remember { mutableStateOf<StartSpellsByPlayer?>(null) }
    var showRaceSpellsPicker by remember { mutableStateOf<StartSpellsByRace?>(null) }
    var showHeroSpellsPicker by remember { mutableStateOf<StartSpellsByHero?>(null) }
    var showAddPlayerConfig by remember { mutableStateOf(false) }
    var showAddRaceConfig by remember { mutableStateOf(false) }
    var showAddHeroConfig by remember { mutableStateOf(false) }

    val playersType = remember(config) {
        PlayerType.values().toList() - (config?.SpellsByPlayers?.map { it.PlayerType } ?: emptyList()).toSet()
    }

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ConfigSectionHeader(
                title = "Global Spells",
                onAddClick = { showGlobalSpellsPicker = true }
            )
            SpellsList(
                spells = config?.GlobalSpells ?: emptyList(),
                onEditClick = null,
                onRemoveClick = { spell ->
                    if (config != null)
                        onConfigChanged(config.copy(GlobalSpells = config.GlobalSpells - spell))
                }
            )
        }

        item {
            ConfigSectionHeader(
                title = "Spells by Player",
                onAddClick = if (playersType.isNotEmpty()) {
                    { showAddPlayerConfig = true }
                } else null
            )
            if (config != null)
            config.SpellsByPlayers.forEach { playerConfig ->
                PlayerConfigItem(
                    config = playerConfig,
                    onEditSpells = { showPlayerSpellsPicker = playerConfig },
                    onRemove = {
                        onConfigChanged(
                            config.copy(
                                SpellsByPlayers = config.SpellsByPlayers - playerConfig
                            )
                        )
                    }
                )
            }
        }

        item {
            ConfigSectionHeader(
                title = "Spells by Race",
                onAddClick = { showAddRaceConfig = true }
            )
            if (config != null)
            config.SpellsByRaces.forEach { raceConfig ->
                RaceConfigItem(
                    config = raceConfig,
                    onEditSpells = { showRaceSpellsPicker = raceConfig },
                    onRemove = {
                        onConfigChanged(
                            config.copy(
                                SpellsByRaces = config.SpellsByRaces - raceConfig
                            )
                        )
                    }
                )
            }
        }

        item {
            ConfigSectionHeader(
                title = "Spells by Hero",
                onAddClick = { showAddHeroConfig = true }
            )
            if (config != null)
            config.SpellsByHeroes.forEach { heroConfig ->
                HeroConfigItem(
                    config = heroConfig,
                    onEditSpells = { showHeroSpellsPicker = heroConfig },
                    onRemove = {
                        onConfigChanged(
                            config.copy(
                                SpellsByHeroes = config.SpellsByHeroes - heroConfig
                            )
                        )
                    }
                )
            }
        }
    }

    // Dialogs for picking spells
    if (showGlobalSpellsPicker) {
        SpellPickerDialog(
            selectedSpells = config?.GlobalSpells ?: emptyList(),
            onDismiss = { showGlobalSpellsPicker = false },
            onConfirm = { spells ->
                if (config != null)
                onConfigChanged(config.copy(GlobalSpells = spells))
                showGlobalSpellsPicker = false
            }
        )
    }

    showPlayerSpellsPicker?.let { playerConfig ->
        SpellPickerDialog(
            selectedSpells = playerConfig.Spells,
            onDismiss = { showPlayerSpellsPicker = null },
            onConfirm = { spells ->
                if (config != null) {
                    val updatedList = config.SpellsByPlayers.map {
                        if (it == playerConfig) it.copy(Spells = spells) else it
                    }
                    onConfigChanged(config.copy(SpellsByPlayers = updatedList))
                    showPlayerSpellsPicker = null
                }
            }
        )
    }

    showRaceSpellsPicker?.let { raceConfig ->
        SpellPickerDialog(
            selectedSpells = raceConfig.Spells,
            onDismiss = { showRaceSpellsPicker = null },
            onConfirm = { spells ->
                if (config != null) {
                    val updatedList = config.SpellsByRaces.map {
                        if (it == raceConfig) it.copy(Spells = spells) else it
                    }
                    onConfigChanged(config.copy(SpellsByRaces = updatedList))
                    showRaceSpellsPicker = null
                }
            }
        )
    }

    showHeroSpellsPicker?.let { heroConfig ->
        SpellPickerDialog(
            selectedSpells = heroConfig.Spells,
            onDismiss = { showHeroSpellsPicker = null },
            onConfirm = { spells ->
                if (config != null) {
                    val updatedList = config.SpellsByHeroes.map {
                        if (it == heroConfig) it.copy(Spells = spells) else it
                    }
                    onConfigChanged(config.copy(SpellsByHeroes = updatedList))
                    showHeroSpellsPicker = null
                }
            }
        )
    }

    // Dialogs for adding new configurations
    if (showAddPlayerConfig) {
        PlayerTypePickerDialog(
            items = playersType,
            onDismiss = { showAddPlayerConfig = false },
            onConfirm = { playerType ->
                if (config != null) {
                    val newConfig = StartSpellsByPlayer(playerType, emptyList())
                    onConfigChanged(
                        config.copy(
                            SpellsByPlayers = config.SpellsByPlayers + newConfig
                        )
                    )
                    showAddPlayerConfig = false
                    showPlayerSpellsPicker = newConfig
                }
            }
        )
    }

    if (showAddRaceConfig) {
        CastleTypePickerDialog(
            onDismiss = { showAddRaceConfig = false },
            onConfirm = { castleType ->
                if (config != null) {
                    val newConfig = StartSpellsByRace(castleType, emptyList())
                    onConfigChanged(
                        config.copy(
                            SpellsByRaces = config.SpellsByRaces + newConfig
                        )
                    )
                    showAddRaceConfig = false
                    showRaceSpellsPicker = newConfig
                }
            }
        )
    }

    if (showAddHeroConfig) {
        HeroTypePickerDialog(
            onDismiss = { showAddHeroConfig = false },
            onConfirm = { heroType ->
                if (config != null) {
                    val newConfig = StartSpellsByHero(heroType, emptyList())
                    onConfigChanged(
                        config.copy(
                            SpellsByHeroes = config.SpellsByHeroes + newConfig
                        )
                    )
                    showAddHeroConfig = false
                    showHeroSpellsPicker = newConfig
                }
            }
        )
    }
}

@Composable
private fun ConfigSectionHeader(
    title: String,
    onAddClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
        if (onAddClick != null)
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
    }
}

@Composable
private fun SpellsList(
    spells: List<SpellType>,
    onEditClick: (() -> Unit)?,
    onRemoveClick: (SpellType) -> Unit,
    modifier: Modifier = Modifier
) {
    if (spells.isEmpty() && onEditClick != null) {
        OutlinedButton(
            onClick = onEditClick,
            modifier = modifier.fillMaxWidth()
        ) {
            Text("Add Spells")
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            spells.forEach { spell ->
                SpellItem(
                    spell = spell,
                    onRemoveClick = { onRemoveClick(spell) }
                )
            }
            if (onEditClick != null) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Spells")
                }
            }
        }
    }
}

@Composable
private fun SpellItem(
    spell: SpellType,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(spell.name)
            IconButton(onClick = onRemoveClick) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}

@Composable
private fun PlayerConfigItem(
    config: StartSpellsByPlayer,
    onEditSpells: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConfigItem(
        title = config.PlayerType.description,
        content = {
            SpellsPreview(spells = config.Spells)
        },
        onEditClick = onEditSpells,
        onRemoveClick = onRemove,
        modifier = modifier
    )
}

@Composable
private fun RaceConfigItem(
    config: StartSpellsByRace,
    onEditSpells: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConfigItem(
        title = config.CastleType.name,
        content = {
            SpellsPreview(spells = config.Spells)
        },
        onEditClick = onEditSpells,
        onRemoveClick = onRemove,
        modifier = modifier
    )
}

@Composable
private fun HeroConfigItem(
    config: StartSpellsByHero,
    onEditSpells: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConfigItem(
        title = config.HeroType.name,
        content = {
            SpellsPreview(spells = config.Spells)
        },
        onEditClick = onEditSpells,
        onRemoveClick = onRemove,
        modifier = modifier
    )
}

@Composable
private fun ConfigItem(
    title: String,
    content: @Composable () -> Unit,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onRemoveClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            content()

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Spells")
            }
        }
    }
}

@Composable
private fun SpellsPreview(spells: List<SpellType>) {
    if (spells.isEmpty()) {
        Text(
            text = "No spells selected",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        val displayedSpells = if (spells.size > 3) {
            spells.take(3) + SpellType.Bless
        } else {
            spells
        }

        Column {
            displayedSpells.forEach { spell ->
                Text(
                    text = "• ${spell.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun SpellPickerDialog(
    selectedSpells: List<SpellType>,
    onDismiss: () -> Unit,
    onConfirm: (List<SpellType>) -> Unit
) {
    var currentSelection by remember { mutableStateOf(selectedSpells.toSet()) }
    var searchQuery by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.width(400.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Select Spells",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search spells") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                val filteredSpells = remember(searchQuery) {
                    if (searchQuery.isBlank()) {
                        SpellType.values().toList()
                    } else {
                        SpellType.values().filter {
                            it.name.contains(searchQuery, ignoreCase = true)
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredSpells) { spell ->
                        SpellSelectionItem(
                            spell = spell,
                            isSelected = currentSelection.contains(spell),
                            onSelectionChange = { selected ->
                                currentSelection = if (selected) {
                                    currentSelection + spell
                                } else {
                                    currentSelection - spell
                                }
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
                        onConfirm(currentSelection.toList())
                    }
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
private fun SpellSelectionItem(
    spell: SpellType,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = onSelectionChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = spell.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PlayerTypePickerDialog(
    items: List<PlayerType>,
    onDismiss: () -> Unit,
    onConfirm: (PlayerType) -> Unit
) {
    SelectionDialog(
        title = "Select Player Type",
        items = items,
        itemName = { it.description },
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}

@Composable
private fun CastleTypePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (project.data.enums.CastleType) -> Unit
) {
    SelectionDialog(
        title = "Select Race",
        items = _root_ide_package_.project.data.enums.CastleType.values().toList(),
        itemName = { it.name },
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}

@Composable
private fun HeroTypePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (HeroType) -> Unit
) {
    SelectionDialog(
        title = "Select Hero",
        items = HeroType.values().toList(),
        itemName = { it.name },
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}

@Composable
private fun <T> SelectionDialog(
    title: String,
    items: List<T>,
    itemName: (T) -> String,
    onDismiss: () -> Unit,
    onConfirm: (T) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.width(400.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                val filteredItems = remember(searchQuery) {
                    if (searchQuery.isBlank()) {
                        items
                    } else {
                        items.filter {
                            itemName(it).contains(searchQuery, ignoreCase = true)
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredItems) { item ->
                        Card(
                            onClick = {
                                onConfirm(item)
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = itemName(item),
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}