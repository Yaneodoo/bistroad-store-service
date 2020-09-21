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
    fun createStore(dto: StoreDto.ForCreate): StoreDto.ForResult {
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
        return StoreDto.ForResult.fromEntity(store)
    }

    fun readStore(id: UUID): StoreDto.ForResult? {
        val store = storeRepository.findByIdOrNull(id) ?: return null
        return StoreDto.ForResult.fromEntity(store)
    }

    fun searchStores(
        ownerId: UUID?,
        pageable: Pageable
    ): List<StoreDto.ForResult> =
        storeRepository.search(
            ownerId = ownerId,
            pageable = pageable
        ).content
            .map(StoreDto.ForResult.Companion::fromEntity)

    fun searchNearbyStores(
        originLat: Double,
        originLng: Double,
        radius: Double,
        pageable: Pageable
    ): List<StoreDto.ForResult> {
        return storeRepository.searchNearby(
            originLat = originLat,
            originLng = originLng,
            radius = radius,
            pageable = pageable
        ).content
            .map(StoreDto.ForResult.Companion::fromEntity)
    }

    fun updateStore(id: UUID, dto: StoreDto.ForUpdate): StoreDto.ForResult {
        val store = storeRepository.findByIdOrNull(id) ?: throw StoreNotFoundException()

        if (dto.ownerId != null) store.ownerId = dto.ownerId
        if (dto.name != null) store.name = dto.name
        if (dto.phone != null) store.phone = dto.phone
        if (dto.description != null) store.description = dto.description
        if (dto.location?.lat != null) store.locationLat = dto.location.lat
        if (dto.location?.lng != null) store.locationLng = dto.location.lng

        storeRepository.save(store)
        return StoreDto.ForResult.fromEntity(store)
    }

    fun deleteStore(id: UUID): Boolean {
        val numDeleted = storeRepository.removeById(id)
        return numDeleted > 0
    }
}