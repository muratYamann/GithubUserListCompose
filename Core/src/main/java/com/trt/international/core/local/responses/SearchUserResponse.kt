package com.trt.international.core.local.responses


import com.google.gson.annotations.SerializedName
import com.trt.international.core.local.responses.UserSearchResponseItem

data class SearchUserResponse(
    @SerializedName("items")
    val userItems: List<UserSearchResponseItem>?,
    @SerializedName("total_count")
    val totalCount: Int?
)