package ua.com.masterok.shoppinglist.domain

class GetItemFromIdUseCase(private val shopLIstRepository: ShopLIstRepository) {

    fun getItemFromId(shopItemId: Int): ShopItem {
        return shopLIstRepository.getItemFromId(shopItemId)
    }

}