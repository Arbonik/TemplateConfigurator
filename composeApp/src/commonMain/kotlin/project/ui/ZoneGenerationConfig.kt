import BasicZoneConfigEditor
import BuildingTextureConfig
import CheckboxWithLabel
import CreatureConfigBooleanField
import CreatureConfigDoubleField
import CreatureModifier
import CreatureModifierEditor
import CreatureSelectionDialog
import CreatureTierReplacement
import CreatureTierReplacementEditor
import CreaturesConfigEditor
import CreaturesConfiguration
import DependantDwellingConfig
import DwellingByPointsConfig
import DwellingByPointsConfigScreen
import DwellingGenerationConfig
import DwellingGenerationEditor
import DwellingValue
import EnumDropdown
import GenerationTypeEditor
import IntValueConfig
import IntValueConfigWidget
import MineGenerationEditor
import NullableIntValueConfigEditor
import NullableNumberInputField
import PositioningEditor
import RandomDwellingConfig
import RandomDwellingConfigEditor
import ResourcesConfig
import SectionHeader
import StaticDwellingConfigs
import StaticDwellingConfigsEditor
import TerrainType
import ZoneEditor
import ZoneFeaturesEditor
import ZoneGenerationConfig
import ZoneList
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import getNextIndexItem
import project.data.enums.Creature
import project.ui.MyListItem
import project.ui.dwellingGenerationConfig.DependantDwellingConfigEditor
import project.ui.dwellingGenerationConfig.DwellingValueEditor

@Composable
fun ZoneGenerationConfigEditor(
    zones: List<ZoneGenerationConfig>,
    onZonesUpdated: (List<ZoneGenerationConfig>) -> Unit
) {
    var selectedZoneIndex by remember {
        mutableStateOf(if (zones.isEmpty()) null else 0)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        ) {
            Button(
                onClick = {
                    val newZone = ZoneGenerationConfig(
                        ZoneId = (zones.maxOfOrNull { it.ZoneId } ?: 0) + 1,
                        TerrainType = TerrainType.FirstPlayer
                    )
                    onZonesUpdated(zones + newZone)
                    selectedZoneIndex = zones.lastIndex
                },
                enabled = true
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Добавить зону")
            }

            selectedZoneIndex?.let { it1 ->
                ZoneList(
                    zones = zones,
                    selectedIndex = it1,
                    onZoneSelected = { index -> selectedZoneIndex = index },
                    deleteZone = {
                        selectedZoneIndex = if (zones.size == 1) null else 0
                        onZonesUpdated(
                            zones - it
                        )
                    },
                    modifier = Modifier.width(200.dp)
                )
            }
        }

        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        selectedZoneIndex?.let {
            ZoneEditor(
                config = zones[it],
                onConfigChanged = { updatedZone ->
                    onZonesUpdated(
                        zones.map { zone ->
                            if (zone.ZoneId == updatedZone.ZoneId) updatedZone else zone
                        }.toList()
                    )
                },
                modifier = Modifier.weight(1f)
            )
        } ?: Text("Нет зон для отображения")
    }
}

