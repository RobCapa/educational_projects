package com.example.recyclerview.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.recyclerview.api.Api
import java.util.concurrent.Executors

object PagedList {
    fun createCharactersList(throwable: MutableLiveData<Throwable?>) = PagedList
        .Builder(
            CharacterDataSource(throwable),
            PagedList
                .Config
                .Builder()
                .setEnablePlaceholders(false)
                .setPageSize(Api.pageSize)
                .build()
        )
        .setFetchExecutor(Executors.newSingleThreadExecutor())
        .setNotifyExecutor(MainThreadExecutor())
        .build()
}