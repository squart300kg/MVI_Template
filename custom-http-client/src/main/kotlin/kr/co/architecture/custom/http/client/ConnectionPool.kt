package kr.co.architecture.custom.http.client

import kr.co.architecture.custom.http.client.model.Address
import java.net.Socket

class ConnectionPool private constructor(
  private val maxIdleSocketCountPerHost: Int,
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
          maxIdleSocketCountPerHost = maxIdlePerHost,
          idleTimeoutMs = idleTimeoutMs
        ).also { INSTANCE = it }
      }
    }
  }

  private val socketsPerAddress = mutableMapOf<Address, ArrayDeque<Pair<Long, Socket>>>()

  fun acquire(address: Address): Socket? {
    var acquired: Socket? = null
    val willCloseSocket = mutableListOf<Socket>()

    synchronized(this) {
      val socketQueue = socketsPerAddress[address] ?: return null
      while (socketQueue.isNotEmpty()) {
        val (sinceTimeMillis, socket) = socketQueue.removeFirst()
        val alive =
          !socket.isClosed &&
            !socket.isInputShutdown &&
            !socket.isOutputShutdown &&
            System.currentTimeMillis() - sinceTimeMillis <= idleTimeoutMs

        if (alive) { acquired = socket; break }
        else willCloseSocket += socket
      }
    }
    willCloseSocket.forEach { runCatching { it.close() } }
    return acquired
  }

  fun release(address: Address, socket: Socket) {
    var willCloseSocket: Socket? = null
    synchronized(this) {
      val socketQueue = socketsPerAddress.getOrPut(address) { ArrayDeque() }
      if (socketQueue.size >= maxIdleSocketCountPerHost) willCloseSocket = socket
      else socketQueue.addLast(System.currentTimeMillis() to socket)
    }
    willCloseSocket?.let { runCatching { it.close() } }
  }
}