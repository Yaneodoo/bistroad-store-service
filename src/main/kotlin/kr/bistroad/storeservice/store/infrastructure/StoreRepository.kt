package kr.bistroad.storeservice.store.infrastructure

import kr.bistroad.storeservice.store.domain.Store
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface StoreRepository : MongoRepository<Store, UUID>, StoreRepositoryCustom {
    fun removeById(id: UUID): Long
}