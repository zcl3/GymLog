package com.personal.gymlog.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppDestination(val route: String, val label: String) {
    data object Home : AppDestination("home", "首页")
    data object Workout : AppDestination("workout", "训练")
    data object Nutrition : AppDestination("nutrition", "饮食")
    data object Water : AppDestination("water", "喝水")
    data object More : AppDestination("more", "更多")
    data object Statistics : AppDestination("statistics", "统计")
    data object Exercises : AppDestination("exercises", "动作库")
    data object Templates : AppDestination("templates", "训练模板")
    data object Settings : AppDestination("settings", "设置")
}

data class BottomDestination(val destination: AppDestination, val icon: ImageVector)

val bottomDestinations = listOf(
    BottomDestination(AppDestination.Home, Icons.Outlined.Dashboard),
    BottomDestination(AppDestination.Workout, Icons.Outlined.CalendarMonth),
    BottomDestination(AppDestination.Nutrition, Icons.Outlined.Restaurant),
    BottomDestination(AppDestination.Water, Icons.Outlined.WaterDrop),
    BottomDestination(AppDestination.More, Icons.Outlined.MoreHoriz),
)
