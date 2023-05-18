package ua.com.masterok.shoppinglist.domain

import javax.inject.Inject

class RemoveItemUseCase @Inject constructor(private val shopLIstRepository: ShopListRepository) {

    suspend fun removeItem(shopItem: ShopItem) {
        shopLIstRepository.removeItem(shopItem)
    }

}