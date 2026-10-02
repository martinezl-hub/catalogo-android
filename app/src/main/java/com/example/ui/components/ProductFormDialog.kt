package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.DemoData
import com.example.data.local.Product
import java.util.Locale

@Composable
fun ProductFormDialog(
    initialProduct: Product? = null,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        price: Double,
        description: String,
        category: String,
        iconName: String,
        stock: Int,
        existingId: Int
    ) -> Unit
) {
    val isEditMode = initialProduct != null

    // Form field states
    var name by remember { mutableStateOf(initialProduct?.name ?: "") }
    var priceText by remember {
        mutableStateOf(
            if (initialProduct != null) String.format(Locale.US, "%.2f", initialProduct.price) else ""
        )
    }
    var description by remember { mutableStateOf(initialProduct?.description ?: "") }
    var category by remember {
        mutableStateOf(initialProduct?.category ?: "Electrónica")
    }
    var selectedIcon by remember {
        mutableStateOf(initialProduct?.iconName ?: "laptop")
    }
    var stockText by remember {
        mutableStateOf(initialProduct?.stock?.toString() ?: "10")
    }

    // Granular validation states
    var nameError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    var descError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }

    // Dropdown state for category selection
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    val selectableCategories = remember {
        DemoData.categories.filter { it != "Todos" }
    }

    // Validation rules
    fun validateName(value: String): String? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> "El nombre del producto es obligatorio"
            trimmed.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            else -> null
        }
    }

    fun validatePrice(value: String): String? {
        val trimmed = value.trim().replace(',', '.')
        val parsed = trimmed.toDoubleOrNull()
        return when {
            trimmed.isEmpty() -> "El precio es obligatorio"
            parsed == null || parsed <= 0.0 -> "Ingrese un precio válido mayor a 0 (ej: 19.99)"
            else -> null
        }
    }

    fun validateDescription(value: String): String? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> "La descripción es obligatoria"
            trimmed.length < 5 -> "La descripción debe tener al menos 5 caracteres"
            else -> null
        }
    }

    fun validateStock(value: String): String? {
        val trimmed = value.trim()
        val parsed = trimmed.toIntOrNull()
        return when {
            trimmed.isEmpty() -> "El stock es obligatorio"
            parsed == null || parsed < 0 -> "El stock debe ser un número entero mayor o igual a 0"
            else -> null
        }
    }

    fun validateAll(): Boolean {
        nameError = validateName(name)
        priceError = validatePrice(priceText)
        descError = validateDescription(description)
        stockError = validateStock(stockText)
        return nameError == null && priceError == null && descError == null && stockError == null
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 20.dp)
                .testTag("product_form_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isEditMode) "Editar Producto" else "Nuevo Producto",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isEditMode) "Modifica los datos del registro en Room" else "Completa los campos para registrar en Room",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("form_close_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (nameError != null) {
                            nameError = validateName(it)
                        }
                    },
                    label = { Text("Nombre del Producto *") },
                    placeholder = { Text("Ej: Auriculares Bluetooth") },
                    isError = nameError != null,
                    supportingText = {
                        nameError?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_product_name")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price and Stock row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Price
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = {
                            priceText = it
                            if (priceError != null) {
                                priceError = validatePrice(it)
                            }
                        },
                        label = { Text("Precio ($) *") },
                        placeholder = { Text("0.00") },
                        isError = priceError != null,
                        supportingText = {
                            priceError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_product_price")
                    )

                    // Stock
                    OutlinedTextField(
                        value = stockText,
                        onValueChange = {
                            stockText = it
                            if (stockError != null) {
                                stockError = validateStock(it)
                            }
                        },
                        label = { Text("Stock *") },
                        placeholder = { Text("1") },
                        isError = stockError != null,
                        supportingText = {
                            stockError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_product_stock")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category selector
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría *") },
                        trailingIcon = {
                            IconButton(onClick = { categoryDropdownExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Desplegar categorías"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { categoryDropdownExpanded = true }
                            .testTag("input_product_category")
                    )

                    DropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        selectableCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Icon selector
                Text(
                    text = "Selecciona un ícono representativo:",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(DemoData.availableIcons) { (iconKey, _) ->
                        val isSelected = selectedIcon == iconKey
                        val iconVector = ProductIconHelper.getIconForName(iconKey, category)

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedIcon = iconKey },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = iconVector,
                                contentDescription = iconKey,
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Description Field
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        if (descError != null) {
                            descError = validateDescription(it)
                        }
                    },
                    label = { Text("Descripción del Producto *") },
                    placeholder = { Text("Escribe características, especificaciones y detalles...") },
                    isError = descError != null,
                    supportingText = {
                        descError?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_product_description")
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("form_cancel_button")
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (validateAll()) {
                                val finalPrice = priceText.trim().replace(',', '.').toDouble()
                                val finalStock = stockText.trim().toInt()
                                onSave(
                                    name,
                                    finalPrice,
                                    description,
                                    category,
                                    selectedIcon,
                                    finalStock,
                                    initialProduct?.id ?: 0
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("form_save_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isEditMode) "Actualizar" else "Guardar")
                    }
                }
            }
        }
    }
}
