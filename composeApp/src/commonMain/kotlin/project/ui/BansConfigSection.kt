package project.ui

import ArtifactType
import BannedBasesByClass
import BasesBanModel
import EntitiesBanModel
import HeroClassType
import HeroType
import SkillType
import SpellType
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import project.ui.common.AddButton
import project.ui.components.PickerDialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.Alignment

@Composable
fun BansConfigSection(
    bans: EntitiesBanModel,
    onBansChanged: (EntitiesBanModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Баны", style = MaterialTheme.typography.headlineMedium)

        Text(
            "Banned Artifacts",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        _root_ide_package_.project.ui.ChipGroup(
            items = bans.BannedArtifacts,
            title = { it.description },
            onItemRemoved = { artifact ->
                onBansChanged(bans.copy(BannedArtifacts = bans.BannedArtifacts - artifact))
            }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            var isDialogOpen by remember { mutableStateOf(false) }
            _root_ide_package_.project.ui.components.PickerDialog(
                show = isDialogOpen,
                onDismiss = { isDialogOpen = false },
                items = ArtifactType.values().toList(),
                text = { it.description },
                onBuildingSelected = { artifact ->
                    onBansChanged(
                        bans.copy(BannedArtifacts = bans.BannedArtifacts + artifact)
                    )
                    isDialogOpen = false
                }
            )
            _root_ide_package_.project.ui.common.AddButton { isDialogOpen = true }
        }

        Text("Banned Heroes", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        _root_ide_package_.project.ui.ChipGroup(
            items = bans.BannedHeroes,
            title = { it.description.ifEmpty { it.name } },
            onItemRemoved = { hero ->
                onBansChanged(bans.copy(BannedHeroes = bans.BannedHeroes - hero))
            }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            var isDialogOpen by remember { mutableStateOf(false) }
            _root_ide_package_.project.ui.components.PickerDialog(
                show = isDialogOpen,
                onDismiss = { isDialogOpen = false },
                items = HeroType.values().toList(),
                text = { it.description.ifEmpty { it.name } },
                onBuildingSelected = { hero ->
                    onBansChanged(bans.copy(BannedHeroes = bans.BannedHeroes + hero))
                    isDialogOpen = false
                }
            )
            _root_ide_package_.project.ui.common.AddButton { isDialogOpen = true }
        }

        Text("Banned Spells", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        _root_ide_package_.project.ui.ChipGroup(
            items = bans.BannedSpells,
            title = { it.description.ifEmpty { it.name } },
            onItemRemoved = { spell ->
                onBansChanged(bans.copy(BannedSpells = bans.BannedSpells - spell))
            }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            var isDialogOpen by remember { mutableStateOf(false) }
            _root_ide_package_.project.ui.components.PickerDialog(
                show = isDialogOpen,
                onDismiss = { isDialogOpen = false },
                items = SpellType.values().toList(),
                text = { it.description.ifEmpty { it.name } },
                onBuildingSelected = { newSpell ->
                    onBansChanged(bans.copy(BannedSpells = bans.BannedSpells + newSpell))
                    isDialogOpen = false
                }
            )
            _root_ide_package_.project.ui.common.AddButton { isDialogOpen = true }
        }

        Text("забанить мародерство?")
        var banMaradeur by remember { mutableStateOf(bans.BanMaradeur == true) }
        Checkbox(
            checked = banMaradeur,
            onCheckedChange = {
                onBansChanged(bans.copy(BanMaradeur = it))
                banMaradeur = it
            }
        )

        Text(
            "забаненные базовые скиллы",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        _root_ide_package_.project.ui.BasesBanEditor(
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

    Column(modifier = modifier.padding(16.dp)) {
        Text("Bases Ban Editor")
        Spacer(modifier = Modifier.height(16.dp))

        // Common banned skills section
        _root_ide_package_.project.ui.CommonBannedSkillsEditor(
            skills = currentModel.CommonBannedSkills,
            onSkillsChanged = { newSkills ->
                currentModel = currentModel.copy(CommonBannedSkills = newSkills)
                onModelChanged(currentModel)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Class-specific bans section
        _root_ide_package_.project.ui.ClassSpecificBansEditor(
            bans = currentModel.SkillsBannedForClass,
            onBansChanged = { newBans ->
                currentModel = currentModel.copy(SkillsBannedForClass = newBans)
                onModelChanged(currentModel)
            }
        )
    }
}

@Composable
private fun CommonBannedSkillsEditor(
    skills: List<SkillType>,
    onSkillsChanged: (List<SkillType>) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var availableSkills by remember {
        mutableStateOf(SkillType.values().toList() - skills.toSet())
    }

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

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (availableSkills.isEmpty()) {
                DropdownMenuItem(
                    onClick = { expanded = false },
                    text = {
                        Text("All skills are banned")
                    }
                )
            } else {
                availableSkills.forEach { skill ->
                    DropdownMenuItem(onClick = {
                        val newList = skills + skill
                        onSkillsChanged(newList)
                        availableSkills = availableSkills - skill
                        expanded = false
                    }, text = {
                        Text("${skill.description} (${skill.number})")
                    })
                }
            }
        }

        if (skills.isEmpty()) {
            Text("No common banned skills")
        } else {
            FlowColumn(modifier = Modifier.fillMaxWidth()) {
                skills.forEach { skill ->
                    _root_ide_package_.project.ui.SkillItem(
                        skill = skill,
                        onRemove = {
                            val newList = skills - skill
                            onSkillsChanged(newList)
                            availableSkills = availableSkills + skill
                        }
                    )
                }
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

        if (bans.isEmpty()) {
            Text("No class-specific bans")
        } else {
            FlowColumn(modifier = Modifier.fillMaxWidth()) {
                bans.forEach { ban ->
                    _root_ide_package_.project.ui.ClassBanItem(
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
    }

    if (showAddDialog) {
        _root_ide_package_.project.ui.AddClassBanDialog(
            existingClasses = bans.map { it.Class },
            onAdd = { newBan ->
                onBansChanged(bans + newBan)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
private fun SkillItem(
    skill: SkillType,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "${skill.description} (${skill.number})",
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, "Remove skill")
            }
        }
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
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = ban.Class.description
                )
                Row {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, "Edit ban")
                    }
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Default.Delete, "Remove ban")
                    }
                }
            }

            if (ban.Skills.isEmpty()) {
                Text("No skills banned for this class")
            } else {
                Text("Banned skills:")
                FlowRow(modifier = Modifier.padding(top = 4.dp)) {
                    ban.Skills.forEach { skill ->
                        _root_ide_package_.project.ui.Chip(
                            text = skill.description,
                            onRemove = {
                                onBanChanged(ban.copy(Skills = ban.Skills - skill))
                            }
                        )
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        _root_ide_package_.project.ui.EditClassBanDialog(
            ban = ban,
            onSave = { updatedBan ->
                onBanChanged(updatedBan)
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }
}

@Composable
private fun AddClassBanDialog(
    existingClasses: List<HeroClassType>,
    onAdd: (BannedBasesByClass) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedClass by remember { mutableStateOf<HeroClassType?>(null) }
    val availableClasses = remember(existingClasses) {
        HeroClassType.values().toList() - existingClasses.toSet()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Class Ban") },
        text = {
            Column {
                Text("Select class to ban skills for:")
                Spacer(modifier = Modifier.height(8.dp))
                _root_ide_package_.project.ui.DropdownMenuBox(
                    items = availableClasses,
                    selectedItem = selectedClass,
                    onItemSelected = { selectedClass = it },
                    itemText = {
                        selectedClass?.description ?: "Select class"
                    },
                    itemContent = { it?.description ?: "" }
                )
            }
        },
        confirmButton = {

            Button(
                onClick = {
                    selectedClass?.let {
                        onAdd(BannedBasesByClass(it, emptyList()))
                    }
                },
                enabled = selectedClass != null
            ) {
                Text("Add")
            }
        },
        dismissButton = {

            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Cancel")
            }
        },
    )
}

@Composable
private fun EditClassBanDialog(
    ban: BannedBasesByClass,
    onSave: (BannedBasesByClass) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedSkills by remember { mutableStateOf(ban.Skills.toSet()) }
    val allSkills = SkillType.values().toList()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Ban for ${ban.Class.description}") },
        text = {
            Column {
                Text("Select skills to ban:")
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(allSkills) { skill ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(4.dp)
                        ) {
                            Checkbox(
                                checked = selectedSkills.contains(skill),
                                onCheckedChange = { checked ->
                                    selectedSkills = if (checked) {
                                        selectedSkills + skill
                                    } else {
                                        selectedSkills - skill
                                    }
                                }
                            )
                            Text(
                                text = "${skill.description} (${skill.number})",
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
                    onSave(ban.copy(Skills = selectedSkills.toList()))
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Cancel")
            }
        },
    )
}

@Composable
private fun Chip(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(end = 4.dp, bottom = 4.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
        ) {
            Text(text)
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(16.dp)
            ) {
                Icon(Icons.Default.Close, "Remove", modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
private fun <T> DropdownMenuBox(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemText: @Composable () -> String,
    itemContent: @Composable (T) -> String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(itemText())
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                    expanded = false
                }, text = {
                    Text(itemContent(item))
                })
            }
        }
    }
}