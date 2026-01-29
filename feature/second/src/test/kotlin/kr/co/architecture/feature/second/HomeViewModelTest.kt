package kr.co.architecture.feature.second

import kr.co.architecture.core.domain.GetListUseCase
import kr.co.architecture.core.domain.repository.ArticleRepository
import kr.co.architecture.core.model.Article
import kr.co.architecture.test.testing.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SecondViewModelTest {
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: SecondViewModel

  private val repository = object : ArticleRepository {
    override suspend fun getList(): List<Article> = emptyList()
  }

  @Before
  fun setup() {
    viewModel = SecondViewModel(
      getListUseCase = GetListUseCase(repository)
    )
  }

  @Test
  fun createViewModel() {
    // construction smoke test
  }

}