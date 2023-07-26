package com.asimodabas.haberinolsun.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "article")
data class Article(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String? = null,
    val source: Source? = null,
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String? = null
) : Serializable