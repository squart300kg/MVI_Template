package kr.co.architecture.core.ui

import kotlinx.serialization.Serializable
import kr.co.architecture.core.router.internal.navigator.Route

@Serializable
data class DetailRoute(val id: String, val name: String): Route

@Serializable
data object HomeRoute : Route

@Serializable
data object BookmarkRoute: Route