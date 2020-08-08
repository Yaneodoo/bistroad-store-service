package kr.bistroad.storeservice.store.item

import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class StoreItemController(
        val storeItemService: StoreItemService
) {
    @GetMapping("/stores/{storeId}/items/{id}")
    fun getStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID) =
            storeItemService.readStoreItem(storeId, id)

    @GetMapping("/stores/{storeId}/items")
    fun getStoreItems() =
            storeItemService.searchStoreItems()

    @PostMapping("/stores/{storeId}/items")
    fun postStoreItem(@PathVariable storeId: UUID, @RequestBody dto: StoreItemDto.CreateReq) =
            storeItemService.createStoreItem(storeId, dto)

    @PutMapping("/stores/{storeId}/items/{id}")
    fun putStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID, @RequestBody dto: StoreItemDto.PutReq) =
            storeItemService.putStoreItem(storeId, id, dto)

    @PatchMapping("/stores/{storeId}/items/{id}")
    fun patchStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID, @RequestBody dto: StoreItemDto.PatchReq) =
            storeItemService.patchStoreItem(storeId, id, dto)

    @DeleteMapping("/stores/{storeId}/items/{id}")
    fun deleteStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID) =
            storeItemService.deleteStoreItem(storeId, id)
}