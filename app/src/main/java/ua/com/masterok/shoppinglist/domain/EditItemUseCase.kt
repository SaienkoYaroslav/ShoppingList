package ua.com.masterok.shoppinglist.domain

import javax.inject.Inject

class EditItemUseCase @Inject constructor(private val shopLIstRepository: ShopListRepository) {

    suspend fun editItem(shopItem: ShopItem) {
        shopLIstRepository.editItem(shopItem)
    }

}