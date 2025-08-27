package project.ui

import ArtifactType
import BannedBasesByClass
import BasesBanModel
import Chip
import EntitiesBanModel
import HeroClassType
import HeroType
import SearchableEnumDialog
import SkillType
import SpellType
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.collections.plus


@Composable
fun <T> ListWithDialog(
    label: String,
    allItems: List<T>,
    currentItems: List<T>,
    itemTitle: (T) -> String,
    onConfigChanged: (List<T>) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
        )
        var isDialogOpen by remember { mutableStateOf(false) }

        if (isDialogOpen)
            SearchableEnumDialog(
                label = "Select",
                items = allItems - currentItems,
                itemTitle = itemTitle,
                onItemSelected = { new ->
                    onConfigChanged(currentItems + new)
                    isDialogOpen = false
                },
                onDismiss = { isDialogOpen = false }
            )
        IconButton(onClick = {
            isDialogOpen = true
        }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
    // Show selected buildings as chips
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        currentItems.forEach { item ->
            Chip(
                label = { Text(itemTitle(item)) },
                modifier = Modifier.padding(4.dp).clickable {
                    onConfigChanged(allItems - item)
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
fun BansConfigSection(
    bans: EntitiesBanModel,
    onBansChanged: (EntitiesBanModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Banned Artifacts",
            )
            var isDialogOpen by remember { mutableStateOf(false) }

            if (isDialogOpen)
                SearchableEnumDialog(
                    label = "Select Arifact",
                    items = ArtifactType.entries - bans.BannedArtifacts.toSet(),
                    itemTitle = { "${it.description} (${it.name})" },
                    onItemSelected = { artifact ->
                        onBansChanged(
                            bans.copy(BannedArtifacts = bans.BannedArtifacts + artifact as ArtifactType)
                        )
                        isDialogOpen = false
                    },
                    onDismiss = { isDialogOpen = false }
                )
            IconButton(onClick = {
                isDialogOpen = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
        // Show selected buildings as chips
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            bans.BannedArtifacts.forEach { artifact ->
                Chip(
                    label = { Text(artifact.description) },
                    modifier = Modifier.padding(4.dp).clickable {
                        onBansChanged(bans.copy(BannedArtifacts = bans.BannedArtifacts - artifact))
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Banned Heroes")
            var isDialogOpen by remember { mutableStateOf(false) }
            if (isDialogOpen)
                SearchableEnumDialog(
                    label = "Select Hero",
                    onDismiss = { isDialogOpen = false },
                    items = HeroType.entries - bans.BannedHeroes.toSet(),
                    itemTitle = { it.description.ifEmpty { it.name } },
                    onItemSelected = { hero ->
                        onBansChanged(bans.copy(BannedHeroes = bans.BannedHeroes + hero))
                        isDialogOpen = false
                    }
                )

            IconButton(onClick = {
                isDialogOpen = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            bans.BannedHeroes.forEach { hero ->
                Chip(
                    label = { Text(hero.description.ifEmpty { hero.name }) },
                    modifier = Modifier.padding(4.dp).clickable {
                        onBansChanged(bans.copy(BannedHeroes = bans.BannedHeroes - hero))
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Banned Spells")
            var isDialogOpen by remember { mutableStateOf(false) }
            if (isDialogOpen)
                SearchableEnumDialog(
                    label = "Select Spell",
                    onDismiss = { isDialogOpen = false },
                    items = SpellType.entries - bans.BannedSpells.toSet(),
                    itemTitle = { it.description.ifEmpty { it.name } },
                    onItemSelected = { newSpell ->
                        onBansChanged(bans.copy(BannedSpells = bans.BannedSpells + newSpell))
                        isDialogOpen = false
                    }
                )

            IconButton(onClick = {
                isDialogOpen = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            bans.BannedSpells.forEach { spell ->
                Chip(
                    label = { Text(spell.description.ifEmpty { spell.name }) },
                    modifier = Modifier.padding(4.dp).clickable {
                        onBansChanged(bans.copy(BannedSpells = bans.BannedSpells - spell))
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("забанить мародерство?")
            var banMaradeur by remember { mutableStateOf(bans.BanMaradeur == true) }
            Checkbox(
                checked = banMaradeur,
                onCheckedChange = {
                    onBansChanged(bans.copy(BanMaradeur = it))
                    banMaradeur = it
                }
            )
        }

        BasesBanEditor(
            bans.BannedBases,
            onModelChanged = { newBases ->
                onBansChanged(bans.copy(BannedBases = newBases))
            }
        )
    }
}

@Composable
fun BasesBanEditor(
    model: BasesBanModel,
    onModelChanged: (BasesBanModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentModel by remember { mutableStateOf(model) }

    CommonBannedSkillsEditor(
        skills = currentModel.CommonBannedSkills,
        onSkillsChanged = { newSkills ->
            currentModel = currentModel.copy(CommonBannedSkills = newSkills)
            onModelChanged(currentModel)
        }
    )

    // Class-specific bans section
    ClassSpecificBansEditor(
        bans = currentModel.SkillsBannedForClass,
        onBansChanged = { newBans ->
            currentModel = currentModel.copy(SkillsBannedForClass = newBans)
            onModelChanged(currentModel)
        }
    )
}

@Composable
private fun CommonBannedSkillsEditor(
    skills: List<SkillType>,
    onSkillsChanged: (List<SkillType>) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Common Banned Skills")
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Add, "Add skill")
            }
        }
        if (expanded)
            SearchableEnumDialog(
                label = "Select Skill",
                onDismiss = { expanded = false },
                items = SkillType.entries - skills.toSet(),
                itemTitle = { it.description.ifEmpty { it.name } },
                onItemSelected = { newSpell ->
                    onSkillsChanged(skills + newSpell)
                    expanded = false
                }
            )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            skills.forEach { skill ->
                Chip(
                    label = { Text(skill.description.ifEmpty { skill.name }) },
                    modifier = Modifier.padding(4.dp).clickable {
                        onSkillsChanged(skills - skill)
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
}

@Composable
private fun ClassSpecificBansEditor(
    bans: List<BannedBasesByClass>,
    onBansChanged: (List<BannedBasesByClass>) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Class-Specific Bans")
            IconButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, "Add class ban")
            }
        }

        FlowColumn(modifier = Modifier.fillMaxWidth()) {
            bans.forEach { ban ->
                ClassBanItem(
                    ban = ban,
                    onRemove = {
                        onBansChanged(bans - ban)
                    },
                    onBanChanged = { updatedBan ->
                        onBansChanged(bans.map { if (it.Class == updatedBan.Class) updatedBan else it })
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        SearchableEnumDialog(
            label = "Select class",
            onDismiss = { showAddDialog = false },
            items = HeroClassType.entries - bans.map { it.Class }.toSet(),
            itemTitle = { it.description.ifEmpty { it.name } },
            onItemSelected = { newBan ->
                onBansChanged(bans + BannedBasesByClass(newBan, emptyList()))
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun ClassBanItem(
    ban: BannedBasesByClass,
    onRemove: () -> Unit,
    onBanChanged: (BannedBasesByClass) -> Unit,
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { showEditDialog = true }) {
                    Icon(Icons.Default.Add, "Add ban")
                }
                Text(
                    text = ban.Class.description
                )

                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, "Remove ban")
                }
            }

            Text("Banned skills:")
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ban.Skills.forEach { skill ->
                    Chip(
                        label = { Text(skill.description.ifEmpty { skill.name }) },
                        modifier = Modifier.padding(4.dp).clickable {
                            onBanChanged(ban.copy(Skills = ban.Skills - skill))
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
    }

    if (showEditDialog)
        SearchableEnumDialog(
            label = "Select Skill",
            onDismiss = { showEditDialog = false },
            items = SkillType.entries - ban.Skills.toSet(),
            itemTitle = { it.description.ifEmpty { it.name } },
            onItemSelected = { newban ->
                onBanChanged(ban.copy(Skills = ban.Skills + newban))
                showEditDialog = false
            }
        )
}