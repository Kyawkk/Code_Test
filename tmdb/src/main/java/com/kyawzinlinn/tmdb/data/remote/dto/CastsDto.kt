package com.kyawzinlinn.tmdb.data.remote.dto

data class CastsDto(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)