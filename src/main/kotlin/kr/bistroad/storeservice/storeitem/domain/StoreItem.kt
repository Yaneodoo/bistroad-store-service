package kr.bistroad.storeservice.storeitem.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("store_items")
data class StoreItem(
    @Id
    val id: UUID = UUID.randomUUID(),

    var store: Store,
    var name: String,
    var description: String,
    var photo: Photo? = null,
    var price: Double,
    var stars: Double = 0.0,
    val reviewStars: MutableMap<UUID, Int> = mutableMapOf(),
    var orderCount: Int = 0
)