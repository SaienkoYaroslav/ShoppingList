package ua.com.masterok.shoppinglist.domain

data class ShopItem (
    val id: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean
        )