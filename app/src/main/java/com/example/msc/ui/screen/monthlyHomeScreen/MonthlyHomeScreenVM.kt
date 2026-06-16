package com.example.msc.ui.screen.monthlyHomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
import com.example.msc.domain.usecase.family.GetFamilyGroupUseCase
import com.example.msc.domain.usecase.purchases.GetMonthlyExpensesUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MonthlyHomeScreenVM(
    private val getPurchasesDetailUseCase: GetPurchasesDetailUseCase,
    private val getMonthlyExpensesUseCase: GetMonthlyExpensesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val getFamilyGroupUseCase: GetFamilyGroupUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MonthlyHomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val firebaseUser = getCurrentUserUseCase()
        firebaseUser?.let { user ->
            viewModelScope.launch {
                val userData = getUsernameUseCase(user.uid)
                val currentUsername = userData?.username ?: user.displayName ?: "User"
                _uiState.update { it.copy(username = currentUsername) }

                val userIds = mutableListOf(user.uid)
                userData?.familyGroupId?.let { groupId ->
                    val familyGroup = getFamilyGroupUseCase(groupId)
                    familyGroup?.members?.let { members ->
                        userIds.addAll(members.filter { it != user.uid })
                    }
                }
                getMonthlyPurchases(userIds)
            }
        }
    }

    private fun getMonthlyPurchases(userIds: List<String>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getPurchasesDetailUseCase(userIds).collect { purchases ->
                val monthlyData = getMonthlyExpensesUseCase(purchases)
                _uiState.update { 
                    it.copy(
                        monthlyTotals = monthlyData,
                        isLoading = false
                    )
                }
            }
        }
    }
}
