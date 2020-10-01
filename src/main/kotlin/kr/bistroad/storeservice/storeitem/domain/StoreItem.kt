package kr.bistroad.storeservice.storeitem.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("store_items")
data class StoreItem(
    @Id
    val id: UUID = UUID.randomUUID(),

    var store: StoreOfItem,
    var name: String,
    var description: String,
    var photoUri: String?,
    var price: Double,
    var stars: Double
)