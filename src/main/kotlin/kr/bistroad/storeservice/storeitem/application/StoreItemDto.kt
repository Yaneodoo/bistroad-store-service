package kr.bistroad.storeservice.storeitem.application

import io.swagger.annotations.ApiModel
import kr.bistroad.storeservice.storeitem.domain.StoreItem
import java.util.*

interface StoreItemDto {
    data class ForCreate(
        val id: UUID? = null,
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
        val photoUri: String? = null,
        val stars: Double
    ) : StoreItemDto {
        companion object {
            fun fromEntity(item: StoreItem) = ForResult(
                id = item.id!!,
                storeId = item.store!!.id!!,
                name = item.name,
                description = item.description,
                photoUri = item.photoUri,
                price = item.price,
                stars = item.stars
            )
        }
    }
}