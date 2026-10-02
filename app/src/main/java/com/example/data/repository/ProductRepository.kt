package com.example.data.repository

import com.example.data.local.Product
import com.example.data.local.ProductDao
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que abstrae la capa de datos de Room para el ViewModel.
 * Sigue las mejores prácticas de Clean Architecture y Room Database.
 */
class ProductRepository(private val productDao: ProductDao) {

    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    fun getProductById(id: Int): Flow<Product?> = productDao.getProductById(id)

    fun searchProducts(query: String): Flow<List<Product>> = productDao.searchProducts(query)

    fun getProductsByCategory(category: String): Flow<List<Product>> =
        productDao.getProductsByCategory(category)

    suspend fun insert(product: Product): Long = productDao.insertProduct(product)

    suspend fun insertAll(products: List<Product>) = productDao.insertAll(products)

    suspend fun update(product: Product) = productDao.updateProduct(product)

    suspend fun delete(product: Product) = productDao.deleteProduct(product)

    suspend fun deleteById(id: Int) = productDao.deleteProductById(id)

    suspend fun deleteAll() = productDao.deleteAll()

    suspend fun count(): Int = productDao.countProducts()
}
