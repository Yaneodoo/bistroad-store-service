package kr.bistroad.storeservice.store.item

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface StoreItemRepositoryCustom {
    fun search(storeId: UUID, pageable: Pageable): Page<StoreItem>
}