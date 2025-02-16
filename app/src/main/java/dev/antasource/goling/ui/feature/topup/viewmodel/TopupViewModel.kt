package dev.antasource.goling.ui.feature.topup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.antasource.goling.data.model.ErrorMessage
import dev.antasource.goling.data.model.PaymentResponse
import dev.antasource.goling.data.model.TopUpRequest
import dev.antasource.goling.data.model.TopUpResponse
import dev.antasource.goling.data.repositoty.TopUpRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

class TopupViewModel(private val repository: TopUpRepository): ViewModel() {
    private val _topUpResponse =MutableLiveData<TopUpResponse>()
    val topUpResponse : LiveData<TopUpResponse> = _topUpResponse

    private val _paymentVerify = MutableLiveData<PaymentResponse>()
    val paymentVerify : LiveData<PaymentResponse> = _paymentVerify

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage

    var token: String =""

    var transactionId : String = ""

    var amount: Int = 0

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->   }


    fun topUpAmountWallet(){
        viewModelScope.launch{
            val response = repository.topupwallet(token, TopUpRequest(amount))
            if(response.isSuccessful){
                _topUpResponse.value = response.body()
            }
            else{
                try {
                    val gson = Gson()
                    val error = gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java )
                    _errorMessage.value = error.message
                } catch (e: IOException){
                    _errorMessage.value = e.message
                }
            }
        }
    }

    fun verifyTopUp(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch{
            val response = repository.verifyPayment(token, transactionId)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    _paymentVerify.value = response.body()
                }
                else{
                    try {
                        val gson = Gson()
                        val error = gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java )
                        _errorMessage.value = error.message
                    } catch (e: IOException){
                        _errorMessage.value = e.message
                    }
                }
            }
        }
    }

}