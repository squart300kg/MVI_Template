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

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001c\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"convertUiModel", "Lkr/co/architecture/core/model/GeneralNewsListUiModel;", "newsList", "", "Lkr/co/architecture/repository/model/NewsListDataModel$News;", "query", "Lkr/co/architecture/model/GeneralNewsTypeQuery;", "generals_debug"})
public final class GeneralNewsListViewModelKt {
    
    @org.jetbrains.annotations.NotNull()
    public static final kr.co.architecture.core.model.GeneralNewsListUiModel convertUiModel(@org.jetbrains.annotations.NotNull()
    java.util.List<kr.co.architecture.repository.model.NewsListDataModel.News> newsList, @org.jetbrains.annotations.NotNull()
    kr.co.architecture.model.GeneralNewsTypeQuery query) {
        return null;
    }
}