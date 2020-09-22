package kr.bistroad.storeservice.store.presentation

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.bistroad.storeservice.global.error.exception.StoreNotFoundException
import kr.bistroad.storeservice.store.application.StoreService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Api(tags = ["/stores"])
class StoreController(
    val storeService: StoreService
) {
    @GetMapping("/stores/{id}")
    @ApiOperation("\${swagger.doc.operation.store.get-store.description}")
    fun getStore(@PathVariable id: UUID) =
        storeService.readStore(id) ?: throw StoreNotFoundException()

    @GetMapping("/stores")
    @ApiOperation("\${swagger.doc.operation.store.get-stores.description}")
    fun getStores(params: StoreRequest.SearchParams, pageable: Pageable) =
        storeService.searchStores(
            ownerId = params.ownerId,
            pageable = pageable
        )

    @GetMapping("/stores/nearby")
    @ApiOperation("\${swagger.doc.operation.store.get-nearby-stores.description}")
    fun getNearbyStores(params: StoreRequest.SearchNearbyParams, pageable: Pageable) =
        storeService.searchNearbyStores(
            originLat = params.originLat,
            originLng = params.originLng,
            radius = params.radius,
            pageable = pageable
        )

    @PostMapping("/stores")
    @ApiOperation("\${swagger.doc.operation.store.post-store.description}")
    @PreAuthorize("isAuthenticated() and ( hasRole('ROLE_ADMIN') or ( #body.ownerId == principal.userId ) )")
    @ResponseStatus(HttpStatus.CREATED)
    fun postStore(@RequestBody body: StoreRequest.PostBody) =
        storeService.createStore(body.toDtoForCreate())

    @PatchMapping("/stores/{id}")
    @ApiOperation("\${swagger.doc.operation.store.patch-store.description}")
    @PreAuthorize("isAuthenticated() and ( hasRole('ROLE_ADMIN') or hasPermission(#id, 'Store', 'write') )")
    fun patchStore(@PathVariable id: UUID, @RequestBody body: StoreRequest.PatchBody) =
        storeService.updateStore(id, body.toDtoForUpdate())

    @DeleteMapping("/stores/{id}")
    @ApiOperation("\${swagger.doc.operation.store.delete-store.description}")
    @PreAuthorize("isAuthenticated() and ( hasRole('ROLE_ADMIN') or hasPermission(#id, 'Store', 'write') )")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStore(@PathVariable id: UUID) {
        val deleted = storeService.deleteStore(id)
        if (!deleted) throw StoreNotFoundException()
    }
}