package ua.com.masterok.shoppinglist.domain

import javax.inject.Inject

class GetItemFromIdUseCase @Inject constructor(private val shopLIstRepository: ShopListRepository) {

    suspend fun getItemFromId(shopItemId: Int): ShopItem {
        return shopLIstRepository.getItemFromId(shopItemId)
    }

}