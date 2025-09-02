//package kr.co.architecture.core.domain
//
//import kr.co.architecture.core.repository.PicsumImageRepository
//import javax.inject.Inject
//
//class GetListUseCase @Inject constructor(
//  private val repository: PicsumImageRepository
//) {
//  suspend operator fun invoke(): List<String> {
//    return repository.getPicsumImages()
//      .map { it.name }
//  }
//}