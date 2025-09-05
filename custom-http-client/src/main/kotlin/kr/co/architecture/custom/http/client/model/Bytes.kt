package kr.co.architecture.custom.http.client.model

data class Bytes(val data: ByteArray) {
  override fun toString() = "Bytes(${data.size} bytes)"
  override fun equals(other: Any?) = other is Bytes && data.contentEquals(other.data)
  override fun hashCode() = data.contentHashCode()
}

fun ByteArray.toBytes() = Bytes(this)
