package com.example.data

import com.example.data.local.Product

/**
 * Datos de demostración claramente identificados para pruebas del estudiante y el evaluador.
 */
object DemoData {

    val categories = listOf(
        "Todos",
        "Electrónica",
        "Ropa y Moda",
        "Hogar y Muebles",
        "Alimentos y Bebidas",
        "Deportes",
        "Cuidado y Belleza"
    )

    val availableIcons = listOf(
        "laptop" to "Computadora / Laptop",
        "phone" to "Teléfono Celular",
        "headphones" to "Audífonos",
        "watch" to "Reloj Inteligente",
        "camera" to "Cámara Fotográfica",
        "shirt" to "Ropa / Camisa",
        "shoe" to "Calzado / Zapatos",
        "coffee" to "Café / Bebidas",
        "couch" to "Muebles / Hogar",
        "ball" to "Deportes",
        "cart" to "General / Compras"
    )

    fun getSampleProducts(): List<Product> = listOf(
        Product(
            id = 1,
            name = "Laptop Dell Inspiron 15",
            price = 649.99,
            description = "Pantalla 15.6'' Full HD, Procesador Intel Core i5 de 12da generación, 16 GB de RAM y 512 GB SSD NVMe. Ideal para desarrollo y oficina.",
            category = "Electrónica",
            iconName = "laptop",
            stock = 8
        ),
        Product(
            id = 2,
            name = "Audífonos Sony WH-1000XM4",
            price = 279.50,
            description = "Cancelación de ruido activa líder en el mercado, hasta 30 horas de batería continua, micrófono con IA para llamadas cristalinas.",
            category = "Electrónica",
            iconName = "headphones",
            stock = 15
        ),
        Product(
            id = 3,
            name = "Camiseta Deportiva Dry-Fit",
            price = 24.99,
            description = "Tejido transpirable de secado rápido, costuras planas anti-rozaduras, disponible en varias tallas para alto rendimiento.",
            category = "Ropa y Moda",
            iconName = "shirt",
            stock = 30
        ),
        Product(
            id = 4,
            name = "Cafetera Espresso Automática",
            price = 129.00,
            description = "Bomba italiana de 15 bares de presión, vaporizador para leche espumada y depósito de agua desmontable de 1.5 litros.",
            category = "Hogar y Muebles",
            iconName = "coffee",
            stock = 6
        ),
        Product(
            id = 5,
            name = "Smartwatch Garmin Forerunner",
            price = 199.99,
            description = "GPS integrado, sensor óptico de ritmo cardíaco, resistencia al agua 5 ATM y perfiles avanzados de entrenamiento.",
            category = "Deportes",
            iconName = "watch",
            stock = 10
        ),
        Product(
            id = 6,
            name = "Balón de Fútbol Profesional N° 5",
            price = 34.50,
            description = "Construcción térmica sin costuras para trayectoria uniforme y mínima absorción de agua. Aprobado para competición.",
            category = "Deportes",
            iconName = "ball",
            stock = 22
        ),
        Product(
            id = 7,
            name = "Silla Ergonómica Transpirable",
            price = 185.00,
            description = "Respaldo de malla ergonómica con soporte lumbar ajustable, reposabrazos 3D y ruedas silenciosas para suelo delicado.",
            category = "Hogar y Muebles",
            iconName = "couch",
            stock = 5
        )
    )
}
