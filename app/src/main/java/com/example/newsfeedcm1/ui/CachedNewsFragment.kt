package com.example.newsfeedcm1.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfeedcm1.NewsApiManager
import com.example.newsfeedcm1.NewsWebView
import com.example.newsfeedcm1.R
import com.example.newsfeedcm1.localdb.News
import com.example.newsfeedcm1.localdb.NewsViewModel

class CachedNewsFragment : Fragment() {
    private lateinit var mNewsViewModel: NewsViewModel
    private var mLayoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_cached_news, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCachedNews)
        val adapter = NewsAdapter()
        recyclerView.adapter = adapter
        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager

        mNewsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        mNewsViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
        return view
    }

    inner class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.PostViewHolder>() {
        private var newsList = emptyList<News>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.row_news_post, parent, false)
            return PostViewHolder(view)
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            val curNews = newsList[position]

            holder.newsTitle.text = curNews.title
            holder.newsDescription.text = ""
            //holder.newsDescription.text = curNews.description

//            curNews.urlToImage?.let {
//                NewsApiManager.getImageFromUrl(it, holder.imageViewNews)
//            }
        }

        inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val newsTitle: TextView = itemView.findViewById(R.id.textViewTitle)
            val newsDescription: TextView = itemView.findViewById(R.id.textViewDescription)
            val imageViewNews: ImageView = itemView.findViewById(R.id.imageViewNews)
            val layoutRowNews: ConstraintLayout = itemView.findViewById(R.id.layoutRowNews)
        }

        override fun getItemCount() = newsList.size

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        fun setData(news: List<News>) {
            this.newsList = news
            notifyDataSetChanged()
        }
    }


}