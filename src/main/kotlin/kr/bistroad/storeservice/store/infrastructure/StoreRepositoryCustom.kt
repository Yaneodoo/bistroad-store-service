package kr.bistroad.storeservice.store.infrastructure

import kr.bistroad.storeservice.global.domain.Coordinate
import kr.bistroad.storeservice.store.domain.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.geo.GeoResult
import java.util.*

interface StoreRepositoryCustom {
    fun search(
        ownerId: UUID?,
        pageable: Pageable
    ): Page<Store>

    fun searchNearby(
        origin: Coordinate,
        distance: Double,
        pageable: Pageable
    ): Page<GeoResult<Store>>
}