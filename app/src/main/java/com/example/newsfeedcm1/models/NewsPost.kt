package com.example.newsfeedcm1.models

import org.json.JSONObject

class NewsPost {
    var title: String? = null
    var description: String? = null
    var urlToImage: String? = null
    var url: String? = null

    constructor(title: String?, description: String?, urlToImage: String?, url: String?) {
        this.title = title
        this.description = description
        this.urlToImage = urlToImage
        this.url = url
    }

    constructor()


    fun toJson(): JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put("title", title)
        jsonObject.put("description", description)
        jsonObject.put("urlToImage", urlToImage)
        jsonObject.put("url", url)

        return jsonObject
    }

    companion object {
        fun fromJson(jsonObject: JSONObject): NewsPost {
            val newsPost = NewsPost()
            newsPost.title = jsonObject.getString("title")
            newsPost.description = jsonObject.getString("description")
            newsPost.urlToImage = jsonObject.getString("urlToImage")
            newsPost.url = jsonObject.getString("url")

            return newsPost
        }
    }

}