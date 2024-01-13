package ru.aston.interfaces.navigation

import ru.aston.data.News
import ru.aston.data.NewsSource

interface SourcesContainerNavigation {
    fun navToSourceNewsFrag(source: NewsSource)
    fun navToNewsDetailsFrag(news: News)
    fun navToSourcesFrag()
    fun navToFiltersFrag()
}