package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

object ProductIconHelper {

    fun getIconForName(iconName: String, category: String = ""): ImageVector {
        return when (iconName.lowercase()) {
            "laptop", "computadora" -> Icons.Default.Laptop
            "phone", "celular", "telefono" -> Icons.Default.PhoneAndroid
            "headphones", "audifonos" -> Icons.Default.Headphones
            "watch", "reloj" -> Icons.Default.Watch
            "camera", "camara" -> Icons.Default.PhotoCamera
            "shirt", "ropa", "camisa" -> Icons.Default.Checkroom
            "shoe", "zapatos", "calzado" -> Icons.Default.DirectionsRun
            "coffee", "cafe", "bebida" -> Icons.Default.LocalCafe
            "couch", "muebles", "sofa" -> Icons.Default.Weekend
            "ball", "pelota", "futbol", "deportes" -> Icons.Default.SportsSoccer
            "spa", "belleza" -> Icons.Default.Spa
            "cart", "compras" -> Icons.Default.ShoppingCart
            else -> {
                // Fallback based on category
                when {
                    category.contains("electr", ignoreCase = true) -> Icons.Default.Laptop
                    category.contains("ropa", ignoreCase = true) -> Icons.Default.Checkroom
                    category.contains("hogar", ignoreCase = true) -> Icons.Default.Weekend
                    category.contains("alimento", ignoreCase = true) || category.contains("bebida", ignoreCase = true) -> Icons.Default.LocalCafe
                    category.contains("deporte", ignoreCase = true) -> Icons.Default.SportsSoccer
                    category.contains("belleza", ignoreCase = true) || category.contains("cuidado", ignoreCase = true) -> Icons.Default.Spa
                    else -> Icons.Default.Inventory2
                }
            }
        }
    }

    fun getCategoryColor(category: String): Pair<Color, Color> {
        // Returns (containerColor, contentColor)
        return when {
            category.contains("electr", ignoreCase = true) -> Pair(Color(0xFFDBEAFE), Color(0xFF1E40AF))
            category.contains("ropa", ignoreCase = true) -> Pair(Color(0xFFFCE7F3), Color(0xFF9D174D))
            category.contains("hogar", ignoreCase = true) -> Pair(Color(0xFFFEF3C7), Color(0xFF92400E))
            category.contains("alimento", ignoreCase = true) -> Pair(Color(0xFFDCFCE7), Color(0xFF166534))
            category.contains("deporte", ignoreCase = true) -> Pair(Color(0xFFFFEDD5), Color(0xFF9A3412))
            category.contains("belleza", ignoreCase = true) -> Pair(Color(0xFFF3E8FF), Color(0xFF6B21A8))
            else -> Pair(Color(0xFFE2E8F0), Color(0xFF334155))
        }
    }
}
