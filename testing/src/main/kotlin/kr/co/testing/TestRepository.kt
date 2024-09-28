package kr.co.testing

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.repository.DataModel
import kr.co.architecture.repository.Repository

class TestRepository : Repository {
    override val list: Flow<Result<DataModel>>
        get() = TODO("Not yet implemented")


}