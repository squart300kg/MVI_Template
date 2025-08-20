package kr.co.architecture.core.model.enums

enum class AlimCenterTypeEnum {
  ALL,
  ACTIVITY,
  NOTICE,
  TRADING;

  companion object {
    fun creator(value: String?): AlimCenterTypeEnum =
      AlimCenterTypeEnum.entries.firstOrNull { it.name == value } ?: ALL
  }
}