package kr.bistroad.storeservice.store.presentation

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.bistroad.storeservice.global.error.exception.StoreNotFoundException
import kr.bistroad.storeservice.store.application.StoreDto
import kr.bistroad.storeservice.store.application.StoreService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun getStores(dto: StoreDto.SearchReq, pageable: Pageable) = storeService.searchStores(dto, pageable)

    @GetMapping("/stores/nearby")
    @ApiOperation("\${swagger.doc.operation.store.get-nearby-stores.description}")
    fun getNearbyStores(dto: StoreDto.SearchNearbyReq, pageable: Pageable) =
        storeService.searchNearbyStores(dto, pageable)

    @PostMapping("/stores")
    @ApiOperation("\${swagger.doc.operation.store.post-store.description}")
    @PreAuthorize("isAuthenticated() and (( #dto.ownerId == principal.userId ) or hasRole('ROLE_ADMIN'))")
    @ResponseStatus(HttpStatus.CREATED)
    fun postStore(@RequestBody dto: StoreDto.CreateReq) = storeService.createStore(dto)

    @PutMapping("/stores/{id}")
    @ApiOperation("\${swagger.doc.operation.store.put-store.description}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#id, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun putStore(@PathVariable id: UUID, @RequestBody dto: StoreDto.PutReq): ResponseEntity<StoreDto.CruRes> {
        return if (storeService.readStore(id) == null) {
            ResponseEntity(
                storeService.createStore(
                    StoreDto.CreateReq(
                        id = id,
                        ownerId = dto.ownerId,
                        name = dto.name,
                        phone = dto.phone,
                        description = dto.description,
                        location = StoreDto.CreateReq.Location(
                            lat = dto.location.lat,
                            lng = dto.location.lng
                        )
                    )
                ), HttpStatus.CREATED
            )
        } else {
            ResponseEntity(storeService.patchStore(id, StoreDto.PatchReq(
                ownerId = dto.ownerId,
                name = dto.name,
                phone = dto.phone,
                description = dto.description,
                location = StoreDto.PatchReq.Location(
                    lat = dto.location.lat,
                    lng = dto.location.lng
                )
            )), HttpStatus.OK)
        }
    }

    @PatchMapping("/stores/{id}")
    @ApiOperation("\${swagger.doc.operation.store.patch-store.description}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#id, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun patchStore(@PathVariable id: UUID, @RequestBody dto: StoreDto.PatchReq): StoreDto.CruRes {
        return storeService.patchStore(id, dto)
    }

    @DeleteMapping("/stores/{id}")
    @ApiOperation("\${swagger.doc.operation.store.delete-store.description}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#id, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun deleteStore(@PathVariable id: UUID): ResponseEntity<Void> =
        if (storeService.deleteStore(id))
            ResponseEntity.noContent().build()
        else
            ResponseEntity.notFound().build()
}