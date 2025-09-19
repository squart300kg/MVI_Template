package kr.co.architecture.core.router

import android.net.Uri

object AppDeepLinks {
  private const val SCHEME = "custom_kb_app"
  private const val HOST = "feature.detail"
  private const val PATH = "view"

  object Detail {
    data class Args(
      val id: String
    )

    fun build(args: Args): String =
      Uri.Builder()
        .scheme(SCHEME)
        .authority(HOST)
        .appendPath(PATH)
        .appendQueryParameter("id", args.id)
        .build()
        .toString()
  }
}