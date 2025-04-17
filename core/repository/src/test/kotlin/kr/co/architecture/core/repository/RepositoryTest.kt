package kr.co.architecture.core.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.repository.RepositoryImpl
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryTest {

  private val testScope = TestScope(UnconfinedTestDispatcher())

  private lateinit var repository: RepositoryImpl

  private lateinit var remoteApi: RemoteApi

  @Before
  fun setup() {

  }

}