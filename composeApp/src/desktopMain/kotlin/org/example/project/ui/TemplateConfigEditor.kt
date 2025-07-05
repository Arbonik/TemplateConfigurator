package org.example.project.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.data.TemplateGenerationConfig


@Composable
fun TemplateConfigEditor(
    config: TemplateGenerationConfig,
    onConfigChanged: (TemplateGenerationConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableStateOf("General") }

    Row(modifier = modifier.fillMaxSize()) {
        // Sidebar Navigation
        SidebarNavigation(
            selectedSection = selectedSection,
            onSectionSelected = { selectedSection = it },
            modifier = Modifier.width(200.dp)
        )

        // Main Content
        Box(modifier = Modifier.weight(1f).padding(16.dp)) {
            when (selectedSection) {
                "General" -> GeneralConfigSection(
                    config = config,
                    onConfigChanged = onConfigChanged
                )

                "Zones" -> ZonesConfigSection(
                    zones = config.Zones,
                    onZonesChanged = { newZones ->
                        onConfigChanged(config.copy(Zones = newZones))
                    }
                )

                "Army" -> ArmyConfigSection(
                    baseMultiplier = config.BaseArmyMultiplier,
                    multipliers = config.ArmyMultipliers,
                    onBaseMultiplierChanged = { newValue ->
                        onConfigChanged(config.copy(BaseArmyMultiplier = newValue))
                    },
                    onMultipliersChanged = { newMultipliers ->
                        onConfigChanged(config.copy(ArmyMultipliers = newMultipliers))
                    }
                )

                "Connections" -> ConnectionsConfigSection(
                    connections = config.Connections,
                    onConnectionsChanged = { newConnections ->
                        onConfigChanged(config.copy(Connections = newConnections))
                    }
                )

                "Shops" -> ShopConfigSection(
                    shops = config.ShopBuildingConfigs,
                    onShopsChanged = { newShops ->
                        onConfigChanged(config.copy(ShopBuildingConfigs = newShops))
                    }
                )

                "Terrains" -> TerrainConfigSection(
                    terrains = config.TerrainConfig,
                    onTerrainsChanged = { newTerrains ->
                        onConfigChanged(config.copy(TerrainConfig = newTerrains))
                    }
                )

                "Scripts" -> ScriptFeaturesConfigSection(
                    scripts = config.ScriptFeaturesConfig,
                    onScriptsChanged = { newScripts ->
                        onConfigChanged(config.copy(ScriptFeaturesConfig = newScripts))
                    }
                )

                "Bans" -> BansConfigSection(
                    bans = config.EntitiesBanConfig,
                    onBansChanged = { newBans ->
                        onConfigChanged(config.copy(EntitiesBanConfig = newBans))
                    }
                )

                "CreatureBanks" -> CreatureBanksConfigSection(
                    pool = config.CreatureBanksPool,
                    onPoolChanged = { newPool ->
                        onConfigChanged(config.copy(CreatureBanksPool = newPool))
                    }
                )

                "GeneralData" -> GeneralDataConfigSection(
                    generalData = config.GeneralData,
                    onGeneralDataChanged = { newData ->
                        onConfigChanged(config.copy(GeneralData = newData))
                    }
                )

                "StartBuildings" -> StartBuildingsConfigSection(
                    buildings = config.StartBuildingConfigs,
                    onBuildingsChanged = { newBuildings ->
                        onConfigChanged(config.copy(StartBuildingConfigs = newBuildings))
                    }
                )

                "ZoneRandomization" -> ZoneRandomizationConfigSection(
                    randomization = config.ZoneRandomizationConfig,
                    onRandomizationChanged = { newRandomization ->
                        onConfigChanged(config.copy(ZoneRandomizationConfig = newRandomization))
                    }
                )
            }
        }
    }
}
