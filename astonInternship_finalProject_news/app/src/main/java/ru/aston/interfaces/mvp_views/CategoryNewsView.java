package ru.aston.interfaces.mvp_views;


import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.aston.data.News;

public interface CategoryNewsView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showFoundNews(List<News> news);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideOrShowNewsWithFilters(Boolean show);
}
