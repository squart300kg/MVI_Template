package kr.co.architecture.ui

import kr.co.architecture.ui.first.FirstViewModel
import kr.co.testing.TestRepository
import kr.co.testing.util.MainDispatcherRule
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