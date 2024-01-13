package ru.aston.fragments.category_news.list;

import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.aston.data.Filters;
import ru.aston.data.News;
import ru.aston.data.NewsCategory;
import ru.aston.exceptions.NoConnectionException;
import ru.aston.interfaces.mvp_views.CategoryNewsListView;
import ru.aston.interfaces.repositories.FiltersRepository;
import ru.aston.interfaces.repositories.NewsRepository;
import ru.aston.repositories.utils.DataSource;

@InjectViewState
public class CategoryNewsListPresenter extends MvpPresenter<CategoryNewsListView> {

    NewsRepository newsRepository;

    FiltersRepository filtersRepository;

    int currentPage = 1;
    int pageSize = NewsRepository.DEFAULT_PAGE_SIZE;

    Handler UIHandler = new Handler(Looper.getMainLooper());

    DataSource dataWasFrom = null;

    @Inject
    public CategoryNewsListPresenter(
            NewsRepository newsRepository,
            FiltersRepository filtersRepository
    ) {
        this.newsRepository = newsRepository;
        this.filtersRepository = filtersRepository;
    }

    public void loadCategoryNews(NewsCategory newsCategory) {
        Filters filters = filtersRepository.getFilters();

        DataSource dataWillBeFrom = newsRepository.dataGettingFrom();
        if (dataWasFrom != dataWillBeFrom) {
            resetCurrentPage();
            dataWasFrom = dataWillBeFrom;
        }

        CompletableFuture<List<News>> future;

        if (filtersAreNotSuitableForCategory()) {
            future = newsRepository.getNewsWithFiltersFuture(
                    filters,
                    null,
                    currentPage,
                    pageSize
            );
        } else {
            future = newsRepository.getNewsByCategoryFuture(
                    filters,
                    newsCategory,
                    null,
                    currentPage,
                    pageSize
            );
        }

        future.handleAsync((result, exception) -> {
            UIHandler.post(() -> {
                if (exception == null) {
                    getViewState().showNews(result, currentPage++ == 1);
                    getViewState().handleException(null);
                } else if (exception instanceof NoConnectionException && currentPage != 1) {
                    getViewState().showNews(List.of(), currentPage++ == 1);
                    getViewState().handleException(null);
                } else {
                    getViewState().handleException((Exception) exception);
                }

                UIHandler.removeCallbacksAndMessages(null);
            });

            return null;
        });
    }

    public void resetCurrentPage() {
        currentPage = 1;
    }

    boolean filtersAreNotSuitableForCategory() {
        Filters filters = filtersRepository.getFilters();
        return filters.getDates() != null || filters.getOrderBy() != null;
    }
}
