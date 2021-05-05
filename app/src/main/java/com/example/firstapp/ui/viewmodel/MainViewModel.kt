package com.example.firstapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstapp.data.Resource
import com.example.firstapp.data.model.GifDataResponse
import com.example.firstapp.data.model.GifResponse
import com.example.firstapp.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    val gifs = MutableLiveData<Resource<GifResponse>>()//mutableLiveData
    private val savedgifs = MutableLiveData<List<GifDataResponse>>()

    fun getGifs(apiKey: String, query: String, isLoadMore: Boolean = false) =
        //to make code work on background thread
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isBlank()) {
                savedgifs.postValue(emptyList())
                gifs.postValue(Resource.success(GifResponse(emptyList())))
                return@launch
            }
            gifs.postValue(Resource.loading(data = null))
            try {
                val size = gifs.value?.data?.data?.size ?: 0
                val result = mainRepository.getGifs(apiKey, query, offset = size)
                //if isloadMore == true, we just scroll them, we have to add new gifs without deleting old
                if (isLoadMore) {
                    val currentSavedGifs = savedgifs.value ?: emptyList()
                    val newGifs = result.data
                    val gifResult = currentSavedGifs + newGifs
                    savedgifs.postValue(gifResult)
                    gifs.postValue(Resource.success(data = result.copy(data = gifResult)))
                }
                // isLoadMore == false -> need to delete old gifs and make new api call
                else {
                    savedgifs.postValue(emptyList())
                    gifs.postValue(Resource.success(data = result))
                }
            } catch (exception: Exception) {
                gifs.postValue(
                    Resource.error(
                        data = null,
                        message = exception.message ?: "Error Occurred!"

                    )
                )
            }
        }
}
