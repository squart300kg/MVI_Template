package kr.co.architecture.core.domain

import javax.inject.Inject

class ChangeFromYnToBoolean @Inject constructor(
) {
  operator fun invoke(params: Params): Boolean {
    return params.yn.uppercase() == "Y"
  }
  data class Params(val yn: String)
}