package com.kyawzinlinn.tmdb.utils

data class MovieState(
    val isLoading: Boolean = false,
    val data: Any? = null,
    val error: String = ""
)