@Composable
private fun ZoneList(
    zones: List<ZoneGenerationConfig>,
    selectedIndex: Int,
    onZoneSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    deleteZone: (ZoneGenerationConfig) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(zones) { zone ->
            _root_ide_package_.project.ui.MyListItem(
                item = zone,
                isSelected = zones.indexOf(zone) == selectedIndex,
                onClick = { onZoneSelected(zones.indexOf(zone)) },
                deleteZone = deleteZone,
                content = {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Zone ${it.ZoneId}",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = it.TerrainType.toString(),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun ZoneEditor(
    config: ZoneGenerationConfig,
    onConfigChanged: (ZoneGenerationConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedSections by remember { mutableStateOf(setOf<String>()) }

    LazyColumn(modifier = modifier.padding(16.dp)) {
        item {
            SectionHeader(
                title = "Basic Zone Configuration",
                expanded = expandedSections.contains("basic"),
                onToggle = {
                    expandedSections = if (expandedSections.contains("basic")) {
                        expandedSections - "basic"
                    } else {
                        expandedSections + "basic"
                    }
                }
            )

            if (expandedSections.contains("basic")) {
                BasicZoneConfigEditor(config, onConfigChanged)
            }
        }

        item {
            SectionHeader(
                title = "Dwelling Generation",
                expanded = expandedSections.contains("dwelling"),
                onToggle = {
                    expandedSections = if (expandedSections.contains("dwelling")) {
                        expandedSections - "dwelling"
                    } else {
                        expandedSections + "dwelling"
                    }
                }
            )

            if (expandedSections.contains("dwelling")) {
                DwellingGenerationEditor(config, onConfigChanged)
            }
        }

        item {
            SectionHeader(
                title = "Mine Generation",
                expanded = expandedSections.contains("mine"),
                onToggle = {
                    expandedSections = if (expandedSections.contains("mine")) {
                        expandedSections - "mine"
                    } else {
                        expandedSections + "mine"
                    }
                }
            )

            if (expandedSections.contains("mine")) {
                MineGenerationEditor(config, onConfigChanged)
            }
        }

        item {
            SectionHeader(
                title = "Zone Features",
                expanded = expandedSections.contains("features"),
                onToggle = {
                    expandedSections = if (expandedSections.contains("features")) {
                        expandedSections - "features"
                    } else {
                        expandedSections + "features"
                    }
                }
            )

            if (expandedSections.contains("features")) {
                ZoneFeaturesEditor(config, onConfigChanged)
            }
        }

        item {
            SectionHeader(
                title = "Positioning",
                expanded = expandedSections.contains("positioning"),
                onToggle = {
                    expandedSections = if (expandedSections.contains("positioning")) {
                        expandedSections - "positioning"
                    } else {
                        expandedSections + "positioning"
                    }
                }
            )

            if (expandedSections.contains("positioning")) {
                PositioningEditor(config, onConfigChanged)
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = onToggle) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium
        )
    }
    Divider()
}

@Composable
private fun BasicZoneConfigEditor(
    config: ZoneGenerationConfig,
    onConfigChanged: (ZoneGenerationConfig) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        // Terrain Type
        EnumDropdown(
            label = "Terrain Type",
            options = TerrainType.values().toList(),
            selectedOption = config.TerrainType,
            onOptionSelected = { newTerrain ->
                onConfigChanged(config.copy(TerrainType = newTerrain))
            },
            optionToString = { it.toString() }
        )

        // Mirror Zone ID
        NullableNumberInputField(
            label = "Mirror Zone ID",
            value = config.MirrorZoneId,
            onValueChange = { newValue ->
                onConfigChanged(config.copy(MirrorZoneId = newValue))
            }
        )

        // Town flag
        CheckboxWithLabel(
            label = "Has Town",
            checked = config.Town ?: false,
            onCheckedChange = { checked ->
                onConfigChanged(config.copy(Town = checked))
            }
        )
    }
}

@Composable
private fun DwellingGenerationEditor(
    config: ZoneGenerationConfig,
    onConfigChanged: (ZoneGenerationConfig) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        if (config.DwellingGenerationConfig == null) {
            Button(
                onClick = {
                    val newDwellingConfig = DwellingGenerationConfig(
                        BuildingTexture = BuildingTextureConfig.DefaultDwellingByTerrain,
                        CreaturesConfiguration = CreaturesConfiguration(
                            ReplacementsCount = IntValueConfig(0),
                            TerrainFaction = null,
                            NonPlayersFactions = null,
                            NoGrades = null,
                            Grades = null,
                            Neutrals = null,
                            BaseCostMultiplier = null,
                            BaseResourcesMultiplier = null,
                            BaseGrowMultiplier = null,
                            CreatureModifiers = emptyList(),
                            CreatureTierReplacements = emptyList(),
                            NonUniqueReplacements = null
                        ),
                    )
                    onConfigChanged(config.copy(DwellingGenerationConfig = newDwellingConfig))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Add Dwelling Generation Config")
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dwelling Generation Config", style = MaterialTheme.typography.labelMedium)
                IconButton(
                    onClick = {
                        onConfigChanged(config.copy(DwellingGenerationConfig = null))
                    }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }

            // Building Texture
            EnumDropdown(
                label = "Building Texture",
                options = BuildingTextureConfig.values().toList(),
                selectedOption = config.DwellingGenerationConfig.BuildingTexture
                    ?: BuildingTextureConfig.DefaultDwellingByTerrain,
                onOptionSelected = { newTexture ->
                    onConfigChanged(
                        config.copy(
                            DwellingGenerationConfig =
                                config.DwellingGenerationConfig.copy(BuildingTexture = newTexture)
                        )
                    )
                },
                optionToString = { it.description }
            )

            // Creatures Configuration
            CreaturesConfigEditor(
                config = config.DwellingGenerationConfig.CreaturesConfiguration ?: CreaturesConfiguration(),
                onConfigChanged = { newCreaturesConfig ->
                    onConfigChanged(
                        config.copy(
                            DwellingGenerationConfig =
                                config.DwellingGenerationConfig.copy(CreaturesConfiguration = newCreaturesConfig)
                        )
                    )
                }
            )

            // Generation Type wed
            GenerationTypeEditor(
                zoneId = config.ZoneId,
                config = config.DwellingGenerationConfig,
                onTypeChanged = { random, static, byPoints, dependant ->
                    onConfigChanged(
                        config.copy(
                            DwellingGenerationConfig = config.DwellingGenerationConfig.copy(
                                RandomDwellingConfig = random,
                                StaticDwellingConfigs = static,
                                DwellingByPointsConfig = byPoints,
                                DependantDwellingConfig = dependant
                            )
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun GenerationTypeEditor(
    zoneId: Int,
    config: DwellingGenerationConfig?,
    onTypeChanged: (RandomDwellingConfig?, StaticDwellingConfigs?, DwellingByPointsConfig?, DependantDwellingConfig?) -> Unit
) {

    var selectedType by remember(config) {
        mutableStateOf(
            when {
                config?.RandomDwellingConfig != null -> "Random"
                config?.StaticDwellingConfigs != null -> "Static"
                config?.DwellingByPointsConfig != null -> "By Points"
                config?.DependantDwellingConfig != null -> "Dependant"
                else -> null
            }
        )
    }

    var StaticDwellingConfigs by remember(zoneId) {
        mutableStateOf(
            config?.StaticDwellingConfigs ?: StaticDwellingConfigs(
                DwellingValue = emptyList()
            )
        )
    }

    var DependantDwellingConfig by remember(zoneId) {
        mutableStateOf(
            config?.DependantDwellingConfig ?: DependantDwellingConfig(
                ZoneId = 0,
                MinCount = 1,
                MaxCount = 1,
                IsCopyMode = false
            )
        )
    }

    var DwellingByPointsConfig by remember(zoneId) {
        mutableStateOf(
            config?.DwellingByPointsConfig ?: DwellingByPointsConfig(
                PointsCount = 0,
                DwellingPoints = DwellingValue(),
                DwellingPointsByFaction = emptyMap(),
                AllowedTiers = emptyList(),
                MinCountPerTier = DwellingValue(),
                MinCountPerTierByFaction = emptyMap(),
                MaxCountPerTier = DwellingValue(),
                MaxCountPerTierByFaction = emptyMap()
            )
        )
    }

    var RandomDwellingConfig by remember(zoneId) {
        mutableStateOf(
            config?.RandomDwellingConfig ?: RandomDwellingConfig(
                MinCount = 1,
                MaxCount = 1,
                AllowedTiers = emptyList()
            )
        )
    }

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Text("Generation Type", style = MaterialTheme.typography.labelMedium)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("Random", "Static", "By Points", "Dependant").forEach { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = {
                        selectedType = type
                        when (selectedType) {
                            "Random" -> onTypeChanged(RandomDwellingConfig, null, null, null)
                            "Static" -> onTypeChanged(null, StaticDwellingConfigs, null, null)
                            "By Points" -> onTypeChanged(null, null, DwellingByPointsConfig, null)
                            "Dependant" -> onTypeChanged(null, null, null, DependantDwellingConfig)
                        }
                    },
                    modifier = Modifier.padding(4.dp),
                    label = {
                        Text(type)
                    }
                )
            }
        }
    }

    when (selectedType) {
        "Random" -> RandomDwellingConfigEditor(
            zoneId = zoneId,
            config = config?.RandomDwellingConfig ?: RandomDwellingConfig,
            onConfigChanged = {
                RandomDwellingConfig = it
                onTypeChanged(RandomDwellingConfig, null, null, null)
            }
        )

        "Static" -> StaticDwellingConfigsEditor(
            configs = config?.StaticDwellingConfigs ?: StaticDwellingConfigs,
            onConfigsUpdated = {
                StaticDwellingConfigs = it
                onTypeChanged(null, StaticDwellingConfigs, null, null)
            }
        )

        "By Points" -> DwellingByPointsConfigScreen(
            config = config?.DwellingByPointsConfig ?: DwellingByPointsConfig,
            onConfigChanged = {
                DwellingByPointsConfig = it
                onTypeChanged(null, null, it, null)
            }
        )

        "Dependant" -> DependantDwellingConfigEditor(
            config = config?.DependantDwellingConfig ?: DependantDwellingConfig,
            onConfigUpdated = {
                DependantDwellingConfig = it
                onTypeChanged(null, null, null, it)
            }
        )

        else -> {}
    }
}

@Composable
private fun MineGenerationEditor(
    config: ZoneGenerationConfig,
    onConfigChanged: (ZoneGenerationConfig) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        if (config.MineGenerationConfig == null) {
            Button(
                onClick = {
                    val newMineConfig = ResourcesConfig(
                        Wood = IntValueConfig(0),
                        Ore = IntValueConfig(0),
                        Mercury = IntValueConfig(0),
                        Crystals = IntValueConfig(0),
                        Sulfur = IntValueConfig(0),
                        Gems = IntValueConfig(0),
                        Gold = IntValueConfig(0)
                    )
                    onConfigChanged(config.copy(MineGenerationConfig = newMineConfig))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Add Mine Generation Config")
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mine Generation Config", style = MaterialTheme.typography.labelMedium)
                IconButton(
                    onClick = {
                        onConfigChanged(config.copy(MineGenerationConfig = null))
                    }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }

            // Wood
            IntValueConfigWidget(
                label = "Wood",
                config = config.MineGenerationConfig.Wood,
                onConfigChanged = { newConfig ->
                    onConfigChanged(
                        config.copy(
                            MineGenerationConfig =
                                config.MineGenerationConfig.copy(Wood = newConfig)
                        )
                    )
                }
            )

            // Ore
            IntValueConfigWidget(
                label = "Ore",
                config = config.MineGenerationConfig.Ore,
                onConfigChanged = { newConfig ->
                    onConfigChanged(
                        config.copy(
                            MineGenerationConfig =
                                config.MineGenerationConfig.copy(Ore = newConfig)
                        )
                    )
                }
            )

            // Mercury
            IntValueConfigWidget(
                label = "Mercury",
                config = config.MineGenerationConfig.Mercury,
                onConfigChanged = { newConfig ->
                    onConfigChanged(
                        config.copy(
                            MineGenerationConfig =
                                config.MineGenerationConfig.copy(Mercury = newConfig)
                        )
                    )
                }
            )

            // Crystals
            IntValueConfigWidget(
                label = "Crystals",
                config = config.MineGenerationConfig.Crystals,
                onConfigChanged = { newConfig ->
                    onConfigChanged(
                        config.copy(
                            MineGenerationConfig =
                                config.MineGenerationConfig.copy(Crystals = newConfig)
                        )
                    )
                }
            )

            // Sulfur
            IntValueConfigWidget(
                label = "Sulfur",
                config = config.MineGenerationConfig.Sulfur,
                onConfigChanged = { newConfig ->
                    onConfigChanged(
                        config.copy(
                            MineGenerationConfig =
                                config.MineGenerationConfig.copy(Sulfur = newConfig)
                        )
                    )
                }
            )

            // Gems
            IntValueConfigWidget(
                label = "Gems",
                config = config.MineGenerationConfig.Gems,
                onConfigChanged = { newConfig ->
                    onConfigChanged(
                        config.copy(
                            MineGenerationConfig =
                                config.MineGenerationConfig.copy(Gems = newConfig)
                        )
                    )
                }
            )

            // Gold
            IntValueConfigWidget(
                label = "Gold",
                config = config.MineGenerationConfig.Gold,
                onConfigChanged = { newConfig ->
                    onConfigChanged(
                        config.copy(
                            MineGenerationConfig =
                                config.MineGenerationConfig.copy(Gold = newConfig)
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun ZoneFeaturesEditor(
    config: ZoneGenerationConfig,
    onConfigChanged: (ZoneGenerationConfig) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        // Abandoned Mines
        NullableIntValueConfigEditor(
            label = "Abandoned Mines",
            config = config.AbandonedMines,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(AbandonedMines = newConfig))
            }
        )

        // UpgBuildingsDensity
        NullableIntValueConfigEditor(
            label = "UpgBuildingsDensity",
            config = config.UpgBuildingsDensity,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(UpgBuildingsDensity = newConfig))
            }
        )

        // Treasure Density
        NullableIntValueConfigEditor(
            label = "Treasure Density",
            config = config.TreasureDensity,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(TreasureDensity = newConfig))
            }
        )
        // TreasureChestDensity
        NullableIntValueConfigEditor(
            label = "TreasureChestDensity",
            config = config.TreasureChestDensity,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(TreasureChestDensity = newConfig))
            }
        )


        // Prisons
        NullableIntValueConfigEditor(
            label = "Prisons",
            config = config.Prisons,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(Prisons = newConfig))
            }
        )

        // Town Guard Strength
        NullableIntValueConfigEditor(
            label = "Town Guard Strength",
            config = config.TownGuardStrenght,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(TownGuardStrenght = newConfig))
            }
        )

        // ShopPoints
        NullableIntValueConfigEditor(
            label = "ShopPoints",
            config = config.ShopPoints,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(ShopPoints = newConfig))
            }
        )
        // ShrinePoints
        NullableIntValueConfigEditor(
            label = "ShrinePoints",
            config = config.ShrinePoints,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(ShrinePoints = newConfig))
            }
        )
        // LuckMoralBuildingsDensity
        NullableIntValueConfigEditor(
            label = "LuckMoralBuildingsDensity",
            config = config.LuckMoralBuildingsDensity,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(LuckMoralBuildingsDensity = newConfig))
            }
        )
        // ResourceBuildingsDensity
        NullableIntValueConfigEditor(
            label = "ResourceBuildingsDensity",
            config = config.ResourceBuildingsDensity,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(ResourceBuildingsDensity = newConfig))
            }
        )
        // TreasureBuildingPoints
        NullableIntValueConfigEditor(
            label = "TreasureBuildingPoints",
            config = config.TreasureBuildingPoints,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(TreasureBuildingPoints = newConfig))
            }
        )
        // TreasureBlocksTotalValue
        NullableIntValueConfigEditor(
            label = "TreasureBlocksTotalValue",
            config = config.TreasureBlocksTotalValue,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(TreasureBlocksTotalValue = newConfig))
            }
        )
        // DenOfThieves
        NullableIntValueConfigEditor(
            label = "DenOfThieves",
            config = config.DenOfThieves,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(DenOfThieves = newConfig))
            }
        )
        // RedwoodObservatoryDensity
        NullableIntValueConfigEditor(
            label = "RedwoodObservatoryDensity",
            config = config.RedwoodObservatoryDensity,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(RedwoodObservatoryDensity = newConfig))
            }
        )
        // Size
        NullableIntValueConfigEditor(
            label = "Size",
            config = config.Size,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(Size = newConfig))
            }
        )
        // DistBetweenTreasureBlocks
        NullableIntValueConfigEditor(
            label = "DistBetweenTreasureBlocks",
            config = config.DistBetweenTreasureBlocks,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(DistBetweenTreasureBlocks = newConfig))
            }
        )


        // Treasure Blocks Scaling
        CheckboxWithLabel(
            label = "Treasure Blocks Scale from Town Distance",
            checked = config.TreasureBlocksScalingFromTownDist ?: false,
            onCheckedChange = { checked ->
                onConfigChanged(config.copy(TreasureBlocksScalingFromTownDist = checked))
            },
        )
    }
}

