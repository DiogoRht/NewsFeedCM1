package com.example.newsfeedcm1.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsfeedcm1.NewsApiManager
import com.example.newsfeedcm1.NewsWebView
import com.example.newsfeedcm1.R
import com.example.newsfeedcm1.models.NewsPost


class SportsNewsFragment : Fragment() {
    private var sportsNews: ArrayList<NewsPost> = arrayListOf()

    var newsAdapter = NewsAdapter()
    private var mLayoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sports_news, container, false)

        NewsApiManager.getTopHeadlinesNews(NewsApiManager.SPORTS) { wasSuccessful, newsPosts ->
            if (wasSuccessful) {
                sportsNews = newsPosts!!
                newsAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Getting News error", Toast.LENGTH_LONG).show()
            }
        }

        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerViewSportsNews)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = newsAdapter

        return root
    }

    inner class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.PostViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.row_news_post, parent, false)
            return PostViewHolder(view)
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            val curNews = sportsNews[position]

            holder.newsTitle.text = curNews.title
            holder.newsDescription.text = curNews.description

            curNews.urlToImage?.let {
                NewsApiManager.getImageFromUrl(it, holder.imageViewNews)
            }

            holder.layoutRowNews.setOnClickListener {
                val intent = Intent(context, NewsWebView::class.java)
                intent.putExtra( NewsWebView.EXTRA_NEWS , sportsNews[position].toJson().toString())
                startActivity(intent)
            }
        }

        inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val newsTitle: TextView = itemView.findViewById(R.id.textViewTitle)
            val newsDescription: TextView = itemView.findViewById(R.id.textViewDescription)
            val imageViewNews: ImageView = itemView.findViewById(R.id.imageViewNews)
            val layoutRowNews: ConstraintLayout = itemView.findViewById(R.id.layoutRowNews)
        }

        override fun getItemCount() = sportsNews.size

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }
    }

}