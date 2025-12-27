package com.example.echolock.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echolock.api.RetrofitClient
import com.example.echolock.model.FileItem
import kotlinx.coroutines.launch

class FilesViewModel : ViewModel() {

    private val _files = mutableStateOf<List<FileItem>>(emptyList())
    val files: State<List<FileItem>> = _files

    private val _loading = mutableStateOf(true)
    val loading: State<Boolean> = _loading

    fun loadFiles(userId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getFiles(userId)
                if (response.status == "success") {
                    _files.value = response.data
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
}
