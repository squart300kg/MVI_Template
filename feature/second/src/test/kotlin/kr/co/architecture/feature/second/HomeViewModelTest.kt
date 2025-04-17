package kr.co.architecture.feature.second

import kr.co.architecture.feature.home.HomeViewModel
import kr.co.testing.TestRepository
import kr.co.test.testing.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule

class HomeViewModelTest {
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