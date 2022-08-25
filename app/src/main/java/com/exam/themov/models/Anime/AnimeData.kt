package com.exam.themov.models.Anime

data class AnimeData(
    val page: Int,
    val results: List<AnimeResult>,
    val total_pages: Int,
    val total_results: Int
)