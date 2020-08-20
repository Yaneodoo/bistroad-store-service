package kr.bistroad.storeservice.store.item

import kr.bistroad.storeservice.exception.StoreItemNotFoundException
import kr.bistroad.storeservice.exception.StoreNotFoundException
import kr.bistroad.storeservice.store.StoreRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class StoreItemService(
        private val storeRepository: StoreRepository,
        private val storeItemRepository: StoreItemRepository
) {
    fun createStoreItem(storeId: UUID, dto: StoreItemDto.CreateReq): StoreItemDto.CruRes {
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
        return StoreItemDto.CruRes.fromEntity(item)
    }

    fun readStoreItem(storeId: UUID, id: UUID): StoreItemDto.CruRes? {
        val item = storeItemRepository.findByStoreIdAndId(storeId, id) ?: return null
        return StoreItemDto.CruRes.fromEntity(item)
    }

    fun searchStoreItems(storeId: UUID, pageable: Pageable): List<StoreItemDto.CruRes> {
        return storeItemRepository.search(storeId, pageable)
            .content.map(StoreItemDto.CruRes.Companion::fromEntity)
    }

    fun putStoreItem(storeId: UUID, id: UUID, dto: StoreItemDto.PutReq): StoreItemDto.CruRes {
        val original = storeItemRepository.findByStoreIdAndId(storeId, id)
        val item = StoreItem(
            id = id,
            store = original?.store,
            name = dto.name,
            description = dto.description,
            photoUri = original?.photoUri,
            price = dto.price,
            stars = original?.stars ?: 0.0
        ).apply {
            if (original == null) {
                val store = storeRepository.findByIdOrNull(storeId) ?: throw StoreNotFoundException()
                store.addMenuItem(this)
            }
        }

        storeItemRepository.save(item)
        return StoreItemDto.CruRes.fromEntity(item)
    }

    fun patchStoreItem(storeId: UUID, id: UUID, dto: StoreItemDto.PatchReq): StoreItemDto.CruRes {
        val item = storeItemRepository.findByStoreIdAndId(storeId, id) ?: throw StoreItemNotFoundException()

        if (dto.name != null) item.name = dto.name
        if (dto.description != null) item.description = dto.description
        if (dto.price != null) item.price = dto.price

        storeItemRepository.save(item)
        return StoreItemDto.CruRes.fromEntity(item)
    }

    fun deleteStoreItem(storeId: UUID, id: UUID): Boolean {
        val numDeleted = storeItemRepository.removeByStoreIdAndId(storeId, id)
        return numDeleted > 0
    }
}