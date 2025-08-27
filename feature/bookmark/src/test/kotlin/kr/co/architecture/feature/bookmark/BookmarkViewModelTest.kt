package kr.co.architecture.feature.bookmark

import kr.co.architecture.feature.search.HomeViewModel
import kr.co.testing.TestRepository
import kr.co.architecture.test.testing.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule

class BookmarkViewModelTest {
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: HomeViewModel

  private val newsRepository = TestRepository()

  @Before
  fun setup() {
    viewModel = HomeViewModel(
      repository = newsRepository
    )
  }

}