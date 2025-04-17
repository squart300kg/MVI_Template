package kr.co.architecture.feature.first

import kr.co.architecture.feature.first.FirstViewModel
import kr.co.testing.TestRepository
import kr.co.test.testing.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule

class FirstViewModelTest {
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: FirstViewModel

  private val newsRepository = TestRepository()

  @Before
  fun setup() {
    viewModel = FirstViewModel(
      repository = newsRepository
    )
  }

}