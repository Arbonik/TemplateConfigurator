import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        ) {
            // Фиксированное меню навигации
            NavigationPanel(
                onItemSelected = { currentScreen = it },
                currentScreen,
                modifier = Modifier.width(230.dp)
            )

            // Основное содержание
            Box(
                modifier = Modifier
                    .fillMaxSize()
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
                        config = config.CreatureBanksPool,
                        onConfigChanged = {
                            onConfigChanged(config.copy(CreatureBanksPool = it))
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
                                        json.decodeFromString<TemplateGenerationConfig>(
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
    currentScreen: NavigationItem,
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
                    isSelected = item == currentScreen,
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
        Text("Creature Banks Configuration")
        Spacer(modifier = Modifier.height(16.dp))

        CheckboxWithLabel(
            label = "Include Non-Player Factions",
            checked = config?.NonPlayerFactions ?: false,
            onCheckedChange = {
                val base = config ?: CreatureBanksPool()
                onConfigChanged(base.copy(NonPlayerFactions = it))
            }
        )

        CheckboxWithLabel(
            label = "Include Player Factions",
            checked = config?.PlayerFactions ?: false,
            onCheckedChange = {
                val base = config ?: CreatureBanksPool()
                onConfigChanged(base.copy(PlayerFactions = it))
            }
        )

        NullableIntValueConfigEditor(
            label = "Banks Amount",
            config = config?.BanksAmount,
            onConfigChanged = { newValueConfig ->
                val base = config ?: CreatureBanksPool()
                onConfigChanged(
                    base.copy(BanksAmount = newValueConfig)
                )
            }
        )
    }
}

@Composable
private fun NavigationButton(
    item: NavigationItem,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        border = BorderStroke(if (isSelected) 3.dp else 0.dp, if (isSelected) Color.Blue else Color.Gray),
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

