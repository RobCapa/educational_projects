package ru.aston.fragments.news_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aston.data.News
import ru.aston.interfaces.repositories.NewsRepository
import javax.inject.Inject

class NewsDetailsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _newsIsSaved = MutableLiveData<Boolean>()
    val newsIsSaved: LiveData<Boolean>
        get() = _newsIsSaved

    fun saveNews(news: News) {
        viewModelScope.launch {
            newsRepository.addNewsToSaved(news).collect { isSaved ->
                _newsIsSaved.value = isSaved
            }
        }
    }

    fun removeNews(news: News) {
        viewModelScope.launch {
            newsRepository.removeNewsFromSaved(news).collect { isRemoved ->
                _newsIsSaved.value = !isRemoved
            }
        }
    }

    fun checkNewsIsSaved(news: News) {
        viewModelScope.launch {
            newsRepository.newsIsSaved(news).collect { isSaved ->
                _newsIsSaved.value = isSaved
            }
        }
    }
}