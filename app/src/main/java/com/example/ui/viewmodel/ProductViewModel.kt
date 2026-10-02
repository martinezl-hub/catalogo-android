package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.DemoData
import com.example.data.local.Product
import com.example.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado general de la UI del catálogo.
 * Diseñado bajo el patrón de Unidirectional Data Flow (UDF).
 */
data class CatalogUiState(
    val products: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String = "Todos",
    val selectedProductForDetail: Product? = null,
    val productForEdit: Product? = null,
    val isFormOpen: Boolean = false,
    val productToDelete: Product? = null,
    val isAboutDialogOpen: Boolean = false,
    val userMessage: String? = null,
    val totalInventoryValue: Double = 0.0,
    val totalStockCount: Int = 0
)

/**
 * Estado interno exclusivo para modales, formularios y mensajes emergentes.
 */
private data class DialogState(
    val selectedProductForDetail: Product? = null,
    val productForEdit: Product? = null,
    val isFormOpen: Boolean = false,
    val productToDelete: Product? = null,
    val isAboutDialogOpen: Boolean = false,
    val userMessage: String? = null
)

/**
 * ViewModel que gestiona la lógica de negocio y coordina el flujo
 * de datos entre la base de datos local Room y las pantallas Jetpack Compose.
 */
class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _dialogState = MutableStateFlow(DialogState())

    val uiState: StateFlow<CatalogUiState> = combine(
        repository.allProducts,
        _searchQuery,
        _selectedCategory,
        _dialogState
    ) { allProducts, query, category, dialogs ->
        val filtered = allProducts.filter { product ->
            val matchesCategory = (category == "Todos") || (product.category.equals(category, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }

        val totalValue = allProducts.sumOf { it.price * it.stock }
        val totalStock = allProducts.sumOf { it.stock }

        CatalogUiState(
            products = allProducts,
            filteredProducts = filtered,
            searchQuery = query,
            selectedCategory = category,
            selectedProductForDetail = dialogs.selectedProductForDetail,
            productForEdit = dialogs.productForEdit,
            isFormOpen = dialogs.isFormOpen,
            productToDelete = dialogs.productToDelete,
            isAboutDialogOpen = dialogs.isAboutDialogOpen,
            userMessage = dialogs.userMessage,
            totalInventoryValue = totalValue,
            totalStockCount = totalStock
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CatalogUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
    }

    fun openCreateForm() {
        _dialogState.update {
            it.copy(
                isFormOpen = true,
                productForEdit = null
            )
        }
    }

    fun openEditForm(product: Product) {
        _dialogState.update {
            it.copy(
                isFormOpen = true,
                productForEdit = product,
                selectedProductForDetail = null
            )
        }
    }

    fun closeForm() {
        _dialogState.update {
            it.copy(
                isFormOpen = false,
                productForEdit = null
            )
        }
    }

    fun openDetail(product: Product) {
        _dialogState.update {
            it.copy(selectedProductForDetail = product)
        }
    }

    fun closeDetail() {
        _dialogState.update {
            it.copy(selectedProductForDetail = null)
        }
    }

    fun requestDelete(product: Product) {
        _dialogState.update {
            it.copy(productToDelete = product)
        }
    }

    fun cancelDelete() {
        _dialogState.update {
            it.copy(productToDelete = null)
        }
    }

    fun confirmDelete() {
        val product = _dialogState.value.productToDelete ?: return
        viewModelScope.launch {
            try {
                repository.delete(product)
                _dialogState.update {
                    it.copy(
                        productToDelete = null,
                        selectedProductForDetail = if (it.selectedProductForDetail?.id == product.id) null else it.selectedProductForDetail,
                        userMessage = "Producto '${product.name}' eliminado con éxito"
                    )
                }
            } catch (e: Exception) {
                _dialogState.update {
                    it.copy(
                        productToDelete = null,
                        userMessage = "Error al eliminar: ${e.localizedMessage ?: "Ocurrió un error inesperado"}"
                    )
                }
            }
        }
    }

    fun saveProduct(
        name: String,
        price: Double,
        description: String,
        category: String,
        iconName: String,
        stock: Int,
        existingId: Int = 0
    ) {
        viewModelScope.launch {
            try {
                val trimmedName = name.trim()
                val trimmedDesc = description.trim()
                val trimmedCat = category.trim()

                if (existingId == 0) {
                    val newProduct = Product(
                        name = trimmedName,
                        price = price,
                        description = trimmedDesc,
                        category = trimmedCat,
                        iconName = iconName,
                        stock = stock
                    )
                    repository.insert(newProduct)
                    _dialogState.update {
                        it.copy(
                            isFormOpen = false,
                            productForEdit = null,
                            userMessage = "Producto registrado con éxito"
                        )
                    }
                } else {
                    val updatedProduct = Product(
                        id = existingId,
                        name = trimmedName,
                        price = price,
                        description = trimmedDesc,
                        category = trimmedCat,
                        iconName = iconName,
                        stock = stock
                    )
                    repository.update(updatedProduct)
                    _dialogState.update {
                        it.copy(
                            isFormOpen = false,
                            productForEdit = null,
                            selectedProductForDetail = updatedProduct,
                            userMessage = "Producto actualizado correctamente"
                        )
                    }
                }
            } catch (e: Exception) {
                _dialogState.update {
                    it.copy(
                        userMessage = "Error al guardar: ${e.localizedMessage ?: "Error desconocido"}"
                    )
                }
            }
        }
    }

    fun loadDemoData() {
        viewModelScope.launch {
            try {
                val samples = DemoData.getSampleProducts()
                repository.insertAll(samples)
                _dialogState.update {
                    it.copy(
                        userMessage = "Se han cargado ${samples.size} productos de prueba"
                    )
                }
            } catch (e: Exception) {
                _dialogState.update {
                    it.copy(
                        userMessage = "Error al cargar datos de prueba: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun openAboutDialog() {
        _dialogState.update { it.copy(isAboutDialogOpen = true) }
    }

    fun closeAboutDialog() {
        _dialogState.update { it.copy(isAboutDialogOpen = false) }
    }

    fun clearUserMessage() {
        _dialogState.update { it.copy(userMessage = null) }
    }

    class Factory(private val repository: ProductRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                return ProductViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
