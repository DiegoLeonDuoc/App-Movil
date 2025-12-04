package com.example.teamusic_grupo11.data.models

import com.google.gson.annotations.SerializedName

// API Response wrappers for YouTube Data API v3

data class SearchResponse(
    @SerializedName("kind")
    val kind: String,
    
    @SerializedName("etag")
    val etag: String,
    
    @SerializedName("nextPageToken")
    val nextPageToken: String? = null,
    
    @SerializedName("regionCode")
    val regionCode: String? = null,
    
    @SerializedName("pageInfo")
    val pageInfo: PageInfo,
    
    @SerializedName("items")
    val items: List<YouTubeSearchItem>
)

data class VideoListResponse(
    @SerializedName("kind")
    val kind: String,
    
    @SerializedName("etag")
    val etag: String,
    
    @SerializedName("items")
    val items: List<YouTubeVideoItem>,
    
    @SerializedName("pageInfo")
    val pageInfo: PageInfo
)

data class PageInfo(
    @SerializedName("totalResults")
    val totalResults: Int,
    
    @SerializedName("resultsPerPage")
    val resultsPerPage: Int
)
