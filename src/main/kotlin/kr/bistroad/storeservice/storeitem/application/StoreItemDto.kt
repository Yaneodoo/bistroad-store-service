package kr.bistroad.storeservice.storeitem.application

import io.swagger.annotations.ApiModel
import kr.bistroad.storeservice.storeitem.domain.StoreItem
import java.util.*

interface StoreItemDto {
    @ApiModel("Store Item Create Request")
    data class CreateReq(
        val id: UUID?,
        val name: String,
        val description: String,
        val price: Double
    )

    @ApiModel("Store Item Put Request")
    data class PutReq(
        val name: String,
        val description: String,
        val price: Double
    )

    @ApiModel("Store Item Patch Request")
    data class PatchReq(
        val name: String?,
        val description: String?,
        val price: Double?
    )

    @ApiModel("Store Item Response")
    data class CruRes(
        val id: UUID,
        val storeId: UUID,
        val name: String,
        val description: String,
        val price: Double,
        val photoUri: String?,
        val stars: Double
    ) {
        companion object {
            fun fromEntity(item: StoreItem) = CruRes(
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