@Composable
private fun PositioningEditor(
    config: ZoneGenerationConfig,
    onConfigChanged: (ZoneGenerationConfig) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        // Zone Start Point X
        NullableIntValueConfigEditor(
            label = "Zone Start X",
            config = config.ZoneStartPointX,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(ZoneStartPointX = newConfig))
            }
        )

        // Zone Start Point Y
        NullableIntValueConfigEditor(
            label = "Zone Start Y",
            config = config.ZoneStartPointY,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(ZoneStartPointY = newConfig))
            }
        )

        // Main Town Start Point X
        NullableIntValueConfigEditor(
            label = "Main Town X",
            config = config.MainTownStartPointX,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(MainTownStartPointX = newConfig))
            }
        )

        // Main Town Start Point Y
        NullableIntValueConfigEditor(
            label = "Main Town Y",
            config = config.MainTownStartPointY,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(MainTownStartPointY = newConfig))
            }
        )

        // Main Town Rotation Direction
        NullableIntValueConfigEditor(
            label = "Main Town Rotation",
            config = config.MainTownRotationDirection,
            onConfigChanged = { newConfig ->
                onConfigChanged(config.copy(MainTownRotationDirection = newConfig))
            }
        )
    }
}

// Helper components

@Composable
private fun NumberInputField(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember { mutableStateOf(value.toString()) }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            it.toIntOrNull()?.let { intValue -> onValueChange(intValue) }
        },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun NullableNumberInputField(
    label: String,
    value: Int?,
    onValueChange: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isNull by remember { mutableStateOf(value == null) }
    var textValue by remember { mutableStateOf(value?.toString() ?: "") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = !isNull,
            onCheckedChange = {
                isNull = !it
                onValueChange(null)
                if (!isNull && textValue.isEmpty()) {
                    textValue = "0"
                    onValueChange(0)
                }
            }
        )

        OutlinedTextField(
            value = if (isNull) "" else textValue,
            onValueChange = {
                textValue = it
                it.toIntOrNull()?.let { intValue -> onValueChange(intValue) }
            },
            label = { Text(label) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !isNull
        )
    }
}

