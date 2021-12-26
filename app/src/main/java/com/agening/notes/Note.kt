package com.agening.notes

data class Note (
    val id:Int,
    val title: String,
    val description: String,
    val dayOfWeek: String,
    val priority:Int
)