package com.example.newsfeedcm1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.newsfeedcm1.localdb.News
import com.example.newsfeedcm1.localdb.NewsViewModel
import com.example.newsfeedcm1.models.NewsPost
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class NewsWebView : AppCompatActivity() {


    var newsPost: NewsPost? = null
    var url: String? = null
    var titleArticle: String? = null

    private lateinit var mNewsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_web_view)
        val saveNewsBtn = findViewById<FloatingActionButton>(R.id.saveNewsBtn)

        mNewsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        val bundle = intent.extras
        bundle?.let {
            url = it.getString("url")
            titleArticle = it.getString("title")
        }

        val newsPostString = intent.getStringExtra(EXTRA_NEWS)

        newsPost = NewsPost.fromJson(JSONObject(newsPostString!!))
        title = newsPost!!.title
        url = newsPost!!.url
        titleArticle = newsPost!!.title
        val webView = findViewById<WebView>(R.id.webViewNews)
        newsPost!!.url?.let {
            webView.loadUrl(it)
        }


        saveNewsBtn.setOnClickListener {
            insertDataToDatabase()
        }

    }

    private fun insertDataToDatabase() {
        mNewsViewModel.insertNews(News(url!!, newsPost!!.toJson().toString(), titleArticle!!))
        Toast.makeText(this, "News successfully added", Toast.LENGTH_LONG).show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_news, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, titleArticle)
            i.putExtra(Intent.EXTRA_TEXT, url)
            startActivity(Intent.createChooser(i, "Share: "))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val EXTRA_NEWS = "NEWS"
    }
}