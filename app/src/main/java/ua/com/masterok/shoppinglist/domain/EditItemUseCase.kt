package ua.com.masterok.shoppinglist.domain

class EditItemUseCase(private val shopLIstRepository: ShopListRepository) {

    suspend fun editItem(shopItem: ShopItem) {
        shopLIstRepository.editItem(shopItem)
    }

}