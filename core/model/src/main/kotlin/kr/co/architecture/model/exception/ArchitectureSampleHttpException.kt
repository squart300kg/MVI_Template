package kr.co.architecture.model.exception

data class ArchitectureSampleHttpException(
    val code: Int,
    override val message: String
): Exception(message)