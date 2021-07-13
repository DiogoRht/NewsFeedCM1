package com.example.newsfeedcm1.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
class News(@field:PrimaryKey var url: String, var jsonString: String?, var title: String) {
}