package com.personal.gymlog.feature.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.gymlog.data.local.entity.FoodEntry
import com.personal.gymlog.data.repository.GymLogRepository
import com.personal.gymlog.data.settings.AppSettings
import com.personal.gymlog.data.settings.SettingsRepository
import com.personal.gymlog.data.settings.recordDate
import com.personal.gymlog.feature.home.trimNumber
import kotlinx.coroutines.launch

private val mealTypes = listOf("早餐", "午餐", "晚餐", "加餐")

@Composable
fun NutritionScreen(repository: GymLogRepository, settingsRepository: SettingsRepository) {
    val settings by settingsRepository.settings.collectAsStateWithLifecycle(AppSettings())
    val date = settings.recordDate()
    val foods by repository.observeFoods(date).collectAsStateWithLifecycle(emptyList())
    val scope = rememberCoroutineScope()
    var editing by remember { mutableStateOf<FoodEntry?>(null) }
    var adding by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("饮食")
        Text("$date · ${foods.size} 项记录")
        Button(onClick = { adding = true }, Modifier.fillMaxWidth()) { Text("添加食物") }
        if (foods.isEmpty()) Text("当天还没有饮食记录")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(foods, key = { it.id }) { food ->
                FoodRow(food, onEdit = { editing = food }, onDelete = { scope.launch { repository.deleteFood(food.id) } })
            }
        }
    }
    if (adding) FoodDialog(onDismiss = { adding = false }) { form ->
        scope.launch {
            repository.addFood(form.name, form.meal, form.calories, form.protein, form.carbs, form.fat, date)
            adding = false
        }
    }
    editing?.let { original ->
        FoodDialog(entry = original, onDismiss = { editing = null }) { form ->
            scope.launch {
                repository.updateFood(original, form.name, form.meal, form.calories, form.protein, form.carbs, form.fat)
                editing = null
            }
        }
    }
}

@Composable
private fun FoodRow(food: FoodEntry, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("${food.mealType} · ${food.name}")
            val details = listOfNotNull(
                food.caloriesKcal?.let { "${trimNumber(it)} kcal" },
                food.proteinGrams?.let { "蛋白质 ${trimNumber(it)} g" },
                food.carbsGrams?.let { "碳水 ${trimNumber(it)} g" },
                food.fatGrams?.let { "脂肪 ${trimNumber(it)} g" },
            )
            if (details.isNotEmpty()) Text(details.joinToString(" · "))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onEdit) { Text("编辑") }
                TextButton(onClick = onDelete) { Text("删除") }
            }
        }
    }
}

private data class FoodForm(val name: String, val meal: String, val calories: Double?, val protein: Double?, val carbs: Double?, val fat: Double?)

@Composable
private fun FoodDialog(entry: FoodEntry? = null, onDismiss: () -> Unit, onSave: (FoodForm) -> Unit) {
    var name by remember(entry?.id) { mutableStateOf(entry?.name.orEmpty()) }
    var meal by remember(entry?.id) { mutableStateOf(entry?.mealType ?: "早餐") }
    var kcal by remember(entry?.id) { mutableStateOf(entry?.caloriesKcal?.let(::trimNumber).orEmpty()) }
    var protein by remember(entry?.id) { mutableStateOf(entry?.proteinGrams?.let(::trimNumber).orEmpty()) }
    var carbs by remember(entry?.id) { mutableStateOf(entry?.carbsGrams?.let(::trimNumber).orEmpty()) }
    var fat by remember(entry?.id) { mutableStateOf(entry?.fatGrams?.let(::trimNumber).orEmpty()) }
    fun numeric(value: String): Double? = value.trim().takeIf { it.isNotEmpty() }?.toDoubleOrNull()?.takeIf { it >= 0 && it.isFinite() }
    val valid = name.isNotBlank() && listOf(kcal, protein, carbs, fat).all { it.isBlank() || numeric(it) != null }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (entry == null) "添加食物" else "编辑食物") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("食物名称") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Text("餐次")
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    mealTypes.take(2).forEach { option -> FilterChip(selected = meal == option, onClick = { meal = option }, label = { Text(option) }) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    mealTypes.drop(2).forEach { option -> FilterChip(selected = meal == option, onClick = { meal = option }, label = { Text(option) }) }
                }
                NumberField(kcal, { kcal = it }, "热量 kcal（可选）")
                NumberField(protein, { protein = it }, "蛋白质 g（可选）")
                NumberField(carbs, { carbs = it }, "碳水 g（可选）")
                NumberField(fat, { fat = it }, "脂肪 g（可选）")
                if (!valid) Text("请填写食物名称；营养数值需为不小于 0 的数字")
            }
        },
        confirmButton = { Button(enabled = valid, onClick = { onSave(FoodForm(name.trim(), meal, numeric(kcal), numeric(protein), numeric(carbs), numeric(fat))) }) { Text("保存") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
    )
}

@Composable
private fun NumberField(value: String, onValueChange: (String) -> Unit, label: String) =
    OutlinedTextField(value, onValueChange, label = { Text(label) }, singleLine = true, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
