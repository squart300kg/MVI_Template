package kr.co.architecture.core.ui

import kotlinx.serialization.Serializable
import kr.co.architecture.core.router.internal.navigator.Route

@Serializable
inline class DetailRoute(val id: String): Route