@Composable
fun NullableNumberInputField(
    label: String,
    value: Double?,
    onValueChange: (Double?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isNull by remember { mutableStateOf(value == null) }
    var textValue by remember { mutableStateOf(value?.toString() ?: "") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = !isNull,
            onCheckedChange = {
                isNull = !it
                onValueChange(null)
                if (!isNull && textValue.isEmpty()) {
                    textValue = "0"
                    onValueChange(0.0)
                }
            }
        )

        OutlinedTextField(
            value = if (isNull) "" else textValue,
            onValueChange = {
                textValue = it
                it.toDoubleOrNull()?.let { intValue -> onValueChange(intValue) }
            },
            label = { Text(label) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !isNull
        )
    }
}

@Composable
private fun <T : Enum<T>> EnumDropdown(
    label: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionToString: (T) -> String = { it.toString() },
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = optionToString(selectedOption),
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = {
                        Text(optionToString(option))

                    }
                )
            }
        }

        // This invisible button covers the text field and handles clicks
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable { expanded = true }
        )
    }
}

@Composable
private fun CheckboxWithLabel(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(text = label, modifier = Modifier.weight(1f))
    }
}

@Composable
fun NullableIntValueConfigEditor(
    label: String,
    config: IntValueConfig?,
    onConfigChanged: (IntValueConfig?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isNull by remember { mutableStateOf(config == null) }

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Checkbox(
                checked = !isNull,
                onCheckedChange = {
                    isNull = !it
                    if (!isNull && config == null) {
                        onConfigChanged(IntValueConfig(0))
                    } else {
                        onConfigChanged(null)
                    }
                }
            )
        }

        if (!isNull && config != null) {
            IntValueConfigWidget(
                label = "",
                config = config,
                onConfigChanged = onConfigChanged
            )
        }
    }
}

