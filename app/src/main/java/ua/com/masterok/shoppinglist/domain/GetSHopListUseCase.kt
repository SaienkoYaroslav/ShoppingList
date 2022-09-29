package ua.com.masterok.shoppinglist.domain

class GetSHopListUseCase(private val shopLIstRepository: ShopLIstRepository) {

    fun getShopList(): List<ShopItem> {
        return shopLIstRepository.getShopList()
    }

}