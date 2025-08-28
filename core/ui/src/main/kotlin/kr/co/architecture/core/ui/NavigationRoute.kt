package kr.co.architecture.core.ui

import kotlinx.serialization.Serializable
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.router.internal.navigator.Route

@Serializable
inline class DetailRoute(val isbn: String): Route

@Serializable
data object SearchRoute : Route

@Serializable
data object BookmarkRoute: Route