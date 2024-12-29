package dev.antasource.goling.data.repositoty

import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.model.TopUpRequest

class TopUpRepository(private val networkRemoteSource: NetworkRemoteSource) {
    suspend fun topupwallet(token: String,topUpRequest: TopUpRequest) =  networkRemoteSource.topUpWallet(token, topUpRequest)
    suspend fun verifyPayment(token:String, transactionId: String) = networkRemoteSource.verifyPayment(token, transactionId)
}