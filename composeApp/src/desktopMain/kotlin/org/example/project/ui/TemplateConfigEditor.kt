import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.example.project.ui.ConnectionModelEditor
import org.example.project.ui.TerrainConfigSection

@Serializable
data class TemplateGenerationConfig(
    val TemplateName: String,
        val Zones: List<ZoneGenerationConfig>,
        val Connections: List<ConnectionModel>,
    val TerrainConfigs: List<TerrainConfig>,
    val StartBuildingConfigs: List<StartBuildingConfig>,
    val GeneralData: GeneralData,
    val BaseArmyMultiplier: Double? = null,
    val ArmyMultipliers: Map<String, Double>,
    val ScriptFeaturesConfig: ScriptFeaturesConfig,
    val EntitiesBanConfig: EntitiesBanModel,
    val StartSpellsConfig: StartSpellsConfig,
    val CustomBuildingConfigs: List<CustomBuildingConfig>,
    val CreatureBanksPool: CreatureBanksPool,
    val ZoneRandomizationConfig: ZoneRandomizationConfig
)


@Serializable
data class StartBuildingConfig(val id: String)

@Serializable
data class GeneralData(val id: String)

@Serializable
data class ScriptFeaturesConfig(val id: String)

@Serializable
data class EntitiesBanModel(val id: String)

@Serializable
data class StartSpellsConfig(val id: String)

@Serializable
data class CustomBuildingConfig(val id: String)

@Serializable
data class CreatureBanksPool(val id: String)

@Serializable
data class ZoneRandomizationConfig(val id: String)

// Модель для пунктов навигации
sealed class NavigationItem(
    val route: String,
    val titleResId: String, // В реальном приложении это будет ID строкового ресурса
    val icon: ImageVector
) {
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
                    is NavigationItem.General -> GeneralConfigSection(config.GeneralData)
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
                    is NavigationItem.StartBuildings -> StartBuildingsSection(config.StartBuildingConfigs)
                    is NavigationItem.Army -> ArmySection(config.BaseArmyMultiplier, config.ArmyMultipliers)
                    is NavigationItem.ScriptFeatures -> ScriptFeaturesSection(config.ScriptFeaturesConfig)
                    is NavigationItem.BannedEntities -> BannedEntitiesSection(config.EntitiesBanConfig)
                    is NavigationItem.StartSpells -> StartSpellsSection(config.StartSpellsConfig)
                    is NavigationItem.CustomBuildings -> CustomBuildingsSection(config.CustomBuildingConfigs)
                    is NavigationItem.CreatureBanks -> CreatureBanksSection(config.CreatureBanksPool)
                    is NavigationItem.ZoneRandomization -> ZoneRandomizationSection(config.ZoneRandomizationConfig)
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
                .scrollable(rememberScrollState(0), Orientation.Vertical)
                .padding(8.dp)
        ) {
            Text(
                text = "Configuration",
                style = MaterialTheme.typography.h6,
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
                NavigationItem.ZoneRandomization
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

// Заглушки для секций конфигурации
@Composable
private fun GeneralConfigSection(data: GeneralData) {
    Text("General Configuration Section")
    // Реализация формы для GeneralData
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

@Composable
private fun StartBuildingsSection(buildings: List<StartBuildingConfig>) {
    Text("Start Buildings Configuration Section")
    // Реализация формы для StartBuildingConfigs
}

@Composable
private fun ArmySection(baseMultiplier: Double?, multipliers: Map<String, Double>) {
    Text("Army Multipliers Configuration Section")
    // Реализация формы для армии
}

@Composable
private fun ScriptFeaturesSection(config: ScriptFeaturesConfig) {
    Text("Script Features Configuration Section")
    // Реализация формы для ScriptFeaturesConfig
}

@Composable
private fun BannedEntitiesSection(config: EntitiesBanModel) {
    Text("Banned Entities Configuration Section")
    // Реализация формы для EntitiesBanModel
}

@Composable
private fun StartSpellsSection(config: StartSpellsConfig) {
    Text("Start Spells Configuration Section")
    // Реализация формы для StartSpellsConfig
}

@Composable
private fun CustomBuildingsSection(configs: List<CustomBuildingConfig>) {
    Text("Custom Buildings Configuration Section")
    // Реализация формы для CustomBuildingConfigs
}

@Composable
private fun CreatureBanksSection(pool: CreatureBanksPool) {
    Text("Creature Banks Pool Configuration Section")
    // Реализация формы для CreatureBanksPool
}

@Composable
private fun ZoneRandomizationSection(config: ZoneRandomizationConfig) {
    Text("Zone Randomization Configuration Section")
    // Реализация формы для ZoneRandomizationConfig
}