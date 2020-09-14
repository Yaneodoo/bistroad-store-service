package kr.bistroad.storeservice.store.application

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import kr.bistroad.storeservice.store.domain.Store
import java.util.*

interface StoreDto {
    @ApiModel("Store Create Request")
    data class CreateReq(
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val location: Location
    ) {
        @ApiModel("Store Create Request Location")
        data class Location(val lat: Double, val lng: Double)
    }

    @ApiModel("Store Search Request")
    data class SearchReq(
        val ownerId: UUID?
    )

    @ApiModel("Store Search Nearby Request")
    data class SearchNearbyReq(
        @ApiModelProperty(required = true)
        val originLat: Double,

        @ApiModelProperty(required = true)
        val originLng: Double,

        @ApiModelProperty(required = true)
        val radius: Double
    )

    @ApiModel("Store Put Request")
    data class PutReq(
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val location: Location
    ) {
        @ApiModel("Store Put Request Location")
        data class Location(val lat: Double, val lng: Double)
    }

    @ApiModel("Store Patch Request")
    data class PatchReq(
        val ownerId: UUID?,
        val name: String?,
        val phone: String?,
        val description: String?,
        val location: Location?
    ) {
        @ApiModel("Store Patch Request Location")
        data class Location(val lat: Double?, val lng: Double?)
    }

    @ApiModel("Store Response")
    data class CruRes(
        val id: UUID,
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val location: Location
    ) {
        @ApiModel("Store Response Location")
        data class Location(val lat: Double, val lng: Double)

        companion object {
            fun fromEntity(store: Store) = CruRes(
                id = store.id!!,
                ownerId = store.ownerId,
                name = store.name,
                phone = store.phone,
                description = store.description,
                location = Location(
                    lat = store.locationLat,
                    lng = store.locationLng
                )
            )
        }
    }
}