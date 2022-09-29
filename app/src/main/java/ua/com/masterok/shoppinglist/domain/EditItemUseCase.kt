package ua.com.masterok.shoppinglist.domain

class EditItemUseCase(private val shopLIstRepository: ShopLIstRepository) {

    fun editItem(shopItem: ShopItem) {
        shopLIstRepository.editItem(shopItem)
    }

}