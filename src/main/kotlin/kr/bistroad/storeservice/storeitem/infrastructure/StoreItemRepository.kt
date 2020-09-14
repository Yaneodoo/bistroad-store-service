package kr.bistroad.storeservice.storeitem.infrastructure

import kr.bistroad.storeservice.storeitem.domain.StoreItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface StoreItemRepository : JpaRepository<StoreItem, UUID>, StoreItemRepositoryCustom {
    fun findByStoreIdAndId(storeId: UUID, id: UUID): StoreItem?

    @Transactional
    fun removeByStoreIdAndId(storeId: UUID, id: UUID): Long
}