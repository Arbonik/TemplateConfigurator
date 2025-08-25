import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.text.ifEmpty

@Composable
fun StartSpellsConfigEditor(
    config: StartSpellsConfig?,
    onConfigChanged: (StartSpellsConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        if (config == null)
            onConfigChanged(
                StartSpellsConfig(
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList()
                )
            )
    }
    var showGlobalSpellsPicker by remember { mutableStateOf(false) }
    var showPlayerSpellsPicker by remember { mutableStateOf<StartSpellsByPlayer?>(null) }
    var showRaceSpellsPicker by remember { mutableStateOf<StartSpellsByRace?>(null) }
    var showHeroSpellsPicker by remember { mutableStateOf<StartSpellsByHero?>(null) }
    var showAddPlayerConfig by remember { mutableStateOf(false) }
    var showAddRaceConfig by remember { mutableStateOf(false) }
    var showAddHeroConfig by remember { mutableStateOf(false) }

    val playersType = remember(config) {
        PlayerType.entries - (config?.SpellsByPlayers?.map { it.PlayerType } ?: emptyList()).toSet()
    }

    FlowColumn(
        modifier = modifier.padding(16.dp).verticalScroll(rememberScrollState()),
    ) {
        ConfigSectionHeader(
            title = "Global Spells",
            onAddClick = { showGlobalSpellsPicker = true }
        )
        SpellsList(
            spells = config?.GlobalSpells ?: emptyList(),
            onRemoveClick = { spell ->
                if (config != null)
                    onConfigChanged(config.copy(GlobalSpells = config.GlobalSpells - spell))
            }
        )

        ConfigSectionHeader(
            title = "Spells by Player",
            onAddClick = { showAddPlayerConfig = true }
        )
        config?.SpellsByPlayers?.forEach { playerConfig ->
            PlayerConfigItem(
                config = playerConfig,
                onEditSpells = { showPlayerSpellsPicker = playerConfig },
                onRemove = {
                    onConfigChanged(
                        config.copy(
                            SpellsByPlayers = config.SpellsByPlayers - playerConfig
                        )
                    )
                },
                onRemoveSpell = { spell ->
                    onConfigChanged(
                        config.copy(
                            SpellsByPlayers = config.SpellsByPlayers.map {
                                it.copy(Spells = it.Spells - spell)
                            }
                        )
                    )
                }
            )
        }

        ConfigSectionHeader(
            title = "Spells by Race",
            onAddClick = { showAddRaceConfig = true }
        )
        config?.SpellsByRaces?.forEach { raceConfig ->
            RaceConfigItem(
                config = raceConfig,
                onEditSpells = { showRaceSpellsPicker = raceConfig },
                onRemove = {
                    onConfigChanged(
                        config.copy(
                            SpellsByRaces = config.SpellsByRaces - raceConfig
                        )
                    )
                },
                onRemoveSpell = { spell ->
                    onConfigChanged(
                        config.copy(
                            SpellsByRaces = config.SpellsByRaces.map {
                                it.copy(Spells = it.Spells - spell)
                            }
                        )
                    )
                }
            )
        }

        ConfigSectionHeader(
            title = "Spells by Hero",
            onAddClick = { showAddHeroConfig = true }
        )
        config?.SpellsByHeroes?.forEach { heroConfig ->
            HeroConfigItem(
                config = heroConfig,
                onEditSpells = { showHeroSpellsPicker = heroConfig },
                onRemove = {
                    onConfigChanged(
                        config.copy(
                            SpellsByHeroes = config.SpellsByHeroes - heroConfig
                        )
                    )
                },
                onRemoveSpell = { spell ->
                    onConfigChanged(
                        config.copy(
                            SpellsByHeroes = config.SpellsByHeroes.map {
                                it.copy(Spells = it.Spells - spell)
                            }
                        )
                    )
                }
            )
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
    onAddClick: (() -> Unit),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onAddClick) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun SpellsList(
    spells: List<SpellType>,
    onRemoveClick: (SpellType) -> Unit,
    modifier: Modifier = Modifier
) {

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        spells.forEach { spell ->
            Chip(
                label = { Text(spell.description.ifEmpty { spell.name }) },
                modifier = Modifier.padding(4.dp).clickable {
                    onRemoveClick(spell)
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove",
                        modifier = Modifier.size(16.dp).align(Alignment.CenterVertically)
                    )
                }
            )
        }
    }
}

@Composable
private fun PlayerConfigItem(
    config: StartSpellsByPlayer,
    onEditSpells: () -> Unit,
    onRemove: () -> Unit,
    onRemoveSpell: (SpellType) -> Unit,
    modifier: Modifier = Modifier
) {
    ConfigItem(
        title = config.PlayerType.description,
        content = {
            SpellsPreview(spells = config.Spells, onRemoveSpell)
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
    onRemoveSpell: (SpellType) -> Unit,
    modifier: Modifier = Modifier
) {
    ConfigItem(
        title = config.CastleType.name,
        content = {
            SpellsPreview(spells = config.Spells, onRemoveSpell)
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
    onRemoveSpell: (SpellType) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConfigItem(
        title = config.HeroType.description.ifEmpty { config.HeroType.name },
        content = {
            SpellsPreview(spells = config.Spells, onRemoveSpell)
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
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Add, contentDescription = "add")
                }
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
        }
    }
}

@Composable
private fun SpellsPreview(
    spells: List<SpellType>,
    onDelete: (SpellType) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        spells.forEach { spell ->
            Chip(
                label = { Text(spell.description.ifEmpty { spell.name }) },
                modifier = Modifier.padding(4.dp).clickable {
                    onDelete(spell)
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove",
                        modifier = Modifier.size(16.dp).align(Alignment.CenterVertically)
                    )
                }
            )
        }
    }
}

@Composable
private fun SpellPickerDialog(
    selectedSpells: List<SpellType>,
    onDismiss: () -> Unit,
    onConfirm: (List<SpellType>) -> Unit
) {
    SearchableEnumDialog(
        label = "Select Spels",
        items = SpellType.entries - selectedSpells.toSet(),
        itemTitle = { it.description.ifEmpty { it.name } },
        onDismiss = onDismiss,
        onItemSelected = {
            onConfirm(selectedSpells + it)
        }
    )
}

@Composable
private fun PlayerTypePickerDialog(
    items: List<PlayerType>,
    onDismiss: () -> Unit,
    onConfirm: (PlayerType) -> Unit
) {
    SearchableEnumDialog(
        label = "Select Player Type",
        items = items,
        itemTitle = { it.description },
        onDismiss = onDismiss,
        onItemSelected = onConfirm
    )
}

@Composable
private fun CastleTypePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (project.data.enums.CastleType) -> Unit
) {
    SearchableEnumDialog(
        label = "Select Race",
        items = project.data.enums.CastleType.values().toList(),
        itemTitle = { it.name },
        onDismiss = onDismiss,
        onItemSelected = onConfirm
    )
}

@Composable
private fun HeroTypePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (HeroType) -> Unit
) {
    SearchableEnumDialog(
        label = "Select Hero",
        items = HeroType.entries,
        itemTitle = { it.description.ifEmpty { it.name } },
        onDismiss = onDismiss,
        onItemSelected = onConfirm
    )
}