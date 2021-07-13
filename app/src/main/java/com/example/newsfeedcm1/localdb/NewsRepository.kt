package com.example.newsfeedcm1.localdb

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.newsfeedcm1.localdb.News
import com.example.newsfeedcm1.localdb.NewsDao

class NewsRepository(private val newsDao: NewsDao) {
    val readAllData: LiveData<List<News>> = newsDao.readAll()

    suspend fun addNews(news: News){
        newsDao.insert(news)
    }
}