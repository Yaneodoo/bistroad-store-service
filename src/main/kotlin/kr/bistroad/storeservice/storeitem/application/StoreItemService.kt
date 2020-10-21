package kr.bistroad.storeservice.storeitem.application

import kr.bistroad.storeservice.global.error.exception.StoreItemNotFoundException
import kr.bistroad.storeservice.global.error.exception.StoreNotFoundException
import kr.bistroad.storeservice.storeitem.domain.StoreItem
import kr.bistroad.storeservice.storeitem.domain.Store
import kr.bistroad.storeservice.storeitem.infrastructure.StoreItemRepository
import kr.bistroad.storeservice.storeitem.infrastructure.StoreRepository
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
            store = Store(store),
            name = dto.name,
            description = dto.description,
            price = dto.price,
            stars = 0.0
        )

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

    fun addOrderCount(storeId: UUID, id: UUID): StoreItemDto.ForResult {
        val item = storeItemRepository.findByStoreIdAndId(storeId, id) ?: throw StoreItemNotFoundException()

        item.orderCount++
        storeItemRepository.save(item)
        return StoreItemDto.ForResult.fromEntity(item)
    }

    fun subtractOrderCount(storeId: UUID, id: UUID): StoreItemDto.ForResult {
        val item = storeItemRepository.findByStoreIdAndId(storeId, id) ?: throw StoreItemNotFoundException()

        item.orderCount--
        storeItemRepository.save(item)
        return StoreItemDto.ForResult.fromEntity(item)
    }

    fun addReviewStar(storeId: UUID, id: UUID, reviewId: UUID, star: Int): StoreItemDto.ForResult {
        val item = storeItemRepository.findByStoreIdAndId(storeId, id) ?: throw StoreItemNotFoundException()

        item.reviewStars[reviewId] = star
        item.stars = item.reviewStars.values.average()

        storeItemRepository.save(item)
        return StoreItemDto.ForResult.fromEntity(item)
    }

    fun removeReviewStar(storeId: UUID, id: UUID, reviewId: UUID): StoreItemDto.ForResult {
        val item = storeItemRepository.findByStoreIdAndId(storeId, id) ?: throw StoreItemNotFoundException()

        item.reviewStars.remove(reviewId)
        item.stars = item.reviewStars.values.average()

        storeItemRepository.save(item)
        return StoreItemDto.ForResult.fromEntity(item)
    }

    fun deleteStoreItem(storeId: UUID, id: UUID): Boolean {
        val removed = storeItemRepository.removeByStoreIdAndId(storeId, id)
        return (removed > 0)
    }
}