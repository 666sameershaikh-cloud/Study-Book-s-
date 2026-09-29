package com.example.data.model

data class SchoolClass(
    val classNumber: Int,
    val title: String,
    val description: String,
    val badgeColor: Long,
    val subjects: List<Subject>
)

data class Subject(
    val id: String,
    val name: String,
    val category: String,
    val iconName: String,
    val accentColor: Long,
    val books: List<BookReference>
)

data class BookReference(
    val id: String,
    val title: String,
    val authorOrBoard: String,
    val edition: String,
    val summary: String,
    val chaptersCount: Int,
    val sampleChapters: List<String>
)
