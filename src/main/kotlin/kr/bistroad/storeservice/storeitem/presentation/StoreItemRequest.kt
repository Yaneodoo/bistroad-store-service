package kr.bistroad.storeservice.storeitem.presentation

import io.swagger.annotations.ApiModel
import kr.bistroad.storeservice.storeitem.application.StoreItemDto
import java.util.*

interface StoreItemRequest {
    @ApiModel("Store Item Create Body")
    data class PostBody(
        val name: String,
        val description: String,
        val price: Double
    ) {
        fun toDtoForCreate(): StoreItemDto.ForCreate =
            StoreItemDto.ForCreate(
                name = name,
                description = description,
                price = price
            )
    }

    @ApiModel("Store Item Put Body")
    data class PutBody(
        val name: String,
        val description: String,
        val price: Double
    ) {
        fun toDtoForCreate(id: UUID): StoreItemDto.ForCreate =
            StoreItemDto.ForCreate(
                id = id,
                name = name,
                description = description,
                price = price
            )

        fun toDtoForUpdate(): StoreItemDto.ForUpdate =
            StoreItemDto.ForUpdate(
                name = name,
                description = description,
                price = price
            )
    }

    @ApiModel("Store Item Patch Body")
    data class PatchBody(
        val name: String?,
        val description: String?,
        val price: Double?
    ) {
        fun toDtoForUpdate(): StoreItemDto.ForUpdate =
            StoreItemDto.ForUpdate(
                name = name,
                description = description,
                price = price
            )
    }
}