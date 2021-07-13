package com.example.newsfeedcm1.localdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_table")
    fun readAll(): LiveData<List<News>>

    @Query("DELETE FROM news_table WHERE url = :url")
    fun delete(url: String)

    @Query("SELECT * FROM news_table WHERE url = :url")
    fun getByUrl(url: String): News

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newsCache: News)
}