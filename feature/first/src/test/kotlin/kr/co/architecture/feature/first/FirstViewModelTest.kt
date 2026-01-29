package kr.co.architecture.feature.first

import kr.co.architecture.core.domain.GetListUseCase
import kr.co.architecture.core.domain.repository.ArticleRepository
import kr.co.architecture.core.model.Article
import kr.co.architecture.test.testing.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirstViewModelTest {
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: FirstViewModel

  private val repository = object : ArticleRepository {
    override suspend fun getList(): List<Article> = emptyList()
  }

  @Before
  fun setup() {
    viewModel = FirstViewModel(
      getListUseCase = GetListUseCase(repository)
    )
  }

  @Test
  fun createViewModel() {
    // construction smoke test
  }

}
