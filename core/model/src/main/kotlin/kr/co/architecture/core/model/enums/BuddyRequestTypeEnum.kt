package kr.co.architecture.core.model.enums

enum class BuddyRequestTypeEnum(val code: String) {
    RECEIVE("BR0201"),
    SEND("BR0202"),
    NONE("");

    companion object {
        @JvmStatic
        fun creator(value: String?): BuddyRequestTypeEnum =
            BuddyRequestTypeEnum.entries.find { it.code == value } ?: NONE
    }
}