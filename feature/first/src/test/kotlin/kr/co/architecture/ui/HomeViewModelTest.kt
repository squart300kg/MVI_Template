package kr.co.architecture.ui

import kr.co.architecture.ui.first.HomeViewModel
import kr.co.testing.TestRepository
import kr.co.testing.util.MainDispatcherRule
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