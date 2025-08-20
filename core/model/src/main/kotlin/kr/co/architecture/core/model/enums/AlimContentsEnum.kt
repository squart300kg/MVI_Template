package kr.co.architecture.core.model.enums

enum class AlimContentsEnum {
  NONE,
  NOTICE,
  ADVERTISE,
  PROMOTION,
  TRADING,
  BUDDY,
  SUB_COMMENT,
  COMMENT;

  companion object {
    fun creator(contentsType: String): AlimContentsEnum {
      return AlimContentsEnum.entries
        .firstOrNull { it.name == contentsType } ?: NONE
    }
  }
}