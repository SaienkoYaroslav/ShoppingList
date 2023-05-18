package ua.com.masterok.shoppinglist.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetShopListUseCase @Inject constructor(private val shopLIstRepository: ShopListRepository) {

    fun getShopList(): LiveData<List<ShopItem>> {
        return shopLIstRepository.getShopList()
    }

}