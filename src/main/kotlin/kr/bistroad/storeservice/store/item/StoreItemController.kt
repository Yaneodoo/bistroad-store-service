package kr.bistroad.storeservice.store.item

import org.springframework.security.access.prepost.PreAuthorize
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
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun postStoreItem(@PathVariable storeId: UUID, @RequestBody dto: StoreItemDto.CreateReq) =
        storeItemService.createStoreItem(storeId, dto)

    @PutMapping("/stores/{storeId}/items/{id}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun putStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID, @RequestBody dto: StoreItemDto.PutReq) =
        storeItemService.putStoreItem(storeId, id, dto)

    @PatchMapping("/stores/{storeId}/items/{id}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun patchStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID, @RequestBody dto: StoreItemDto.PatchReq) =
        storeItemService.patchStoreItem(storeId, id, dto)

    @DeleteMapping("/stores/{storeId}/items/{id}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun deleteStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID) =
        storeItemService.deleteStoreItem(storeId, id)
}