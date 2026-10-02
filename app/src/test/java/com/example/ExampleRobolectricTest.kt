package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.Product
import com.example.data.local.ProductDao
import com.example.data.local.ProductDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var database: ProductDatabase
    private lateinit var productDao: ProductDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ProductDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        productDao = database.productDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Catálogo", appName)
    }

    @Test
    fun testRoomDatabaseCrudOperations() = runBlocking {
        // 1. Create / Insert
        val product = Product(
            name = "Teclado Mecánico RGB",
            price = 49.99,
            description = "Teclado mecánico para gaming con switches azules.",
            category = "Electrónica",
            iconName = "laptop",
            stock = 12
        )
        val insertedId = productDao.insertProduct(product)
        assertTrue(insertedId > 0)

        // 2. Read / List
        val allProducts = productDao.getAllProducts().first()
        assertEquals(1, allProducts.size)
        assertEquals("Teclado Mecánico RGB", allProducts[0].name)
        assertEquals(49.99, allProducts[0].price, 0.001)

        // 3. Update
        val updatedProduct = allProducts[0].copy(
            price = 39.99,
            stock = 8
        )
        productDao.updateProduct(updatedProduct)

        val productAfterUpdate = productDao.getProductById(allProducts[0].id).first()
        assertNotNull(productAfterUpdate)
        assertEquals(39.99, productAfterUpdate!!.price, 0.001)
        assertEquals(8, productAfterUpdate.stock)

        // 4. Search
        val searchResults = productDao.searchProducts("Mecánico").first()
        assertEquals(1, searchResults.size)

        // 5. Delete
        productDao.deleteProduct(productAfterUpdate)
        val productsAfterDelete = productDao.getAllProducts().first()
        assertTrue(productsAfterDelete.isEmpty())
    }
}
