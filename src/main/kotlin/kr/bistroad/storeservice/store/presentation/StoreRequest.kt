package kr.bistroad.storeservice.store.presentation

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import kr.bistroad.storeservice.store.application.StoreDto
import java.util.*

interface StoreRequest {
    @ApiModel("Store Search Request Params")
    data class SearchParams(
        val ownerId: UUID? = null
    )

    @ApiModel("Store Search Nearby Request Params")
    data class SearchNearbyParams(
        @ApiModelProperty(required = true)
        val originLat: Double,

        @ApiModelProperty(required = true)
        val originLng: Double,

        @ApiModelProperty(required = true)
        val radius: Double
    )

    @ApiModel("Store Request Location")
    data class Location(val lat: Double, val lng: Double)

    @ApiModel("Store Post Request Body")
    data class PostBody(
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val location: Location
    ) {
        fun toDtoForCreate(): StoreDto.ForCreate =
            StoreDto.ForCreate(
                ownerId = ownerId,
                name = name,
                phone = phone,
                description = description,
                location = StoreDto.ForCreate.Location(
                    lat = location.lat,
                    lng = location.lng
                )
            )
    }

    @ApiModel("Store Patch Request Body")
    data class PatchBody(
        val ownerId: UUID? = null,
        val name: String? = null,
        val phone: String? = null,
        val description: String? = null,
        val location: Location? = null
    ) {
        fun toDtoForUpdate(): StoreDto.ForUpdate =
            StoreDto.ForUpdate(
                ownerId = ownerId,
                name = name,
                phone = phone,
                description = description,
                location = StoreDto.ForUpdate.Location(
                    lat = location?.lat,
                    lng = location?.lng
                )
            )
    }
}