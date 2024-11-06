package kr.co.architecture.core.repository.dto

import kr.co.architecture.core.network.model.ArticleResponse

data class ArticleDto(
    val name: String
) {
    companion object {
        fun mapperToDto(article: List<ArticleResponse>) =
            article.map { ArticleDto(it.title) }
    }
}

