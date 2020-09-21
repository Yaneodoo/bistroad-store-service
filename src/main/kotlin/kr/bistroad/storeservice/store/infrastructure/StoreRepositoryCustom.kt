package kr.bistroad.storeservice.store.infrastructure

import kr.bistroad.storeservice.store.domain.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface StoreRepositoryCustom {
    fun search(
        ownerId: UUID?,
        pageable: Pageable
    ): Page<Store>

    fun searchNearby(
        originLat: Double,
        originLng: Double,
        radius: Double,
        pageable: Pageable
    ): Page<Store>
}