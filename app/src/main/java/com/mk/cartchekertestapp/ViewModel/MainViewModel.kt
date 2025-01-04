package com.mk.cartchekertestapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mk.cartchekertestapp.MyApiService
import com.mk.cartchekertestapp.models.CartInfoCallBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val myApiService: MyApiService
) : ViewModel() {

    private val _mutableCartInfo = MutableLiveData<CartInfoCallBody>()
    val mutableCartInfo: LiveData<CartInfoCallBody> get() = _mutableCartInfo

    fun getCartInfo(number: String) {
        viewModelScope.launch {
            try {
                val response = myApiService.getCurtInfo(number)
                if (response.isSuccessful) {
                    _mutableCartInfo.value = response.body()
                    Log.d("TAG", "getCartInfo: "+response.body())
                } else {
                    Log.d("TAG", "API call failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("TAG", "Error fetching cart info: ${e.message}")
            }
        }
    }
}

