package org.example.project.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.data.EntitiesBanConfig
import org.example.project.data.enums.ArtifactType
import org.example.project.data.enums.HeroType
import org.example.project.data.enums.SpellType
import org.example.project.ui.common.AddButton
import org.example.project.ui.components.PickerDialog


@Composable
fun BansConfigSection(
    bans: EntitiesBanConfig,
    onBansChanged: (EntitiesBanConfig) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Banned Entities", style = MaterialTheme.typography.headlineMedium)

        Text("Banned Artifacts", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
        ChipGroup(
            items = bans.BannedArtifacts,
            onItemRemoved = { artifact ->
                onBansChanged(bans.copy(BannedArtifacts = bans.BannedArtifacts - artifact))
            }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            var isDialogOpen by remember { mutableStateOf(false) }
            PickerDialog(
                show = isDialogOpen,
                onDismiss = { isDialogOpen = false },
                items = ArtifactType.values().toList(),
                text = { it.description },
                onBuildingSelected = { artifact ->
                    onBansChanged(bans.copy(BannedArtifacts = bans.BannedArtifacts + artifact.name))
                    isDialogOpen = false
                }
            )
            AddButton { isDialogOpen = true }
        }

        Text("Banned Heroes", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        ChipGroup(
            items = bans.BannedHeroes,
            onItemRemoved = { hero ->
                onBansChanged(bans.copy(BannedHeroes = bans.BannedHeroes - hero))
            }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            var isDialogOpen by remember { mutableStateOf(false) }
            PickerDialog(
                show = isDialogOpen,
                onDismiss = { isDialogOpen = false },
                items = HeroType.values().toList(),
                text = { it.description.ifEmpty { it.name } },
                onBuildingSelected = { hero ->
                    onBansChanged(bans.copy(BannedHeroes = bans.BannedHeroes + hero.name))
                    isDialogOpen = false
                }
            )
            AddButton{ isDialogOpen = true }
        }

        Text("Banned Spells", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        ChipGroup(
            items = bans.BannedSpells,
            onItemRemoved = { spell ->
                onBansChanged(bans.copy(BannedSpells = bans.BannedSpells - spell))
            }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            var isDialogOpen by remember { mutableStateOf(false) }
            PickerDialog(
                show = isDialogOpen,
                onDismiss = { isDialogOpen = false },
                items = SpellType.values().toList(),
                text = { it.description.ifEmpty { it.name } },
                onBuildingSelected = { newSpell ->
                    onBansChanged(bans.copy(BannedSpells = bans.BannedSpells + newSpell.name))
                    isDialogOpen = false
                }
            )
            AddButton { isDialogOpen = true }
        }
    }
}