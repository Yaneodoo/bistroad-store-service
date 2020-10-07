package kr.bistroad.storeservice.storeitem.application

import io.swagger.annotations.ApiModel
import kr.bistroad.storeservice.storeitem.domain.StoreItem
import java.util.*
import kr.bistroad.storeservice.storeitem.domain.Photo as DomainPhoto

interface StoreItemDto {
    data class ForCreate(
        val name: String,
        val description: String,
        val price: Double
    ) : StoreItemDto

    data class ForUpdate(
        val name: String? = null,
        val description: String? = null,
        val price: Double? = null
    ) : StoreItemDto

    @ApiModel("Store Item Response")
    data class ForResult(
        val id: UUID,
        val storeId: UUID,
        val name: String,
        val description: String,
        val price: Double,
        val photo: Photo? = null,
        val stars: Double,
        var orderCount: Int
    ) : StoreItemDto {
        @ApiModel("Store Item Response Photo")
        data class Photo(val sourceUrl: String, val thumbnailUrl: String) {
            constructor(domain: DomainPhoto): this(domain.sourceUrl, domain.thumbnailUrl)
        }

        companion object {
            fun fromEntity(item: StoreItem) = ForResult(
                id = item.id,
                storeId = item.store.id,
                name = item.name,
                description = item.description,
                photo = item.photo?.let(::Photo),
                price = item.price,
                stars = item.stars,
                orderCount = item.orderCount
            )
        }
    }
}