package com.asimodabas.haberinolsun.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asimodabas.haberinolsun.domain.model.Article

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article): Long

    @Query("SELECT * FROM article")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}