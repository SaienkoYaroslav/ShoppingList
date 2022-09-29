package ua.com.masterok.shoppinglist.domain

import androidx.lifecycle.LiveData

class GetShopListUseCase(private val shopLIstRepository: ShopListRepository) {

    fun getShopList(): LiveData<List<ShopItem>> {
        return shopLIstRepository.getShopList()
    }

}