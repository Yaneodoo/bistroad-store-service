package kr.bistroad.storeservice.store.infrastructure

import kr.bistroad.storeservice.store.application.StoreDto
import kr.bistroad.storeservice.store.domain.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface StoreRepositoryCustom {
    fun search(dto: StoreDto.SearchReq, pageable: Pageable): Page<Store>
    fun searchNearby(dto: StoreDto.SearchNearbyReq, pageable: Pageable): Page<Store>
}