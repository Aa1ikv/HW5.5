package com.example.hw55

import android.telecom.Call
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw55.data.CartoonApiService
import com.example.hw55.data.models.BaseResponce
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.Response
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val api: CartoonApiService,
    private val database: AppDatabase
) : ViewModel() {

    private val _charactersData = MutableLiveData<List<Character>?>()
    val charactersData: MutableLiveData<List<Character>?>
        get() = _charactersData

    private val _errorData = MutableLiveData<String>()
    val errorData: LiveData<String> get() = _errorData

    private val _viewedCharacters = MutableLiveData<List<CharacterEntity>>()
    val viewedCharacters: LiveData<List<CharacterEntity>> get() = _viewedCharacters

    fun getCharacters() {
        api.getCharacters().enqueue(object : retrofit2.Callback<BaseResponce> {
            override fun onResponse(call: Call<BaseResponce>, response: Response<BaseResponce>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        _charactersData.postValue(it.characters)
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponce>, t: Throwable) {
                _errorData.postValue(t.localizedMessage ?: "Неизвестная ошибка")
            }
        })
    }

    fun saveCharacter(character: Character) {
        viewModelScope.launch {
            val imageBase64 = bitmapToBase64(character.imageBitmap)
            val entity = CharacterEntity(
                id = character.id,
                name = character.name,
                status = character.status,
                imageBase64 = imageBase64
            )
            database.characterDao().insertCharacter(entity)
        }
    }

    fun loadViewedCharacters() {
        viewModelScope.launch {
            database.characterDao().getViewedCharacters().collect {
                _viewedCharacters.postValue(it)
            }
        }
    }
}
