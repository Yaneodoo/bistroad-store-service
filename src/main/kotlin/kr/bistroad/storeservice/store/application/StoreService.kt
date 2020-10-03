package kr.bistroad.storeservice.store.application

import kr.bistroad.storeservice.global.domain.Coordinate
import kr.bistroad.storeservice.global.error.exception.StoreNotFoundException
import kr.bistroad.storeservice.store.domain.Owner
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
            owner = Owner(dto.ownerId),
            name = dto.name,
            phone = dto.phone,
            description = dto.description,
            address = dto.address,
            location = Coordinate(dto.location.lat, dto.location.lng)
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
    ): List<StoreDto.ForNearbyResult> =
        storeRepository.searchNearby(
            origin = Coordinate(lat = originLat, lng = originLng),
            distance = radius,
            pageable = pageable
        ).content
            .map(StoreDto.ForNearbyResult.Companion::fromEntity)

    fun updateStore(id: UUID, dto: StoreDto.ForUpdate): StoreDto.ForResult {
        val store = storeRepository.findByIdOrNull(id) ?: throw StoreNotFoundException()

        if (dto.ownerId != null) store.owner = Owner(dto.ownerId)
        if (dto.name != null) store.name = dto.name
        if (dto.phone != null) store.phone = dto.phone
        if (dto.description != null) store.description = dto.description
        if (dto.address != null) store.address = dto.address
        if (dto.location != null) store.location = Coordinate(dto.location.lat, dto.location.lng)

        storeRepository.save(store)
        return StoreDto.ForResult.fromEntity(store)
    }

    fun deleteStore(id: UUID): Boolean {
        val numDeleted = storeRepository.removeById(id)
        return numDeleted > 0
    }
}