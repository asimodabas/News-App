package com.asimodabas.haberinolsun.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asimodabas.haberinolsun.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}