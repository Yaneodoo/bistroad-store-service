package kr.bistroad.storeservice.store

import java.util.*

interface StoreDto {
    data class CreateReq(
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val location: Location
    ) {
        data class Location(val lat: Double, val lng: Double)
    }

    data class SearchReq(
            val ownerId: UUID?
    )

    data class PutReq(
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val location: Location
    ) {
        data class Location(val lat: Double, val lng: Double)
    }

    data class PatchReq(
        val ownerId: UUID?,
        val name: String?,
        val phone: String?,
        val description: String?,
        val location: Location?
    ) {
        data class Location(val lat: Double?, val lng: Double?)
    }

    data class CruRes(
        val id: UUID,
        val ownerId: UUID,
        val name: String,
        val phone: String,
        val description: String,
        val location: Location
    ) {
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