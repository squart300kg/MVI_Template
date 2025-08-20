package kr.co.architecture.core.ui.enums

import androidx.compose.ui.graphics.vector.ImageVector
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiAncious
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiAnt
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiCool
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiDepressed
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiDevil
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiFire
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiGlasses
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiHaapy
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiHeart
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiHeartEyes
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiPleasant
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiPoo
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiSad
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiSick
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiSkull
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiSmile
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiUnpleasant
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiWater

enum class EmojiEnum(
  val imageVector: ImageVector?
) {
  EC0101(IcEmojiPleasant),
  EC0102(IcEmojiSmile),
  EC0103(IcEmojiHeartEyes),
  EC0104(IcEmojiCool),
  EC0105(IcEmojiHaapy),
  EC0106(IcEmojiGlasses),
  EC0107(IcEmojiUnpleasant),
  EC0108(IcEmojiAncious),
  EC0109(IcEmojiSick),
  EC0110(IcEmojiDevil),
  EC0111(IcEmojiDepressed),
  EC0112(IcEmojiSad),
  EC0113(IcEmojiAnt),
  EC0114(IcEmojiHeart),
  EC0115(IcEmojiPoo),
  EC0116(IcEmojiFire),
  EC0117(IcEmojiWater),
  EC0118(IcEmojiSkull),
  NONE(null);

  companion object {
    @JvmStatic
    fun creator(value: String?): EmojiEnum {
      return if (value.isNullOrEmpty()) {
        EC0101
      } else {
        values().firstOrNull { it.name == value } ?: NONE
      }
    }
  }
}
