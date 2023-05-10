package com.example.recyclerview.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.example.recyclerview.api.Api
import com.example.recyclerview.api.Character
import com.example.recyclerview.api.Response
import retrofit2.Call
import retrofit2.Callback
import java.lang.IllegalStateException

class CharacterDataSource(
    private val throwable: MutableLiveData<Throwable?>
) : PositionalDataSource<Character>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Character>) {
        load(page = 1) { callback.onResult(it.toMutableList(), 1) }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Character>) {
        load(page = (params.startPosition / Api.pageSize)) { callback.onResult(it.toMutableList()) }
    }

    private fun load(page: Int, callback: (List<Character>) -> Unit) {
        Api.retrofit.loadList(page = page).enqueue(
            object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>,
                ) = if(response.code() == 200)
                    callback(response.body()?.results ?: emptyList())
                else
                    throwable.postValue(IllegalStateException())

                override fun onFailure(call: Call<Response>, t: Throwable) = throwable.postValue(t)
            }
        )
    }
}