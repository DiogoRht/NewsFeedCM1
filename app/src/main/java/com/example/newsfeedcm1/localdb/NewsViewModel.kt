package com.example.newsfeedcm1.localdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class NewsViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<News>>
    private val repository: NewsRepository

    init {
        val newsDao = AppDatabase.getDatabase(application).newsDao()
        repository = NewsRepository(newsDao)
        readAllData = repository.readAllData
    }

    fun insertNews(news: News) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNews(news)
        }
    }
}