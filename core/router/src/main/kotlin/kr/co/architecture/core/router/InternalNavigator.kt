package kr.co.architecture.core.router

import kotlinx.coroutines.channels.Channel

internal interface InternalNavigator {

  val channel: Channel<InternalRoute>

}