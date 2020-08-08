package kr.bistroad.storeservice.store.item

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface StoreItemRepository : JpaRepository<StoreItem, UUID> {
    fun findByStoreIdAndId(storeId: UUID, id: UUID): StoreItem?

    @Transactional
    fun removeByStoreIdAndId(storeId: UUID, id: UUID): Long
}