package kr.co.architecture.ui.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kr.co.architecture.core.model.UiResult
import kr.co.architecture.repository.DataModel
import kr.co.architecture.repository.Repository
import javax.inject.Inject

data class UiModel(
    val item: List<Item>
) {
    data class Item(
        val name: String
    )
}

fun convertUiItem(item: DataModel.Item) =
    UiModel.Item(
        name = item.name
    )

@HiltViewModel
class SecondViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    val list = repository.list
        .map { it.getOrThrow().item.map(::convertUiItem).let(::UiModel) }
        .map<UiModel, UiResult<UiModel>>{ UiResult.Success(it) }
        .catch { emit(UiResult.Error(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UiResult.Empty
        )

}