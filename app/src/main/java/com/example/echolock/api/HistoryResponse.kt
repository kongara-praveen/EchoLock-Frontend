package com.example.echolock.api

import com.example.echolock.model.HistoryItem

data class HistoryResponse(
    val status: String,
    val today: List<HistoryItem>,
    val yesterday: List<HistoryItem>
)
