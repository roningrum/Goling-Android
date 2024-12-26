package dev.antasource.goling.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.antasource.goling.data.repositoty.TopUpRepository
import dev.antasource.goling.ui.feature.topup.viewmodel.TopupViewModel

@Suppress("UNCHECKED_CAST")
class TopUpViewModelFactory(private val repository: TopUpRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TopupViewModel::class.java) -> TopupViewModel(repository) as T
            else -> throw IllegalArgumentException(
                "Unknown View Model"
            )
        }
    }
}