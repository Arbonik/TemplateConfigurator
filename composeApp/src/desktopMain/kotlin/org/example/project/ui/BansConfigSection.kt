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

        var newArtifact by remember { mutableStateOf("") }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            OutlinedTextField(
                value = newArtifact,
                onValueChange = { newArtifact = it },
                label = { Text("New Artifact") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (newArtifact.isNotBlank()) {
                        onBansChanged(bans.copy(BannedArtifacts = bans.BannedArtifacts + newArtifact))
                        newArtifact = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }

        Text("Banned Heroes", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        ChipGroup(
            items = bans.BannedHeroes,
            onItemRemoved = { hero ->
                onBansChanged(bans.copy(BannedHeroes = bans.BannedHeroes - hero))
            }
        )

        var newHero by remember { mutableStateOf("") }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            OutlinedTextField(
                value = newHero,
                onValueChange = { newHero = it },
                label = { Text("New Hero") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (newHero.isNotBlank()) {
                        onBansChanged(bans.copy(BannedHeroes = bans.BannedHeroes + newHero))
                        newHero = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }

        Text("Banned Spells", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        ChipGroup(
            items = bans.BannedSpells,
            onItemRemoved = { spell ->
                onBansChanged(bans.copy(BannedSpells = bans.BannedSpells - spell))
            }
        )

        var newSpell by remember { mutableStateOf("") }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            OutlinedTextField(
                value = newSpell,
                onValueChange = { newSpell = it },
                label = { Text("New Spell") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (newSpell.isNotBlank()) {
                        onBansChanged(bans.copy(BannedSpells = bans.BannedSpells + newSpell))
                        newSpell = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }
    }
}