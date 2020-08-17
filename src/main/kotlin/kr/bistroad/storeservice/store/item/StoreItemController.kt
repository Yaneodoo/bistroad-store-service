package kr.bistroad.storeservice.store.item

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    @ResponseStatus(HttpStatus.CREATED)
    fun postStoreItem(@PathVariable storeId: UUID, @RequestBody dto: StoreItemDto.CreateReq) =
        storeItemService.createStoreItem(storeId, dto)

    @PutMapping("/stores/{storeId}/items/{id}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun putStoreItem(
        @PathVariable storeId: UUID,
        @PathVariable id: UUID,
        @RequestBody dto: StoreItemDto.PutReq
    ): ResponseEntity<StoreItemDto.CruRes> {
        val status = if (storeItemService.readStoreItem(storeId, id) == null) HttpStatus.CREATED else HttpStatus.OK
        return ResponseEntity(storeItemService.putStoreItem(storeId, id, dto), status)
    }

    @PatchMapping("/stores/{storeId}/items/{id}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun patchStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID, @RequestBody dto: StoreItemDto.PatchReq) =
        storeItemService.patchStoreItem(storeId, id, dto)

    @DeleteMapping("/stores/{storeId}/items/{id}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun deleteStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID): ResponseEntity<Void> =
        if (storeItemService.deleteStoreItem(storeId, id))
            ResponseEntity.noContent().build()
        else
            ResponseEntity.notFound().build()
}