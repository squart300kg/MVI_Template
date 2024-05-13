package kr.co.architecture.ui;

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
public final class NewsCompanyViewModel_Factory implements Factory<NewsCompanyViewModel> {
  private final Provider<NewsRepository> newsRepositoryProvider;

  public NewsCompanyViewModel_Factory(Provider<NewsRepository> newsRepositoryProvider) {
    this.newsRepositoryProvider = newsRepositoryProvider;
  }

  @Override
  public NewsCompanyViewModel get() {
    return newInstance(newsRepositoryProvider.get());
  }

  public static NewsCompanyViewModel_Factory create(
      Provider<NewsRepository> newsRepositoryProvider) {
    return new NewsCompanyViewModel_Factory(newsRepositoryProvider);
  }

  public static NewsCompanyViewModel newInstance(NewsRepository newsRepository) {
    return new NewsCompanyViewModel(newsRepository);
  }
}
