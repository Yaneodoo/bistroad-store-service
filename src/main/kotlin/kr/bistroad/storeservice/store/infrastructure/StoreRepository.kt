package kr.bistroad.storeservice.store.infrastructure

import kr.bistroad.storeservice.store.domain.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface StoreRepository : JpaRepository<Store, UUID>, StoreRepositoryCustom {
    @Transactional
    fun removeById(id: UUID): Long
}