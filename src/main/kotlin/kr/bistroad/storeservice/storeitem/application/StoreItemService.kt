package kr.bistroad.storeservice.storeitem.application

import kr.bistroad.storeservice.global.error.exception.StoreItemNotFoundException
import kr.bistroad.storeservice.global.error.exception.StoreNotFoundException
import kr.bistroad.storeservice.store.infrastructure.StoreRepository
import kr.bistroad.storeservice.storeitem.domain.StoreItem
import kr.bistroad.storeservice.storeitem.infrastructure.StoreItemRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class StoreItemService(
    private val storeRepository: StoreRepository,
    private val storeItemRepository: StoreItemRepository
) {
    fun createStoreItem(storeId: UUID, dto: StoreItemDto.ForCreate): StoreItemDto.ForResult {
        val store = storeRepository.findByIdOrNull(storeId) ?: throw StoreNotFoundException()

        val item = StoreItem(
                name = dto.name,
                description = dto.description,
                photoUri = null,
                price = dto.price,
                stars = 0.0
        ).apply {
            store.addMenuItem(this)
        }

        storeItemRepository.save(item)
        return StoreItemDto.ForResult.fromEntity(item)
    }

    fun readStoreItem(storeId: UUID, id: UUID): StoreItemDto.ForResult? {
        val item = storeItemRepository.findByStoreIdAndId(storeId, id) ?: return null
        return StoreItemDto.ForResult.fromEntity(item)
    }

    fun searchStoreItems(storeId: UUID, pageable: Pageable): List<StoreItemDto.ForResult> {
        return storeItemRepository.search(storeId, pageable)
            .content.map(StoreItemDto.ForResult.Companion::fromEntity)
    }

    fun updateStoreItem(storeId: UUID, id: UUID, dto: StoreItemDto.ForUpdate): StoreItemDto.ForResult {
        val item = storeItemRepository.findByStoreIdAndId(storeId, id) ?: throw StoreItemNotFoundException()

        if (dto.name != null) item.name = dto.name
        if (dto.description != null) item.description = dto.description
        if (dto.price != null) item.price = dto.price

        storeItemRepository.save(item)
        return StoreItemDto.ForResult.fromEntity(item)
    }

    fun deleteStoreItem(storeId: UUID, id: UUID): Boolean {
        val item = storeItemRepository.findByStoreIdAndId(storeId, id) ?: return false
        val store = item.store!!

        store.removeMenuItem(item)
        storeRepository.save(store)
        return true
    }
}