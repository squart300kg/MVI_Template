package kr.co.architecture.custom.http.client

import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.HTTPS
import kr.co.architecture.custom.http.client.model.Address
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import javax.net.ssl.SNIHostName
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

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

  fun acquire(
    address: Address,
    maxRetryWhenConnectTimeout: Int,
    connectTimeoutMs: Int,
    readTimeoutMs: Int
  ): Socket {
    var acquired: Socket? = null
    val willCloseSocket = mutableListOf<Socket>()

    synchronized(this) {
      val socketQueue = socketsPerAddress[address]
      while (socketQueue?.isNotEmpty() == true) {
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
    return acquired ?: getSocket(
      address = address,
      maxRetryWhenConnectTimeout = maxRetryWhenConnectTimeout,
      connectTimeoutMs = connectTimeoutMs,
      readTimeoutMs = readTimeoutMs
    )
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

  private fun getSocket(
    address: Address,
    timeoutRetryCount: Int = 0,
    maxRetryWhenConnectTimeout: Int,
    connectTimeoutMs: Int,
    readTimeoutMs: Int
  ): Socket {
    val port = address.port
    val inetSocketAddress = InetSocketAddress(address.host, port)

    val ssl = (SSLSocketFactory.getDefault().createSocket() as SSLSocket)
    return try {
      ssl.apply {
        soTimeout = readTimeoutMs
        connect(inetSocketAddress, connectTimeoutMs)
        sslParameters = ssl.sslParameters.apply {
          serverNames = listOf(SNIHostName(address.host))
          endpointIdentificationAlgorithm = HTTPS
        }
        startHandshake()
      }
    } catch (e: SocketTimeoutException) {
      runCatching { ssl.close() }
      if (timeoutRetryCount < maxRetryWhenConnectTimeout) {
        getSocket(
          address = address,
          timeoutRetryCount = timeoutRetryCount + 1,
          maxRetryWhenConnectTimeout = maxRetryWhenConnectTimeout,
          connectTimeoutMs = connectTimeoutMs,
          readTimeoutMs = readTimeoutMs
        )
      }
      else throw e
    } catch (e: Exception) {
      runCatching { ssl.close() }
      throw e
    }
  }
}