package kr.bistroad.storeservice.storeitem.presentation

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.bistroad.storeservice.global.error.exception.StoreItemNotFoundException
import kr.bistroad.storeservice.storeitem.application.StoreItemService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Api(tags = ["/stores/*/items"])
class StoreItemController(
    val storeItemService: StoreItemService
) {
    @GetMapping("/stores/{storeId}/items/{id}")
    @ApiOperation("\${swagger.doc.operation.store-item.get-store-item.description}")
    fun getStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID) =
        storeItemService.readStoreItem(storeId, id) ?: throw StoreItemNotFoundException()

    @GetMapping("/stores/{storeId}/items")
    @ApiOperation("\${swagger.doc.operation.store-item.get-store-items.description}")
    fun getStoreItems(@PathVariable storeId: UUID, pageable: Pageable) =
        storeItemService.searchStoreItems(storeId, pageable)

    @PostMapping("/stores/{storeId}/items")
    @ApiOperation("\${swagger.doc.operation.store-item.post-store-item.description}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    @ResponseStatus(HttpStatus.CREATED)
    fun postStoreItem(@PathVariable storeId: UUID, @RequestBody body: StoreItemRequest.PostBody) =
        storeItemService.createStoreItem(storeId, body.toDtoForCreate())

    @PatchMapping("/stores/{storeId}/items/{id}")
    @ApiOperation("\${swagger.doc.operation.store-item.patch-store-item.description}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun patchStoreItem(
        @PathVariable storeId: UUID,
        @PathVariable id: UUID,
        @RequestBody body: StoreItemRequest.PatchBody
    ) =
        storeItemService.updateStoreItem(storeId, id, body.toDtoForUpdate())

    @DeleteMapping("/stores/{storeId}/items/{id}")
    @ApiOperation("\${swagger.doc.operation.store-item.delete-store-item.description}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID) {
        val deleted = storeItemService.deleteStoreItem(storeId, id)
        if (!deleted) throw StoreItemNotFoundException()
    }

    @PostMapping("/stores/{storeId}/items/{id}/add-order-count")
    @ApiOperation(value = "Add order count for data integrity", hidden = true)
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    fun addOrderCount(@PathVariable storeId: UUID, @PathVariable id: UUID) =
        storeItemService.addOrderCount(storeId, id)

    @PostMapping("/stores/{storeId}/items/{id}/subtract-order-count")
    @ApiOperation(value = "Subtract order count for data integrity", hidden = true)
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    fun subtractOrderCount(@PathVariable storeId: UUID, @PathVariable id: UUID) =
        storeItemService.subtractOrderCount(storeId, id)

    @PostMapping("/stores/{storeId}/items/{id}/add-review-star")
    @ApiOperation(value = "Add review star for data integrity", hidden = true)
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    fun addReviewStar(
        @PathVariable storeId: UUID,
        @PathVariable id: UUID,
        @RequestBody body: StoreItemRequest.AddReviewStarBody
    ) =
        storeItemService.addReviewStar(storeId, id, body.reviewId, body.stars)

    @PostMapping("/stores/{storeId}/items/{id}/remove-review-star")
    @ApiOperation(value = "Remove review star for data integrity", hidden = true)
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    fun removeReviewStar(
        @PathVariable storeId: UUID,
        @PathVariable id: UUID,
        @RequestBody body: StoreItemRequest.RemoveReviewStarBody
    ) =
        storeItemService.removeReviewStar(storeId, id, body.reviewId)
}