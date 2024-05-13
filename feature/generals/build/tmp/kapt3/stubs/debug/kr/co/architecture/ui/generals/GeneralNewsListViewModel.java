package kr.co.architecture.ui.generals;

import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import kr.co.architecture.model.model.UiResult;
import kr.co.architecture.core.model.GeneralNewsListUiModel;
import kr.co.architecture.core.model.News;
import kr.co.architecture.model.GeneralNewsTypeQuery;
import kr.co.architecture.model.NewsType;
import kr.co.architecture.model.SearchInType;
import kr.co.architecture.model.SearchSortedType;
import kr.co.architecture.repository.NewsRepository;
import kr.co.architecture.repository.model.NewsListDataModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u0015R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0016"}, d2 = {"Lkr/co/architecture/ui/generals/GeneralNewsListViewModel;", "Landroidx/lifecycle/ViewModel;", "newsRepository", "Lkr/co/architecture/repository/NewsRepository;", "(Lkr/co/architecture/repository/NewsRepository;)V", "_generalNewsTypeQuery", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lkr/co/architecture/model/GeneralNewsTypeQuery;", "newsUiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lkr/co/architecture/model/model/UiResult;", "Lkr/co/architecture/core/model/GeneralNewsListUiModel;", "getNewsUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "getNextNewsList", "", "searchNews", "query", "", "type", "Lkr/co/architecture/model/SearchInType;", "Lkr/co/architecture/model/SearchSortedType;", "generals_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class GeneralNewsListViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kr.co.architecture.repository.NewsRepository newsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<kr.co.architecture.model.GeneralNewsTypeQuery> _generalNewsTypeQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<kr.co.architecture.model.model.UiResult<kr.co.architecture.core.model.GeneralNewsListUiModel>> newsUiState = null;
    
    @javax.inject.Inject()
    public GeneralNewsListViewModel(@org.jetbrains.annotations.NotNull()
    kr.co.architecture.repository.NewsRepository newsRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<kr.co.architecture.model.model.UiResult<kr.co.architecture.core.model.GeneralNewsListUiModel>> getNewsUiState() {
        return null;
    }
    
    public final void getNextNewsList() {
    }
    
    public final void searchNews(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void searchNews(@org.jetbrains.annotations.NotNull()
    kr.co.architecture.model.SearchInType type) {
    }
    
    public final void searchNews(@org.jetbrains.annotations.NotNull()
    kr.co.architecture.model.SearchSortedType type) {
    }
}