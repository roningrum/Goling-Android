package dev.antasource.goling.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.antasource.goling.data.repositoty.HomeRepositoryRepository
import dev.antasource.goling.ui.feature.home.viewmodel.HomeViewModel

class MainViewModelFactory(private val repository: HomeRepositoryRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as HomeRepositoryRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }
}