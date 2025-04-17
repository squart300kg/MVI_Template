package kr.co.architecture.core.model

data class ArchitectureSampleHttpException(
  val code: Int,
  override val message: String
) : Exception(message)