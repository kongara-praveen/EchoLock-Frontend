package com.example.echolock.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
    
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadFiles(userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                android.util.Log.d("FilesViewModel", "Loading files for user_id: $userId")
                val response = RetrofitClient.instance.getFiles(userId)
                android.util.Log.d("FilesViewModel", "Response status: ${response.status}, data count: ${response.data.size}")
                if (response.status == "success") {
                    // Log file URLs for debugging
                    response.data.forEach { file ->
                        android.util.Log.d("FilesViewModel", "File: ${file.name}, type: ${file.type}, file_url: ${file.file_url}")
                    }
                    _files.value = response.data
                    android.util.Log.d("FilesViewModel", "Files loaded: ${response.data.size} files")
                } else {
                    _error.value = "Failed to load files: ${response.status}"
                    android.util.Log.e("FilesViewModel", "API returned non-success status: ${response.status}")
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                android.util.Log.e("FilesViewModel", "Error loading files", e)
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
    
    fun refreshFiles(userId: Int) {
        loadFiles(userId)
    }

    fun deleteFile(file: FileItem, userId: Int, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                if (file.file_id != null) {
                    val response = RetrofitClient.instance.deleteFile(file.file_id)
                    if (response.status == "success") {
                        // Remove from local list immediately for better UX
                        _files.value = _files.value.filter { it.file_id != file.file_id }
                        // Refresh from server to ensure consistency
                        loadFiles(userId)
                        onSuccess()
                    } else {
                        val errorMsg = response.message ?: "Failed to delete file"
                        _error.value = errorMsg
                        onError(errorMsg)
                    }
                } else {
                    val errorMsg = "File ID is missing"
                    _error.value = errorMsg
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                val errorMsg = "Error: ${e.message}"
                _error.value = errorMsg
                android.util.Log.e("FilesViewModel", "Error deleting file", e)
                onError(errorMsg)
            }
        }
    }

    fun shareFile(context: Context, file: FileItem) {
        viewModelScope.launch {
            try {
                if (file.file_url != null && file.file_url.isNotBlank()) {
                    // Share the file URL
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Check out this file: ${file.name}\n${file.file_url}")
                        putExtra(Intent.EXTRA_SUBJECT, "Sharing ${file.name}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share ${file.name}"))
                } else {
                    // If no URL, try to share file info
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "File: ${file.name}\nType: ${file.type}\nSize: ${file.size}")
                        putExtra(Intent.EXTRA_SUBJECT, "Sharing ${file.name}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share ${file.name}"))
                }
            } catch (e: Exception) {
                android.util.Log.e("FilesViewModel", "Error sharing file", e)
                Toast.makeText(context, "Error sharing file: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
