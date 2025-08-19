import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import project.Files
import project.json
import project.ui.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateConfigEditor(
    config: TemplateGenerationConfig,
    onConfigChanged: (config: TemplateGenerationConfig) -> Unit
) {
    var currentScreen by remember { mutableStateOf<NavigationItem>(NavigationItem.General) }

    Scaffold { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Фиксированное меню навигации
            NavigationPanel(
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

                    is NavigationItem.File -> {
                        FilePickerScreen(
                            openFile = {
                                Files.openFile({ text ->
                                    onConfigChanged(
                                        _root_ide_package_.project.json.decodeFromString<TemplateGenerationConfig>(
                                            text ?: ""
                                        )
                                    )
                                }, onError = { })
                            },
                            saveFile = {
                                Files.saveFile(
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
    onItemSelected: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            Text(
                text = "H5 Lobby - Template Editor",
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
//                NavigationItem.PNG,
                NavigationItem.File
            )

            navItems.forEach { item ->
                NavigationButton(
                    item = item,
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
    LaunchedEffect(Unit){
        if (config == null) {
            onConfigChanged(CreatureBanksPool())
        }
    }
    Column(modifier = modifier.padding(16.dp)) {
        Text("Creature Banks Configuration")
        Spacer(modifier = Modifier.height(16.dp))

       SwitchWithLabel(
            label = "Include Non-Player Factions",
            checked = config?.NonPlayerFactions,
            onCheckedChange = {
                onConfigChanged(config?.copy(NonPlayerFactions = it))
            }
        )

         SwitchWithLabel(
            label = "Include Player Factions",
            checked = config?.PlayerFactions,
            onCheckedChange = {
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
    checked: Boolean?,
    onCheckedChange: (Boolean?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Switch(
            checked = checked == true,
            onCheckedChange = {
                onCheckedChange(it)
            }
        )
    }
}

@Composable
private fun NavigationButton(
    item: NavigationItem,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
            Text(text = item.titleResId)
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

