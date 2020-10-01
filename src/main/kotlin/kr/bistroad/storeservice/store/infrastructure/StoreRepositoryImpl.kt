package kr.bistroad.storeservice.store.infrastructure

import kr.bistroad.storeservice.global.domain.Coordinate
import kr.bistroad.storeservice.global.util.toPage
import kr.bistroad.storeservice.store.domain.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.geo.GeoResult
import org.springframework.data.geo.Metrics
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.query.NearQuery
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.where
import org.springframework.stereotype.Component
import java.util.*

@Component
class StoreRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : StoreRepositoryCustom {
    override fun search(
        ownerId: UUID?,
        pageable: Pageable
    ): Page<Store> {
        val query = Query()
        if (ownerId != null) query.addCriteria(where(Store::ownerId).`is`(ownerId))

        return mongoTemplate.find<Store>(query.with(pageable)).toPage(pageable)
    }

    override fun searchNearby(
        origin: Coordinate,
        distance: Double,
        pageable: Pageable
    ): Page<GeoResult<Store>> {
        val query = NearQuery
            .near(GeoJsonPoint(origin.lng, origin.lat))
            .maxDistance(distance / 1000, Metrics.KILOMETERS)
            .with(pageable)
        val result = mongoTemplate.query(Store::class.java)
            .near(query)
            .all()
        return result.content.toPage(pageable)
    }
}