package kr.co.architecture.core.network.operator

data class CommonApiResponse<out T> (
    val code: Int,
    val message: String,
    val data: T?
)