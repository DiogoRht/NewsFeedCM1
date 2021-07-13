package com.example.newsfeedcm1

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsfeedcm1.models.NewsPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class NewsApiManager {
    companion object {
        val GENERAL = "general"
        val ECONOMY = "business"
        val SCIENCE = "science"
        val SPORTS = "sports"

        private const val BASE_API = "https://newsapi.org/v2/"
        private const val PATH = "top-headlines?country=pt"
        private const val CATEGORY_PATH = "&category="
        private const val API_KEY = "&apiKey=f006afac297e4dc4af96bae9e1c31db7"

        var cachedGeneralNews: ArrayList<NewsPost>? = null
        var cachedEconomyNews: ArrayList<NewsPost>? = null
        var cachedSportsNews: ArrayList<NewsPost>? = null
        var cachedScienceNews: ArrayList<NewsPost>? = null


        fun getTopHeadlinesNews(
            category: String,
            onFinish: (Boolean, ArrayList<NewsPost>?) -> Unit
        ) {
            if (category == GENERAL && cachedGeneralNews != null) {
                onFinish(true, cachedGeneralNews)
                return
            } else if (category == SCIENCE && cachedScienceNews != null) {
                onFinish(true, cachedScienceNews)
                return
            } else if (category == SPORTS && cachedSportsNews != null) {
                onFinish(true, cachedSportsNews)
                return
            } else if (category == ECONOMY && cachedEconomyNews != null) {
                onFinish(true, cachedEconomyNews)
                return
            }

            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()
                val request =
                    Request.Builder().url(BASE_API + PATH + CATEGORY_PATH + category + API_KEY)
                        .build()

                client.newCall(request).enqueue(object : Callback {

                    override fun onFailure(call: Call, e: IOException) {
                        GlobalScope.launch(Dispatchers.Main) { onFinish(false, null) }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.body?.let {
                            val newsPostArray: ArrayList<NewsPost> = arrayListOf()
                            val newsPostJsonArray =
                                JSONObject(it.string()).getJSONArray("articles")

                            for (i: Int in 0 until newsPostJsonArray.length()) {
                                newsPostArray.add(NewsPost.fromJson(newsPostJsonArray.getJSONObject(i)))
                            }

                            when (category) {
                                SCIENCE -> {
                                    cachedScienceNews = newsPostArray
                                }
                                GENERAL -> {
                                    cachedGeneralNews = newsPostArray
                                }
                                SPORTS -> {
                                    cachedSportsNews = newsPostArray
                                }
                                ECONOMY -> {
                                    cachedEconomyNews = newsPostArray
                                }
                            }
                             GlobalScope.launch(Dispatchers.Main) { onFinish.invoke(true, newsPostArray) }
                        } ?: run {
                            GlobalScope.launch(Dispatchers.Main) { onFinish.invoke(false, null) }
                        }
                    }
                })

            }


        }

        fun getImageFromUrl(url: String, imageView: ImageView) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val input = URL(url).openStream()
                    val bitmap = BitmapFactory.decodeStream(input)
                    GlobalScope.launch(Dispatchers.Main) {
                        imageView.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                    GlobalScope.launch(Dispatchers.Main) {
                        imageView.setImageResource(R.mipmap.ic_launcher)
                    }
                }

            }
        }
    }
}