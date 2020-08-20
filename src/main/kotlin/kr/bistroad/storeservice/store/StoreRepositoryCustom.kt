package kr.bistroad.storeservice.store

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface StoreRepositoryCustom {
    fun search(dto: StoreDto.SearchReq, pageable: Pageable): Page<Store>
}