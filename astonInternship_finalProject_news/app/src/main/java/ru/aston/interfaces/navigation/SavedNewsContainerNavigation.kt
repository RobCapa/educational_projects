package ru.aston.interfaces.navigation

import ru.aston.data.News

interface SavedNewsContainerNavigation {
    fun navToNewsDetailsFrag(news: News)
    fun navToSavedFrag()
    fun navToFiltersFrag()
}