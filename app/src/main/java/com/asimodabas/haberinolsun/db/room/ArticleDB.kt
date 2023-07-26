package com.asimodabas.haberinolsun.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.asimodabas.haberinolsun.db.Converters
import com.asimodabas.haberinolsun.domain.model.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArticleDB : RoomDatabase() {
    abstract fun articleDao(): ArticleDAO

    companion object {
        @Volatile
        private var INSTANCE: ArticleDB? = null

        fun getInstance(context: Context): ArticleDB {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): ArticleDB {
            return Room.databaseBuilder(
                context.applicationContext, ArticleDB::class.java, "article_db"
            ).build()
        }
    }
}