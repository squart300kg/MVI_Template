package kr.co.architecture.yeo.feature.search.fake

import kr.co.architecture.yeo.core.domain.usecase.ToggleBookmarkUseCase

class FakeToggleBookmarkUseCase : ToggleBookmarkUseCase {
  var latestClickedParams: ToggleBookmarkUseCase.Params? = null
  override suspend fun invoke(params: ToggleBookmarkUseCase.Params) {
    latestClickedParams = params
  }
}