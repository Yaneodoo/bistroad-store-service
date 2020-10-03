package kr.bistroad.storeservice.store.application

import io.swagger.annotations.ApiModel
import kr.bistroad.storeservice.store.domain.Store
import org.springframework.data.geo.GeoResult
import org.springframework.data.geo.Metrics
import java.util.*

interface StoreDto {
    data class ForCreate(
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val address: String,
        val location: Location
    ) : StoreDto {
        data class Location(val lat: Double, val lng: Double)
    }

    data class ForUpdate(
        val ownerId: UUID? = null,
        val name: String? = null,
        val phone: String? = null,
        val description: String? = null,
        val address: String? = null,
        val location: Location? = null
    ) : StoreDto {
        data class Location(val lat: Double, val lng: Double)
    }

    @ApiModel("Store Response")
    data class ForResult(
        val id: UUID,
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val address: String,
        val photoUri: String?,
        val location: Location
    ) : StoreDto {
        @ApiModel("Store Response Location")
        data class Location(val lat: Double, val lng: Double)

        companion object {
            fun fromEntity(store: Store) = ForResult(
                id = store.id,
                ownerId = store.owner.id,
                name = store.name,
                phone = store.phone,
                description = store.description,
                address = store.address,
                photoUri = store.photoUri,
                location = Location(
                    lat = store.location.lat,
                    lng = store.location.lng
                )
            )
        }
    }

    @ApiModel("Store Nearby Response")
    data class ForNearbyResult(
        val id: UUID,
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val address: String,
        val photoUri: String?,
        val location: Location,
        val distance: Double
    ) : StoreDto {
        @ApiModel("Store Nearby Response Location")
        data class Location(val lat: Double, val lng: Double)

        companion object {
            fun fromEntity(store: GeoResult<Store>) = ForNearbyResult(
                id = store.content.id,
                ownerId = store.content.owner.id,
                name = store.content.name,
                phone = store.content.phone,
                description = store.content.description,
                address = store.content.address,
                photoUri = store.content.photoUri,
                location = Location(
                    lat = store.content.location.lat,
                    lng = store.content.location.lng
                ),
                distance = store.distance.`in`(Metrics.KILOMETERS).value / 1000
            )
        }
    }
}