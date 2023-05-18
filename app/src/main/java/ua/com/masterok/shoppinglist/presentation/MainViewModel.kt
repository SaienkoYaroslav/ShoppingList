package ua.com.masterok.shoppinglist.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.com.masterok.shoppinglist.domain.EditItemUseCase
import ua.com.masterok.shoppinglist.domain.GetShopListUseCase
import ua.com.masterok.shoppinglist.domain.RemoveItemUseCase
import ua.com.masterok.shoppinglist.domain.ShopItem
import javax.inject.Inject

// успадковуємся від ViewModel(), якщо далі нам не потрібно передавати контекст, якщо потрібно, то
// успадковуємось від AndroidViewModel() куди передаємо аплікейшн в якості контенту (AndroidViewModel(Application()))
class MainViewModel @Inject constructor(
    private val getSHopListUseCase: GetShopListUseCase,
    private val removeItemUseCase: RemoveItemUseCase,
    private val editItemUseCase: EditItemUseCase
) : ViewModel() {

    val shopListLiveData = getSHopListUseCase.getShopList()


    fun removeItem(shopItem: ShopItem) {
        viewModelScope.launch {
            removeItemUseCase.removeItem(shopItem)
        }
    }


    fun changeEnableState(shopItem: ShopItem) {
        viewModelScope.launch {
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            editItemUseCase.editItem(newItem)
        }
    }

}