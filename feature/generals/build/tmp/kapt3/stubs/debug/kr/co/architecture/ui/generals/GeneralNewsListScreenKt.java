package kr.co.architecture.ui.generals;

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi;
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.tooling.preview.PreviewParameter;
import com.airbnb.lottie.compose.LottieCompositionSpec;
import kr.co.architecture.model.SearchInType;
import kr.co.architecture.model.model.UiResult;
import kr.co.architecture.generals.R;
import kr.co.architecture.core.model.GeneralNewsListUiModel;
import kr.co.architecture.model.SearchSortedType;
import kr.co.architecture.core.util.DevicePreviews;
import kr.co.architecture.core.util.NewsListUiModelPreviewParameterProvider;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a0\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007\u001a\u0086\u0001\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\u0014\b\u0002\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\u0014\b\u0002\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00010\u00052\u0014\b\u0002\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a\u0012\u0010\u0013\u001a\u00020\u00012\b\b\u0001\u0010\u0014\u001a\u00020\u000bH\u0007\u00a8\u0006\u0015"}, d2 = {"GeneralNewsListScreen", "", "modifier", "Landroidx/compose/ui/Modifier;", "onSnackBarStateChanged", "Lkotlin/Function1;", "", "viewModel", "Lkr/co/architecture/ui/generals/GeneralNewsListViewModel;", "newsUiState", "Lkr/co/architecture/model/model/UiResult;", "Lkr/co/architecture/core/model/GeneralNewsListUiModel;", "onScrollToEnd", "Lkotlin/Function0;", "onSearchQueryChanged", "onChangeSearchInType", "Lkr/co/architecture/model/SearchInType;", "onChangeSearchSortedType", "Lkr/co/architecture/model/SearchSortedType;", "HomeScreenPreview", "newsListUiModels", "generals_debug"})
public final class GeneralNewsListScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void GeneralNewsListScreen(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSnackBarStateChanged, @org.jetbrains.annotations.NotNull()
    kr.co.architecture.ui.generals.GeneralNewsListViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi.class})
    @androidx.compose.runtime.Composable()
    public static final void GeneralNewsListScreen(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kr.co.architecture.model.model.UiResult<kr.co.architecture.core.model.GeneralNewsListUiModel> newsUiState, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSnackBarStateChanged, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onScrollToEnd, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSearchQueryChanged, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kr.co.architecture.model.SearchInType, kotlin.Unit> onChangeSearchInType, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kr.co.architecture.model.SearchSortedType, kotlin.Unit> onChangeSearchSortedType) {
    }
    
    @kr.co.architecture.core.util.DevicePreviews()
    @androidx.compose.runtime.Composable()
    public static final void HomeScreenPreview(@androidx.compose.ui.tooling.preview.PreviewParameter(provider = kr.co.architecture.core.util.NewsListUiModelPreviewParameterProvider.class)
    @org.jetbrains.annotations.NotNull()
    kr.co.architecture.core.model.GeneralNewsListUiModel newsListUiModels) {
    }
}