package kr.co.architecture.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ActivityScenario.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
class HomeViewModel @Inject constructor(
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