package kr.co.architecture.ui;

import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import kr.co.architecture.model.model.UiResult;
import kr.co.architecture.repository.NewsRepository;
import kr.co.architecture.repository.model.NewsSourcesDataModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lkr/co/architecture/ui/NewsCompanyViewModel;", "Landroidx/lifecycle/ViewModel;", "newsRepository", "Lkr/co/architecture/repository/NewsRepository;", "(Lkr/co/architecture/repository/NewsRepository;)V", "newsSources", "Lkotlinx/coroutines/flow/StateFlow;", "Lkr/co/architecture/model/model/UiResult;", "Lkr/co/architecture/ui/NewsSourcesUiModel;", "getNewsSources", "()Lkotlinx/coroutines/flow/StateFlow;", "news-company_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class NewsCompanyViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kr.co.architecture.repository.NewsRepository newsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<kr.co.architecture.model.model.UiResult<kr.co.architecture.ui.NewsSourcesUiModel>> newsSources = null;
    
    @javax.inject.Inject()
    public NewsCompanyViewModel(@org.jetbrains.annotations.NotNull()
    kr.co.architecture.repository.NewsRepository newsRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<kr.co.architecture.model.model.UiResult<kr.co.architecture.ui.NewsSourcesUiModel>> getNewsSources() {
        return null;
    }
}