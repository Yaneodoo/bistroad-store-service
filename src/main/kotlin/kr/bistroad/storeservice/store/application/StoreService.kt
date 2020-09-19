package kr.bistroad.storeservice.store.application

import kr.bistroad.storeservice.global.error.exception.StoreNotFoundException
import kr.bistroad.storeservice.store.domain.Store
import kr.bistroad.storeservice.store.infrastructure.StoreRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class StoreService(
    private val storeRepository: StoreRepository
) {
    fun createStore(dto: StoreDto.CreateReq): StoreDto.CruRes {
        val store = Store(
            id = dto.id,
            ownerId = dto.ownerId,
            name = dto.name,
            phone = dto.phone,
            description = dto.description,
            locationLat = dto.location.lat,
            locationLng = dto.location.lng
        )
        storeRepository.save(store)
        return StoreDto.CruRes.fromEntity(store)
    }

    fun readStore(id: UUID): StoreDto.CruRes? {
        val store = storeRepository.findByIdOrNull(id) ?: return null
        return StoreDto.CruRes.fromEntity(store)
    }

    fun searchStores(dto: StoreDto.SearchReq, pageable: Pageable): List<StoreDto.CruRes> {
        return storeRepository.search(dto, pageable)
            .content.map(StoreDto.CruRes.Companion::fromEntity)
    }

    fun searchNearbyStores(dto: StoreDto.SearchNearbyReq, pageable: Pageable): List<StoreDto.CruRes> {
        return storeRepository.searchNearby(dto, pageable)
            .content.map(StoreDto.CruRes.Companion::fromEntity)
    }

    fun patchStore(id: UUID, dto: StoreDto.PatchReq): StoreDto.CruRes {
        val store = storeRepository.findByIdOrNull(id) ?: throw StoreNotFoundException()

        if (dto.ownerId != null) store.ownerId = dto.ownerId
        if (dto.name != null) store.name = dto.name
        if (dto.phone != null) store.phone = dto.phone
        if (dto.description != null) store.description = dto.description
        if (dto.location?.lat != null) store.locationLat = dto.location.lat
        if (dto.location?.lng != null) store.locationLng = dto.location.lng

        storeRepository.save(store)
        return StoreDto.CruRes.fromEntity(store)
    }

    fun deleteStore(id: UUID): Boolean {
        val numDeleted = storeRepository.removeById(id)
        return numDeleted > 0
    }
}