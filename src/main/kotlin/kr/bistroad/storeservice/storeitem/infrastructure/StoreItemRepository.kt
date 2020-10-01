package kr.bistroad.storeservice.storeitem.infrastructure

import kr.bistroad.storeservice.storeitem.domain.StoreItem
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface StoreItemRepository : MongoRepository<StoreItem, UUID>, StoreItemRepositoryCustom {
    fun findByStoreIdAndId(storeId: UUID, id: UUID): StoreItem?
    fun removeByStoreIdAndId(storeId: UUID, id: UUID): Long
}