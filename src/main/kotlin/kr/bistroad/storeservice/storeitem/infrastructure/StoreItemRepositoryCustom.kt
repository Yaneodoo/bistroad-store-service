package kr.bistroad.storeservice.storeitem.infrastructure

import kr.bistroad.storeservice.storeitem.domain.StoreItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface StoreItemRepositoryCustom {
    fun search(storeId: UUID, pageable: Pageable): Page<StoreItem>
}