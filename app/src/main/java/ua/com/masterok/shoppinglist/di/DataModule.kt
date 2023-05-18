package ua.com.masterok.shoppinglist.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ua.com.masterok.shoppinglist.data.AppDatabase
import ua.com.masterok.shoppinglist.data.ShopListDao
import ua.com.masterok.shoppinglist.data.ShopListRepositoryImpl
import ua.com.masterok.shoppinglist.domain.ShopListRepository

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {
        @ApplicationScope
        @Provides
        fun provideShopListDao(application: Application): ShopListDao {
            return AppDatabase.getInstance(application).shopListDao()
        }
    }

}