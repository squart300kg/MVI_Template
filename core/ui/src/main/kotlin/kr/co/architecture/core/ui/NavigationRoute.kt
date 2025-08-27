package kr.co.architecture.core.ui

import kotlinx.serialization.Serializable
import kr.co.architecture.core.router.internal.navigator.Route

@Serializable
data class DetailRoute(val id: String, val name: String): Route

@Serializable
data object FirstRoute : Route

@Serializable
data object SecondRoute: Route