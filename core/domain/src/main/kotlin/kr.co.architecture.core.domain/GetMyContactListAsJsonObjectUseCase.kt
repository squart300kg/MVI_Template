package kr.co.architecture.core.domain

import com.google.gson.JsonObject
import javax.inject.Inject

class GetMyContactListAsJsonObjectUseCase @Inject constructor(
) {
  suspend operator fun invoke(): JsonObject {
    return JsonObject().apply {
      addProperty("010-1111-1111", "홍길동")
      addProperty("010-2222-2222", "둘리")
      addProperty("010-3333-3333", "고길동")
    }
  }
}
