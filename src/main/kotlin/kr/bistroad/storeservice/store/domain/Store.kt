package kr.bistroad.storeservice.store.domain

import kr.bistroad.storeservice.global.domain.Coordinate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("stores")
data class Store(
    @Id
    val id: UUID = UUID.randomUUID(),

    var owner: Owner,
    var name: String,
    var phone: String,
    var description: String,
    var address: String,
    var photo: Photo? = null,

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    var location: Coordinate
)