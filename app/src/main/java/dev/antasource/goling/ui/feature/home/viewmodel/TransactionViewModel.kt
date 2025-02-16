package dev.antasource.goling.ui.feature.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.antasource.goling.data.model.ErrorMessage
import dev.antasource.goling.data.model.pickup.response.History
import dev.antasource.goling.data.model.pickup.response.Order
import dev.antasource.goling.data.model.pickup.response.OrderDetail
import dev.antasource.goling.data.repositoty.ShippingRepository
import kotlinx.coroutines.launch
import okio.IOException

class TransactionViewModel(
    private val repository: ShippingRepository
) : ViewModel(){

    private val _listHistoryOrder = MutableLiveData<List<Order>>()
    val historyOrderList : LiveData<List<Order>> = _listHistoryOrder

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    private val _history = MutableLiveData<List<History>?>()
    val history : LiveData<List<History>?> = _history

    private val _orderDetail = MutableLiveData<OrderDetail>()
    val orderHistory : LiveData<OrderDetail> = _orderDetail

    private val _order = MutableLiveData<Order>()
    val order : LiveData<Order> = _order

    private val _productTypeName = MutableLiveData<String>()
    val productTypeName : LiveData<String> = _productTypeName

    var token : String= ""


    fun getHistoryOrder(){
        viewModelScope.launch{
            val response = repository.getOrders(token)
            if (response.isSuccessful) {
                _listHistoryOrder.value = response.body()?.orders
            } else {
                try {
                    val gson = Gson()
                    val error =
                        gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                   _errorMsg.value = error.message
                } catch (e: IOException) {
                    _errorMsg.value = e.message
                }
            }
        }
    }
    fun getHistoryDetail(id: Long){
        viewModelScope.launch{
            val response = repository.getOrderDetail(token, id)
            if(response.isSuccessful){
                val data = response.body()
                data.let {
                    _history.value = data?.history
                    _orderDetail.value = data?.orderDetail!!
                    _order.value = data.order!!
                }
            }
        }
    }

    fun getproductTypeById(id:Int){
        viewModelScope.launch{
            val response = repository.getProductTypeById(id)
            if (response.isSuccessful) {
                _productTypeName.value = response.body()?.productTypeName?.name
                Log.d("View model", "Response ${response.body()}")
            } else {
                try {
                    val gson = Gson()
                    val error =
                        gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                    _errorMsg.value = error.message
                } catch (e: IOException) {
                    _errorMsg.value = e.message
                }
            }
        }
    }

//    fun getProvinceName(id: Int): String {
//        viewModelScope.launch{
//            val response = repository.getRegion()
//            if (response.isSuccessful) {
//                _region.value = response.body()
//            } else {
//                try {
//                    val gson = Gson()
//                    val error =
//                        gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
//                    _errorMsg.value = error.message
//                } catch (e: IOException) {
//                    _errorMsg.value = e.message
//                }
//            }
//        }
//    }
}