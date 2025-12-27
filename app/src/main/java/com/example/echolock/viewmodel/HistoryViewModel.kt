package com.example.echolock.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echolock.api.RetrofitClient
import com.example.echolock.model.HistoryItem
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val _today = mutableStateOf<List<HistoryItem>>(emptyList())
    val today: State<List<HistoryItem>> = _today

    private val _yesterday = mutableStateOf<List<HistoryItem>>(emptyList())
    val yesterday: State<List<HistoryItem>> = _yesterday

    private val _loading = mutableStateOf(true)
    val loading: State<Boolean> = _loading

    fun loadHistory(userId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getHistory(userId)
                if (response.status == "success") {
                    _today.value = response.today
                    _yesterday.value = response.yesterday
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
}
