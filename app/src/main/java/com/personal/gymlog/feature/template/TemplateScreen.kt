package com.personal.gymlog.feature.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.gymlog.data.repository.GymLogRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun TemplateScreen(repository: GymLogRepository) {
    val templates by repository.observeTemplates().collectAsStateWithLifecycle(emptyList())
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("训练模板")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(templates, key = { it.id }) { template ->
                Card(Modifier.fillMaxWidth()) { Text(template.name, Modifier.padding(16.dp)) }
            }
            item { Button(onClick = { showDialog = true }, Modifier.fillMaxWidth()) { Text("新建模板") } }
        }
    }
    if (showDialog) {
        var name by remember { mutableStateOf("") }
        AlertDialog(onDismissRequest = { showDialog = false }, title = { Text("新建训练模板") }, text = { OutlinedTextField(name, { name = it }, label = { Text("模板名称") }, singleLine = true) }, confirmButton = { TextButton(enabled = name.isNotBlank(), onClick = { scope.launch { repository.addTemplate(name); showDialog = false } }) { Text("保存") } }, dismissButton = { TextButton(onClick = { showDialog = false }) { Text("取消") } })
    }
}
