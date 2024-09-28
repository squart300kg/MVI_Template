package kr.co.architecture.repository

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.network.model.Response

interface Repository {

    val list: Flow<Result<DataModel>>

}