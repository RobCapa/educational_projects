package ru.aston.fragments.category_news.main;

import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import moxy.MvpPresenter;
import ru.aston.data.Filters;
import ru.aston.data.News;
import ru.aston.data.NewsCategory;
import ru.aston.interfaces.mvp_views.CategoryNewsView;
import ru.aston.interfaces.repositories.FiltersRepository;
import ru.aston.interfaces.repositories.NewsRepository;

public class CategoryNewsPresenter extends MvpPresenter<CategoryNewsView> {

    NewsRepository newsRepository;

    FiltersRepository filtersRepository;

    Handler UIHandler = new Handler(Looper.getMainLooper());

    @Inject
    public CategoryNewsPresenter(
            NewsRepository newsRepository,
            FiltersRepository filtersRepository
    ) {
        this.newsRepository = newsRepository;
        this.filtersRepository = filtersRepository;
    }

    public void findCategoryNews(
            String searchCriteria,
            NewsCategory newsCategory
    ) {
        Filters filters = filtersRepository.getFilters();

        CompletableFuture<List<News>> future;

        if (filtersAreNotSuitableForCategory()) {
            future = newsRepository.getNewsWithFiltersFuture(
                    filters,
                    searchCriteria,
                    NewsRepository.ALL_PAGE,
                    NewsRepository.ALL_PAGE
            );
        } else {
            future = newsRepository.getNewsByCategoryFuture(
                    filters,
                    newsCategory,
                    searchCriteria,
                    NewsRepository.ALL_PAGE,
                    NewsRepository.ALL_PAGE
            );
        }

        future.handleAsync((result, exception) -> {

            UIHandler.post(() -> {
                if (exception == null) getViewState().showFoundNews(result);
                else getViewState().showFoundNews(List.of());
                UIHandler.removeCallbacksAndMessages(null);
            });

            return null;
        });
    }

    public void checkIfFiltersSuitableForCategory() {
        getViewState().hideOrShowNewsWithFilters(filtersAreNotSuitableForCategory());
    }

    public void clearSearchResult() {
        getViewState().showFoundNews(List.of());
    }

    boolean filtersAreNotSuitableForCategory() {
        Filters filters = filtersRepository.getFilters();
        return filters.getDates() != null || filters.getOrderBy() != null;
    }
}
