package dev.antasource.goling.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.antasource.goling.data.repositoty.ShippingRepository
import dev.antasource.goling.ui.feature.estimate.viewmodel.EstimateViewModel
import dev.antasource.goling.ui.feature.home.viewmodel.TransactionViewModel
import dev.antasource.goling.ui.feature.pickup.viewmodel.PickupViewModel

class ShippingViewModelFactory(private val shippingRepository: ShippingRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EstimateViewModel::class.java) -> EstimateViewModel(shippingRepository) as T
            modelClass.isAssignableFrom(PickupViewModel::class.java) -> PickupViewModel(shippingRepository) as T
            modelClass.isAssignableFrom(TransactionViewModel::class.java) -> TransactionViewModel(shippingRepository) as T
            else -> throw IllegalArgumentException(
                "Unknown View Model"
            )
        }
    }
}