import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.example.project.json
import org.example.project.openFile
import org.example.project.saveFile
import org.example.project.ui.*


@Serializable
data class TemplateGenerationConfig(
    val TemplateName: String,
    val Zones: List<ZoneGenerationConfig>,
    val Connections: List<ConnectionModel>,
    val TerrainConfigs: List<TerrainConfig> = listOf(),
    val StartBuildingConfigs: List<StartBuildingConfig> = listOf(),
    val GeneralData: GeneralData? = null,
    val BaseArmyMultiplier: Double? = null,
    val multipliers: Map<String, Double> = mapOf(),
    val ScriptFeaturesConfig: ScriptFeaturesConfig,
    val EntitiesBanConfig: EntitiesBanModel = EntitiesBanModel(),
    val StartSpellsConfig: StartSpellsConfig? = null,
    val CustomBuildingConfigs: List<CustomBuildingConfig>,
    val CreatureBanksPool: CreatureBanksPool? = null,
    val ZoneRandomizationConfig: ZoneRandomizationConfig? = null
)

// Модель для пунктов навигации
sealed class NavigationItem(
    val route: String,
    val titleResId: String, // В реальном приложении это будет ID строкового ресурса
    val icon: ImageVector
) {
    object File : NavigationItem("file", "File", Icons.Default.FilePresent)
    object General : NavigationItem("general", "General", Icons.Default.Settings)
    object Zones : NavigationItem("zones", "Zones", Icons.Default.Map)
    object Connections : NavigationItem("connections", "Connections", Icons.Default.Link)
    object Terrain : NavigationItem("terrain", "Terrain", Icons.Default.Terrain)
    object StartBuildings : NavigationItem("start_buildings", "Start Buildings", Icons.Default.Home)
    object Army : NavigationItem("army", "Army", Icons.Default.Security)
    object ScriptFeatures : NavigationItem("script_features", "Script Features", Icons.Default.Code)
    object BannedEntities : NavigationItem("banned_entities", "Banned Entities", Icons.Default.Block)
    object StartSpells : NavigationItem("start_spells", "Start Spells", Icons.Default.AutoAwesome)
    object CustomBuildings : NavigationItem("custom_buildings", "Custom Buildings", Icons.Default.Build)
    object CreatureBanks : NavigationItem("creature_banks", "Creature Banks", Icons.Default.AccountTree)
    object ZoneRandomization : NavigationItem("zone_randomization", "Zone Randomization", Icons.Default.Shuffle)
    object PNG : NavigationItem("png", "Graph Scheme", Icons.Default.AttachFile)
}

