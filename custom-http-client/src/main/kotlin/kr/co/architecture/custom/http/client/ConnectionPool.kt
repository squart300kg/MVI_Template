package kr.co.architecture.custom.http.client

import kr.co.architecture.custom.http.client.model.Address
import java.net.Socket

class ConnectionPool private constructor(
  private val maxIdlePerHost: Int,
  private val idleTimeoutMs: Long
) {
  companion object {
    @Volatile
    private var INSTANCE: ConnectionPool? = null

    @JvmStatic
    fun getInstance(
      maxIdlePerHost: Int = 6,
      idleTimeoutMs: Long = 15_000
    ): ConnectionPool {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: ConnectionPool(
          maxIdlePerHost = maxIdlePerHost,
          idleTimeoutMs = idleTimeoutMs
        ).also { INSTANCE = it }
      }
    }
  }

  private val map = mutableMapOf<Address, ArrayDeque<Pair<Long, Socket>>>()

  @Synchronized
  fun acquire(address: Address): Socket? {
    val socketQueue = map[address] ?: return null
    while (socketQueue.isNotEmpty()) {
      val (sinceTimeMillis, socket) = socketQueue.removeFirst()
      val alive =
        !socket.isClosed &&
        !socket.isInputShutdown &&
        !socket.isOutputShutdown &&
        System.currentTimeMillis() - sinceTimeMillis <= idleTimeoutMs
      if (alive) return socket
      runCatching { socket.close() }
    }
    return null
  }

  @Synchronized
  fun release(address: Address, socket: Socket) {
    val socketQueue = map.getOrPut(address) { ArrayDeque() }
    if (socketQueue.size >= maxIdlePerHost) { runCatching { socket.close() }; return }
    socketQueue.addLast(System.currentTimeMillis() to socket)
  }
}