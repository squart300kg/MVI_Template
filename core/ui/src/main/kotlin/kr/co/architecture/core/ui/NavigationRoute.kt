package kr.co.architecture.core.ui

import kotlinx.serialization.Serializable
import kr.co.architecture.core.router.internal.navigator.Route

// TODO: 지우기
@Serializable
data class DetailRoute(val id: String, val name: String): Route

@Serializable
data object SearchRoute : Route

@Serializable
data object BookmarkRoute: Route