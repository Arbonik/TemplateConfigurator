package project.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.ui.graphics.vector.ImageVector

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
//    object PNG : NavigationItem("png", "Graph Scheme", Icons.Default.AttachFile)
}