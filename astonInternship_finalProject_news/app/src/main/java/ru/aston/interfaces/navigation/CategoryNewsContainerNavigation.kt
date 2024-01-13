package ru.aston.interfaces.navigation

import ru.aston.data.News

interface CategoryNewsContainerNavigation {
    fun navToNewsDetailsFrag(news: News)
    fun navToFiltersFrag()
    fun navToCategoryNewsFrag()
}