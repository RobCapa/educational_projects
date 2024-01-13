package ru.aston.dagger.modules

import dagger.Module
import dagger.Provides
import ru.aston.activities.main.MainViewModel
import ru.aston.fragments.filters.FiltersViewModel
import ru.aston.fragments.news_details.NewsDetailsViewModel
import ru.aston.fragments.saved_news.SavedNewsViewModel
import ru.aston.fragments.source_news.SourceNewsViewModel
import ru.aston.fragments.sources.SourceViewModel
import ru.aston.utils.ViewModelFactory

@Module(
    includes = [
        NewsRepositoryModule::class,
        FiltersRepositoryModule::class,
    ]
)
class ViewModelFactoryModule {

    @Provides
    fun provideSourceViewModelFactory(
        viewModel: SourceViewModel
    ): ViewModelFactory<SourceViewModel> {
        return ViewModelFactory { viewModel }
    }

    @Provides
    fun provideFilterViewModelFactory(
        viewModel: FiltersViewModel
    ): ViewModelFactory<FiltersViewModel> {
        return ViewModelFactory {
            viewModel
        }
    }

    @Provides
    fun provideSavedNewsViewModelFactory(
        viewModel: SavedNewsViewModel
    ): ViewModelFactory<SavedNewsViewModel> {
        return ViewModelFactory { viewModel }
    }

    @Provides
    fun provideNewsDetailsViewModelFactory(
        viewModel: NewsDetailsViewModel
    ): ViewModelFactory<NewsDetailsViewModel> {
        return ViewModelFactory { viewModel }
    }

    @Provides
    fun provideSourceNewsViewModelFactory(
        viewModel: SourceNewsViewModel
    ): ViewModelFactory<SourceNewsViewModel> {
        return ViewModelFactory { viewModel }
    }

    @Provides
    fun provideMainViewModelFactory(
        viewModel: MainViewModel
    ): ViewModelFactory<MainViewModel> {
        return ViewModelFactory { viewModel }
    }
}