package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.data.local.ProductDatabase
import com.example.data.repository.ProductRepository
import com.example.ui.screens.ProductCatalogScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ProductViewModel

/**
 * Actividad Principal de la aplicación Catálogo de Productos.
 * Inicializa la persistencia local con Room Database y configura
 * la interfaz de usuario con Jetpack Compose y Material 3.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: ProductViewModel by viewModels {
        val database = ProductDatabase.getDatabase(applicationContext)
        val repository = ProductRepository(database.productDao())
        ProductViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ProductCatalogScreen(viewModel = viewModel)
                }
            }
        }
    }
}
