package dev.antasource.goling.data.repositoty

import dev.antasource.goling.data.networksource.NetworkRemoteSource

class HomeRepository(private val networkRemoteSource: NetworkRemoteSource) {
    suspend fun getUserProfile(token: String) = networkRemoteSource.getUser(token)
    suspend fun getBalance(token: String) = networkRemoteSource.balance(token)
}