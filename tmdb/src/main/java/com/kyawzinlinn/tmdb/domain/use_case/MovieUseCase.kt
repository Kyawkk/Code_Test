package com.kyawzinlinn.tmdb.domain.use_case

import javax.inject.Inject

data class MovieUseCase @Inject constructor(
    val popularMovieUseCase: PopularMovieUseCase,
    val upComingMovieUseCase: UpComingMovieUseCase,
    val movieDetailsUseCase: MovieDetailsUseCase,
    val movieCastsUseCase: MovieCastsUseCase
)
