package kr.co.architecture.yeo.core.ui

import kotlinx.serialization.Serializable
import kr.co.architecture.yeo.core.router.internal.navigator.Route

@Serializable
inline class DetailRoute(val isbn: String): Route

@Serializable
data object SearchRoute : Route

@Serializable
data object BookmarkRoute: Route