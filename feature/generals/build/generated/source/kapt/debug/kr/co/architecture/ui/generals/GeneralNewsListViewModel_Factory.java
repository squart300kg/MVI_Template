package kr.co.architecture.ui.generals;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kr.co.architecture.repository.NewsRepository;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class GeneralNewsListViewModel_Factory implements Factory<GeneralNewsListViewModel> {
  private final Provider<NewsRepository> newsRepositoryProvider;

  public GeneralNewsListViewModel_Factory(Provider<NewsRepository> newsRepositoryProvider) {
    this.newsRepositoryProvider = newsRepositoryProvider;
  }

  @Override
  public GeneralNewsListViewModel get() {
    return newInstance(newsRepositoryProvider.get());
  }

  public static GeneralNewsListViewModel_Factory create(
      Provider<NewsRepository> newsRepositoryProvider) {
    return new GeneralNewsListViewModel_Factory(newsRepositoryProvider);
  }

  public static GeneralNewsListViewModel newInstance(NewsRepository newsRepository) {
    return new GeneralNewsListViewModel(newsRepository);
  }
}
