package com.example.msc.ui.screen.monthlyHomeScreen

data class MonthlyHomeScreenUiState(
    val monthlyTotals: Map<String, Double> = emptyMap(),
    val isLoading: Boolean = false
)
