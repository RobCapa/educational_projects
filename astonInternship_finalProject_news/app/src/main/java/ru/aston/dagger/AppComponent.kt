package ru.aston.dagger

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.aston.activities.main.MainActivity
import ru.aston.dagger.modules.FiltersRepositoryModule
import ru.aston.dagger.modules.NewsRepositoryModule
import ru.aston.dagger.modules.ViewModelFactoryModule
import ru.aston.fragments.category_news.list.CategoryNewsListPresenter
import ru.aston.fragments.category_news.main.CategoryNewsPresenter
import ru.aston.fragments.filters.FiltersFragment
import ru.aston.fragments.news_details.NewsDetailsFragment
import ru.aston.fragments.saved_news.SavedNews
import ru.aston.fragments.source_news.SourceNewsFragment
import ru.aston.fragments.sources.SourcesFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelFactoryModule::class,
        NewsRepositoryModule::class,
        FiltersRepositoryModule::class,
    ]
)
interface AppComponent {

    fun inject(frag: SourcesFragment)
    fun inject(frag: SourceNewsFragment)
    fun inject(frag: SavedNews)
    fun inject(frag: NewsDetailsFragment)
    fun inject(frag: FiltersFragment)
    fun inject(act: MainActivity)

    fun provideCategoryNewsPresenter(): CategoryNewsPresenter
    fun provideCategoryNewsListPresenter(): CategoryNewsListPresenter

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}