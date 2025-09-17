package kr.co.architecture.core.network.constants

object ApiConstants {
  const val AUTHORIZATION = "Authorization"
  const val KAKAO_AK = "KakaoAK"

  object Path {
    const val V2_SEARCH_IMAGE = "v2/search/image"
    const val V2_SEARCH_VCLIP = "v2/search/vclip"
  }

  object Query {
    object Key {
      const val QUERY = "query"
      const val SORT = "sort"
      const val PAGE = "page"
      const val SIZE = "size"
    }
    object Value {
      const val DEFAULT_SIZE = 10
      const val DEFAULT_SORT = "recency"
    }
  }
}