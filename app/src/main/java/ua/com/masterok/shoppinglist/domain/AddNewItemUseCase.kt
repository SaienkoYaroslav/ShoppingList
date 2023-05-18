package ua.com.masterok.shoppinglist.domain

import javax.inject.Inject

class AddNewItemUseCase @Inject constructor(private val shopLIstRepository: ShopListRepository) {

    suspend fun addNewItem(shopItem: ShopItem) {
        shopLIstRepository.addNewItem(shopItem)
    }

}