package kr.co.architecture.feature.search.fake

import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase

class FakeToggleBookmarkUseCase : ToggleBookmarkUseCase {
  var lastParams: ToggleBookmarkUseCase.Params? = null
  override suspend fun invoke(params: ToggleBookmarkUseCase.Params) { lastParams = params }
}