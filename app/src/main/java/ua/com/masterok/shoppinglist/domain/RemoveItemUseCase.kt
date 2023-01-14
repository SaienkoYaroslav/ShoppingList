package ua.com.masterok.shoppinglist.domain

class RemoveItemUseCase(private val shopLIstRepository: ShopListRepository) {

    suspend fun removeItem(shopItem: ShopItem) {
        shopLIstRepository.removeItem(shopItem)
    }

}