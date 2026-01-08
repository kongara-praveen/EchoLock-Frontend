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

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadFiles(userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.instance.getFiles(userId)
                val fileList = response.data ?: emptyList()

                if (response.status == "success") {
                    _files.value = fileList
                } else {
                    _error.value = response.message ?: "Failed to load files"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun refreshFiles(userId: Int) {
        loadFiles(userId)
    }

    /**
     * Delete disabled because file_id is not used
     * (kept only to avoid UI crashes)
     */
    fun deleteFile(
        file: FileItem,
        userId: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        onError("Delete not supported without file ID")
    }

    fun shareFile(context: Context, file: FileItem) {
        try {
            val text = if (!file.file_url.isNullOrBlank()) {
                "File: ${file.name}\n${file.file_url}"
            } else {
                "File: ${file.name}\nType: ${file.type}\nSize: ${file.size}"
            }

            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, text)
                putExtra(Intent.EXTRA_SUBJECT, "EchoLock File")
                type = "text/plain"
            }

            context.startActivity(
                Intent.createChooser(intent, "Share file")
            )
        } catch (_: Exception) {
            Toast.makeText(context, "Share failed", Toast.LENGTH_SHORT).show()
        }
    }
}
