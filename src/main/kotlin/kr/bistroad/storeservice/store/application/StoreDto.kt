package kr.bistroad.storeservice.store.application

import io.swagger.annotations.ApiModel
import kr.bistroad.storeservice.store.domain.Store
import java.util.*

interface StoreDto {
    data class ForCreate(
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val location: Location
    ) : StoreDto {
        data class Location(val lat: Double, val lng: Double)
    }

    data class ForUpdate(
        val ownerId: UUID? = null,
        val name: String? = null,
        val phone: String? = null,
        val description: String? = null,
        val location: Location? = null
    ) : StoreDto {
        data class Location(val lat: Double?, val lng: Double?)
    }

    @ApiModel("Store Response")
    data class ForResult(
        val id: UUID,
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val location: Location
    ) : StoreDto {
        @ApiModel("Store Response Location")
        data class Location(val lat: Double, val lng: Double)

        companion object {
            fun fromEntity(store: Store) = ForResult(
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