@Composable
private fun IntValueConfigEditor(
    config: IntValueConfig,
    label: String,
    onConfigUpdated: (IntValueConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentConfig by remember { mutableStateOf(config) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = currentConfig.MinValue?.toString() ?: "",
                onValueChange = {
                    val newValue = it.toIntOrNull()
                    currentConfig = currentConfig.copy(MinValue = newValue)
                    onConfigUpdated(currentConfig)
                },
                label = { Text("Min") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = currentConfig.MaxValue?.toString() ?: "",
                onValueChange = {
                    val newValue = it.toIntOrNull()
                    currentConfig = currentConfig.copy(MaxValue = newValue)
                    onConfigUpdated(currentConfig)
                },
                label = { Text("Max") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CreaturesConfigEditor(
    config: CreaturesConfiguration,
    onConfigChanged: (CreaturesConfiguration) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Creatures Configuration", style = MaterialTheme.typography.labelMedium)
        }

        IntValueConfigWidget(
            config = config.ReplacementsCount,
            onConfigChanged = {
                onConfigChanged(config.copy(ReplacementsCount = it))
            },
            label = "Replacements count",
        )

        // Boolean flags
        CreatureConfigBooleanField(
            label = "Terrain Faction",
            value = config.TerrainFaction ?: false,
            onValueChange = { newValue ->
                onConfigChanged(config.copy(TerrainFaction = newValue))
            }
        )

        CreatureConfigBooleanField(
            label = "Non-Player Factions",
            value = config.NonPlayersFactions ?: false,
            onValueChange = { newValue ->
                onConfigChanged(config.copy(NonPlayersFactions = newValue))
            }
        )

        CreatureConfigBooleanField(
            label = "No Grades",
            value = config.NoGrades ?: false,
            onValueChange = { newValue ->
                onConfigChanged(config.copy(NoGrades = newValue))
            }
        )

        CreatureConfigBooleanField(
            label = "Grades",
            value = config.Grades ?: false,
            onValueChange = { newValue ->
                onConfigChanged(config.copy(Grades = newValue))
            }
        )

        CreatureConfigBooleanField(
            label = "Neutrals",
            value = config.Neutrals ?: false,
            onValueChange = { newValue ->
                onConfigChanged(config.copy(Neutrals = newValue))
            }
        )

        CreatureConfigBooleanField(
            label = "Non-Unique Replacements",
            value = config.NonUniqueReplacements ?: false,
            onValueChange = { newValue ->
                onConfigChanged(config.copy(NonUniqueReplacements = newValue))
            }
        )

        // Multipliers
        CreatureConfigDoubleField(
            label = "Base Cost Multiplier",
            value = config.BaseCostMultiplier ?: 1.0,
            onValueChange = { newValue ->
                onConfigChanged(config.copy(BaseCostMultiplier = newValue))
            }
        )

        CreatureConfigDoubleField(
            label = "Base Resources Multiplier",
            value = config.BaseResourcesMultiplier ?: 1.0,
            onValueChange = { newValue ->
                onConfigChanged(config.copy(BaseResourcesMultiplier = newValue))
            }
        )

        CreatureConfigDoubleField(
            label = "Base Grow Multiplier",
            value = config.BaseGrowMultiplier ?: 1.0,
            onValueChange = { newValue ->
                onConfigChanged(config.copy(BaseGrowMultiplier = newValue))
            }
        )

        // Creature Modifiers
        Text(
            "Creature Modifiers",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        config.CreatureModifiers?.forEachIndexed { index, modifier ->
            CreatureModifierEditor(
                modifier = modifier,
                onModifierChanged = { newModifier ->
                    val newList = config.CreatureModifiers.toMutableList()
                    newList[index] = newModifier
                    onConfigChanged(config.copy(CreatureModifiers = newList))
                },
                onDelete = {
                    val newList = config.CreatureModifiers.toMutableList()
                    newList.removeAt(index)
                    onConfigChanged(config.copy(CreatureModifiers = newList))
                }
            )
        }

        if ((config.CreatureModifiers?.sumOf { it.Tier } ?: 0) < 28)
            Button(
                onClick = {
                    val newList = config.CreatureModifiers?.toMutableList()
                    val index = getNextIndexItem(newList?.map {
                        it.Tier
                    }, 7)
                    if (index != null)
                        onConfigChanged(
                            config.copy(
                                CreatureModifiers =
                                    (newList ?: listOf()) + CreatureModifier(Tier = index)
                            )
                        )

                },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text("Add Creature Modifier")
            }

        // Creature Tier Replacements
        Text(
            "Creature Tier Replacements",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        config.CreatureTierReplacements?.forEachIndexed { index, replacement ->
            CreatureTierReplacementEditor(
                replacement = replacement,
                onReplacementChanged = { newReplacement ->
                    val newList = config.CreatureTierReplacements.toMutableList()
                    newList[index] = newReplacement
                    onConfigChanged(config.copy(CreatureTierReplacements = newList))
                },
                onDelete = {
                    val newList = config.CreatureTierReplacements.toMutableList()
                    newList.removeAt(index)
                    onConfigChanged(config.copy(CreatureTierReplacements = newList))
                }
            )
        }

        if ((config.CreatureTierReplacements?.sumOf { it.Tier } ?: 0) < 28)
            Button(
                onClick = {
                    val newList = config.CreatureTierReplacements?.toMutableList()
                    val index = getNextIndexItem(newList?.map {
                        it.Tier
                    }, 7)
                    if (index != null) {
                        val a = (newList ?: listOf()) + CreatureTierReplacement(Tier = index, CreatureIds = emptyList())
                        onConfigChanged(config.copy(CreatureTierReplacements = a))
                    }
                },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text("Add Tier Replacement")
            }
    }
}

@Composable
private fun CreatureConfigBooleanField(
    label: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = value,
            onCheckedChange = onValueChange,
            modifier = Modifier.size(24.dp)
        )
        Text(label, modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
private fun CreatureConfigDoubleField(
    label: String,
    value: Double,
    onValueChange: (Double) -> Unit
) {
    var textValue by remember { mutableStateOf(value.toString()) }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            it.toDoubleOrNull()?.let { doubleValue ->
                onValueChange(doubleValue)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CreatureModifierEditor(
    modifier: CreatureModifier,
    onModifierChanged: (CreatureModifier) -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tier ${modifier.Tier}", style = MaterialTheme.typography.labelMedium)
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Delete")
            }
        }

        CreatureConfigDoubleField(
            label = "Cost Multiplier",
            value = modifier.CostMultiplier ?: 1.0,
            onValueChange = { newValue ->
                onModifierChanged(modifier.copy(CostMultiplier = newValue))
            }
        )

        CreatureConfigDoubleField(
            label = "Resources Multiplier",
            value = modifier.ResourcesMultiplier ?: 1.0,
            onValueChange = { newValue ->
                onModifierChanged(modifier.copy(ResourcesMultiplier = newValue))
            }
        )

        CreatureConfigDoubleField(
            label = "Grow Multiplier",
            value = modifier.GrowMultiplier ?: 1.0,
            onValueChange = { newValue ->
                onModifierChanged(modifier.copy(GrowMultiplier = newValue))
            }
        )
    }
}

@Composable
private fun CreatureTierReplacementEditor(
    replacement: CreatureTierReplacement,
    onReplacementChanged: (CreatureTierReplacement) -> Unit,
    onDelete: () -> Unit
) {
    var showCreatureSelectionDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tier ${replacement.Tier}", style = MaterialTheme.typography.labelMedium)
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Delete")
            }
        }

        var creatureIdsText by remember(replacement) {
            mutableStateOf(replacement.CreatureIds.joinToString(", "))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = creatureIdsText,
                onValueChange = {
                    creatureIdsText = it
                    val ids = it.split(",").map { id -> id.trim() }.filter { id -> id.isNotEmpty() }
                    onReplacementChanged(replacement.copy(CreatureIds = ids))
                },
                label = { Text("Creature IDs (через запятую)") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { showCreatureSelectionDialog = true },
                modifier = Modifier.height(56.dp)
            )
            {
                Icon(Icons.Default.Add, contentDescription = "Add creatures")
            }
        }
    }

    if (showCreatureSelectionDialog) {
        CreatureSelectionDialog(
            onDismiss = { showCreatureSelectionDialog = false },
            onCreaturesSelected = { selectedCreatures ->
                onReplacementChanged(
                    replacement.copy(CreatureIds = (replacement.CreatureIds + selectedCreatures.map { it.name }).distinct())
                )
            }
        )
    }
}

@Composable
private fun CreatureSelectionDialog(
    onDismiss: () -> Unit,
    onCreaturesSelected: (List<project.data.enums.Creature>) -> Unit
) {
    val allCreatures = remember { _root_ide_package_.project.data.enums.Creature.values().toList() }
    var searchQuery by remember { mutableStateOf("") }
    val filteredCreatures = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            allCreatures
        } else {
            allCreatures.filter { creature ->
                creature.russianName.contains(searchQuery, ignoreCase = true) ||
                        creature.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    val selectedCreatures = remember { mutableStateListOf<project.data.enums.Creature>() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select creatures") },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search") },
                    modifier = Modifier.fillMaxWidth()
                )

                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(filteredCreatures) { creature ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (selectedCreatures.contains(creature)) {
                                        selectedCreatures.remove(creature)
                                    } else {
                                        selectedCreatures.add(creature)
                                    }
                                }
                                .padding(8.dp)
                        ) {
                            Checkbox(
                                checked = selectedCreatures.contains(creature),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedCreatures.add(creature)
                                    } else {
                                        selectedCreatures.remove(creature)
                                    }
                                }
                            )
                            Text(
                                text = creature.russianName,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onCreaturesSelected(selectedCreatures)
                    onDismiss()
                }
            ) {
                Text("Add selected")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun StaticDwellingConfigsEditor(
    configs: StaticDwellingConfigs,
    onConfigsUpdated: (StaticDwellingConfigs) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Dwelling Configuration",
            style = MaterialTheme.typography.headlineMedium
        )
        configs.DwellingValue.fastForEachIndexed { index, dwellingValue ->
            Column {
                _root_ide_package_.project.ui.dwellingGenerationConfig.DwellingValueEditor(
                    value = dwellingValue,
                    onValueChanged = { newValue ->
                        if (newValue.isEmpty())
                            onConfigsUpdated(configs.copy(DwellingValue = configs.DwellingValue - dwellingValue))
                        else {
                            onConfigsUpdated(configs.copy(DwellingValue = configs.DwellingValue.mapIndexed { i, dv ->
                                if (i == index) newValue else dv
                            }))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Divider()
            }
        }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    onConfigsUpdated(configs.copy(DwellingValue = configs.DwellingValue + DwellingValue()))
                }
            ) {
                Text("Add New")
            }
        }
    }
}

fun getNextIndexItem(list: List<Int>?, maxIndex: Int): Int? {
    if (list.isNullOrEmpty()) {
        return 1
    } else {
        val a = list.sorted()
        for (i in 1..maxIndex) {
            if (a.contains(i).not()) {
                return i
            }
        }
        return null
    }
}
