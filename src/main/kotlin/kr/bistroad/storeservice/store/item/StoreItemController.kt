package kr.bistroad.storeservice.store.item

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import kr.bistroad.storeservice.exception.StoreItemNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Api(tags = ["/stores/**/items"])
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
    @ApiImplicitParam(
        name = "Authorization", value = "Access Token", required = true, paramType = "header",
        allowEmptyValue = false, dataTypeClass = String::class, example = "Bearer access_token"
    )
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    @ResponseStatus(HttpStatus.CREATED)
    fun postStoreItem(@PathVariable storeId: UUID, @RequestBody dto: StoreItemDto.CreateReq) =
        storeItemService.createStoreItem(storeId, dto)

    @PutMapping("/stores/{storeId}/items/{id}")
    @ApiOperation("\${swagger.doc.operation.store-item.put-store-item.description}")
    @ApiImplicitParam(
        name = "Authorization", value = "Access Token", required = true, paramType = "header",
        allowEmptyValue = false, dataTypeClass = String::class, example = "Bearer access_token"
    )
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
    @ApiOperation("\${swagger.doc.operation.store-item.patch-store-item.description}")
    @ApiImplicitParam(
        name = "Authorization", value = "Access Token", required = true, paramType = "header",
        allowEmptyValue = false, dataTypeClass = String::class, example = "Bearer access_token"
    )
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun patchStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID, @RequestBody dto: StoreItemDto.PatchReq) =
        storeItemService.patchStoreItem(storeId, id, dto)

    @DeleteMapping("/stores/{storeId}/items/{id}")
    @ApiOperation("\${swagger.doc.operation.store-item.delete-store-item.description}")
    @ApiImplicitParam(
        name = "Authorization", value = "Access Token", required = true, paramType = "header",
        allowEmptyValue = false, dataTypeClass = String::class, example = "Bearer access_token"
    )
    @PreAuthorize("isAuthenticated() and (( hasPermission(#storeId, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun deleteStoreItem(@PathVariable storeId: UUID, @PathVariable id: UUID): ResponseEntity<Void> =
        if (storeItemService.deleteStoreItem(storeId, id))
            ResponseEntity.noContent().build()
        else
            ResponseEntity.notFound().build()
}