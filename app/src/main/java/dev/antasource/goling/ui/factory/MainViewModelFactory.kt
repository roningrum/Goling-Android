package dev.antasource.goling.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.antasource.goling.data.repositoty.HomeRepository
import dev.antasource.goling.ui.feature.home.viewmodel.HomeViewModel

class MainViewModelFactory(private val repository: HomeRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as HomeRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }
}