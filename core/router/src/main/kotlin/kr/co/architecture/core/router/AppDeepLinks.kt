package kr.co.architecture.core.router

import android.net.Uri
import kr.co.architecture.core.router.AppDeepLinks.Detail.ArgsKey.ORIGIN
import kr.co.architecture.core.router.AppDeepLinks.Detail.ArgsKey.ID

object AppDeepLinks {
  private const val SCHEME = "custom_kb_app"
  private const val HOST = "feature.detail"
  private const val PATH = "view"

  object Detail {
    object ArgsKey {
      const val ID = "id"
      const val ORIGIN = "origin"
    }
    enum class Origin {
      BOOKMARK, SEARCH
    }
    data class Args(
      val id: String,
      val origin: Origin
    )

    fun build(args: Args): String =
      Uri.Builder()
        .scheme(SCHEME)
        .authority(HOST)
        .appendPath(PATH)
        .appendQueryParameter(ID, args.id)
        .appendQueryParameter(ORIGIN, args.origin.name)
        .build()
        .toString()
  }
}