@Composable
fun TemplateConfigEditor(
    config: TemplateGenerationConfig,
    onConfigChanged: (config: TemplateGenerationConfig) -> Unit
) {
    var currentScreen by remember { mutableStateOf<NavigationItem>(NavigationItem.General) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(config.TemplateName) },
                actions = {
                    LanguageSwitcher()
                }
            )
        }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Фиксированное меню навигации
            NavigationPanel(
                currentScreen = currentScreen,
                onItemSelected = { currentScreen = it },
                modifier = Modifier.width(250.dp)
            )

            // Основное содержание
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                when (currentScreen) {
                    is NavigationItem.General -> GeneralDataEditor(
                        config.GeneralData,
                        onSave = { onConfigChanged(config.copy(GeneralData = it)) },
                    )

                    is NavigationItem.Zones -> ZoneGenerationConfigEditor(
                        config.Zones,
                        onZonesUpdated = { it ->
                            onConfigChanged(config.copy(Zones = it))
                        }
                    )

                    is NavigationItem.Connections -> ConnectionsSection(
                        config.Connections,
                        config.Zones,
                        onConnectionsUpdated = { it ->
                            onConfigChanged(config.copy(Connections = it))
                        }
                    )

                    is NavigationItem.Terrain -> TerrainConfigSection(
                        terrains = config.TerrainConfigs,
                        onTerrainsChanged = {
                            onConfigChanged(config.copy(TerrainConfigs = it))
                        }
                    )

                    is NavigationItem.StartBuildings -> StartBuildingConfigEditor(
                        configs = config.StartBuildingConfigs,
                        onConfigsUpdated = {
                            onConfigChanged(config.copy(StartBuildingConfigs = it))
                        }
                    )

                    is NavigationItem.Army -> ArmySection(
                        baseMultiplier = config.BaseArmyMultiplier,
                        multipliers = config.multipliers,
                        onChangeBaseMultiplier = {
                            onConfigChanged(config.copy(BaseArmyMultiplier = it))
                        },
                        onChangeMultiplier = {
                            onConfigChanged(config.copy(multipliers = it))
                        }
                    )

                    is NavigationItem.ScriptFeatures -> ScriptFeaturesConfigEditor(
                        config = config.ScriptFeaturesConfig,
                        onConfigChanged = {
                            onConfigChanged(config.copy(ScriptFeaturesConfig = it))
                        }
                    )

                    is NavigationItem.BannedEntities -> BansConfigSection(
                        config.EntitiesBanConfig,
                        onBansChanged = { it -> onConfigChanged(config.copy(EntitiesBanConfig = it)) }
                    )

                    is NavigationItem.StartSpells -> StartSpellsConfigEditor(
                        config = config.StartSpellsConfig,
                        onConfigChanged = {
                            onConfigChanged(config.copy(StartSpellsConfig = it))
                        }
                    )
                    is NavigationItem.CustomBuildings -> BuildingConfigEditor(
                        buildingConfigs = config.CustomBuildingConfigs,
                        onConfigsUpdated = { it ->
                            onConfigChanged(config.copy(CustomBuildingConfigs = it))
                        }
                    )
                    is NavigationItem.CreatureBanks -> CreatureBankConfigEditor(
                       config =  config.CreatureBanksPool,
                        onConfigChanged = {
                            config.copy(CreatureBanksPool = it)
                        }
                    )
                    is NavigationItem.ZoneRandomization -> ZoneRandomizationConfigEditor(
                        config.ZoneRandomizationConfig,
                        onConfigChanged = {
                            onConfigChanged(config.copy(ZoneRandomizationConfig = it))
                        },
                        availableZoneIds = config.Zones.map { it.ZoneId }
                    )
                    is NavigationItem.PNG -> ZonesGraphInteractiveScreen(
                        config.Zones,
                        connections = config.Connections
                    )

                    is NavigationItem.File -> {
                        FilePickerScreen(
                            openFile = {
                                openFile({ text ->
                                    onConfigChanged(json.decodeFromString<TemplateGenerationConfig>(text ?: ""))
                                }, onError = { })
                            },
                            saveFile = {
                                saveFile(
                                    json.encodeToString<TemplateGenerationConfig>(config)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationPanel(
    currentScreen: NavigationItem,
    onItemSelected: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            Text(
                text = "Configuration",
                modifier = Modifier.padding(16.dp)
            )

            Divider()

            val navItems = listOf(
                NavigationItem.General,
                NavigationItem.Zones,
                NavigationItem.Connections,
                NavigationItem.Terrain,
                NavigationItem.StartBuildings,
                NavigationItem.Army,
                NavigationItem.ScriptFeatures,
                NavigationItem.BannedEntities,
                NavigationItem.StartSpells,
                NavigationItem.CustomBuildings,
                NavigationItem.CreatureBanks,
                NavigationItem.ZoneRandomization,
                NavigationItem.PNG,
                NavigationItem.File
            )

            navItems.forEach { item ->
                NavigationButton(
                    item = item,
                    isSelected = currentScreen == item,
                    onClick = { onItemSelected(item) }
                )
            }
        }
    }
}

@Composable
fun CreatureBankConfigEditor(
    config: CreatureBanksPool?,
    onConfigChanged: (CreatureBanksPool?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("Creature Banks Configuration", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        // NonPlayerFactions toggle
        var nonPlayerFactions by remember { mutableStateOf(config?.NonPlayerFactions ?: false) }
        SwitchWithLabel(
            label = "Include Non-Player Factions",
            checked = nonPlayerFactions,
            onCheckedChange = {
                nonPlayerFactions = it
                onConfigChanged(config?.copy(NonPlayerFactions = it))
            }
        )

        // PlayerFactions toggle
        var playerFactions by remember { mutableStateOf(config?.PlayerFactions ?: false) }
        SwitchWithLabel(
            label = "Include Player Factions",
            checked = playerFactions,
            onCheckedChange = {
                playerFactions = it
                onConfigChanged(config?.copy(PlayerFactions = it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        NullableIntValueConfigEditor(
            label = "Banks Amount",
            config = config?.BanksAmount,
            onConfigChanged = { newValueConfig ->
                onConfigChanged(config?.copy(BanksAmount = newValueConfig))
            },
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun SwitchWithLabel(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun NavigationButton(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.12f) else MaterialTheme.colors.surface
    val contentColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.titleResId,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
private fun LanguageSwitcher() {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("English", "Русский")
    var selectedLanguage by remember { mutableStateOf(languages[0]) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.Language, contentDescription = "Switch language")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(onClick = {
                    selectedLanguage = language
                    expanded = false
                    // Здесь будет логика смены локализации
                }) {
                    Text(language)
                }
            }
        }
    }
}

@Composable
private fun ConnectionsSection(
    connections: List<ConnectionModel>,
    zones: List<ZoneGenerationConfig>,
    onConnectionsUpdated: (List<ConnectionModel>) -> Unit,
) {
    ConnectionModelEditor(
        connections = connections,
        zones = zones,
        onConnectionaUpdated = onConnectionsUpdated
    )
}

