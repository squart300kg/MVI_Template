package kr.co.architecture.feature.first

import kr.co.architecture.core.domain.GetListUseCase
import kr.co.architecture.core.domain.repository.ArticleRepository
import kr.co.architecture.core.model.Article
import kr.co.architecture.test.testing.util.MainDispatcherRule
import org.junit.Assert.assertEquals
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

  @Test
  fun `mapperToUi는 서버 문자열을 raw String으로 유지한다`() {
    val uiModels = UiModel.mapperToUi(listOf("server name"))

    assertEquals(
      "server name",
      uiModels.first().name
    )
  }
}
