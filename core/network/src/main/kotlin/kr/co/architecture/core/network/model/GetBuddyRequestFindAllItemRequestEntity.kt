package kr.co.architecture.core.network.model

import com.google.gson.JsonObject

data class GetBuddyRequestFindAllItemRequestEntity(
    val buddyRequestType: String,
    val phoneNums: JsonObject,
    val updateFlag: Boolean,
    val userId: Int,
    val page: Int
)