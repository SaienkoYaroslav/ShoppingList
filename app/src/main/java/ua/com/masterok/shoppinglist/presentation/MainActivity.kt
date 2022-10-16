package ua.com.masterok.shoppinglist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ua.com.masterok.shoppinglist.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        // теж саме що viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopListLiveData.observe(this) {
            shopListAdapter.shopList = it
        }
    }

    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(rvShopList){
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            // встановлення максимальної кількості пула для кожного вью
            recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_DISABLED, ShopListAdapter.MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_ENABLED, ShopListAdapter.MAX_POOL_SIZE)
        }

    }


}