package kr.bistroad.storeservice.store

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class StoreService(
    private val storeRepository: StoreRepository
) {
    fun createStore(dto: StoreDto.CreateReq): StoreDto.CruRes {
        val store = Store(
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

    fun searchStores(): List<StoreDto.CruRes> {
        return storeRepository.findAll()
            .map(StoreDto.CruRes.Companion::fromEntity)
    }

    fun putStore(id: UUID, dto: StoreDto.PutReq): StoreDto.CruRes {
        val store = Store(
            id = id,
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

    fun patchStore(id: UUID, dto: StoreDto.PatchReq): StoreDto.CruRes {
        val store = storeRepository.findByIdOrNull(id) ?: error("Store not found")

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