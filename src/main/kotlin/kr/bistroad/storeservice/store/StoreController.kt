package kr.bistroad.storeservice.store

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class StoreController(
    val storeService: StoreService
) {
    @GetMapping("/stores/{id}")
    fun getStore(@PathVariable id: UUID) = storeService.readStore(id)

    @GetMapping("/stores")
    fun getStores() = storeService.searchStores()

    @PostMapping("/stores")
    @PreAuthorize("isAuthenticated() and (( #dto.ownerId == principal.userId ) or hasRole('ROLE_ADMIN'))")
    fun postStore(@RequestBody dto: StoreDto.CreateReq) = storeService.createStore(dto)

    @PutMapping("/stores/{id}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#id, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun putStore(@PathVariable id: UUID, @RequestBody dto: StoreDto.PutReq): StoreDto.CruRes {
        return storeService.putStore(id, dto)
    }

    @PatchMapping("/stores/{id}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#id, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun patchStore(@PathVariable id: UUID, @RequestBody dto: StoreDto.PatchReq): StoreDto.CruRes {
        return storeService.patchStore(id, dto)
    }

    @DeleteMapping("/stores/{id}")
    @PreAuthorize("isAuthenticated() and (( hasPermission(#id, 'Store', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun deleteStore(@PathVariable id: UUID): Boolean {
        return storeService.deleteStore(id)
    }
}