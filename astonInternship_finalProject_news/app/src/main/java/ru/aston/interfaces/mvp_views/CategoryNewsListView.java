package ru.aston.interfaces.mvp_views;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.aston.data.News;

public interface CategoryNewsListView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showNews(List<News> news, boolean resetItems);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void handleException(Exception ex);
}
