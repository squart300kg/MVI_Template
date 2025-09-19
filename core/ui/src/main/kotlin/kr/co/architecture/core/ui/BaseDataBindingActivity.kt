package kr.co.architecture.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

/**
 * ViewSystem + DataBinding + MVI(ViewModel) 공통 베이스.
 *
 * VB : ViewDataBinding 타입 (ex: ActivityDetailBinding)
 * S  : UiState
 * E  : UiEvent
 * SE : UiSideEffect
 * VM : BaseViewModel<S, E, SE>
 */
abstract class BaseDataBindingActivity<
  VB : ViewDataBinding,
  S : UiState,
  E : UiEvent,
  SE : UiSideEffect,
  VM : BaseViewModel<S, E, SE>
  > : ComponentActivity() {

  protected lateinit var binding: VB

  /** ex) R.layout.activity_detail */
  @get:LayoutRes
  protected abstract val layoutResId: Int

  /** DataBinding 변수 id (ex: BR.vm). 레이아웃에 `<variable name="vm" .../>` 있어야 함 */
  protected abstract val variableId: Int

  /** 화면 ViewModel */
  protected abstract val viewModel: VM

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // 1) DataBinding 세팅
    @Suppress("UNCHECKED_CAST")
    binding = DataBindingUtil.setContentView(this, layoutResId)
    binding.lifecycleOwner = this
    // 레이아웃의 variable="vm" 에 ViewModel 주입
    binding.setVariable(variableId, viewModel)

    // 2) 추가 초기화 훅
    onBindCreated(binding)

    // 3) 상태/사이드이펙트 수집 공통화
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          viewModel.uiSideEffect.collect { handleSideEffect(it) }
        }
        launch {
          viewModel.uiState.collect { renderState(it) }
        }
      }
    }
  }

  /** 바인딩 직후 1회 호출되는 훅 (인텐트 파싱 등) */
  protected open fun onBindCreated(binding: VB) = Unit

  /** 상태 랜더링 (각 화면이 구현) */
  protected abstract fun renderState(state: S)

  /** 사이드 이펙트 처리 (필요 시 오버라이드) */
  protected open fun handleSideEffect(effect: SE) = Unit
}