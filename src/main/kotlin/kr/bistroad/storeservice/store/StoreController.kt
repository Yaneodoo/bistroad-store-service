package kr.bistroad.storeservice.store

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
    fun postStore(@RequestBody dto: StoreDto.CreateReq) = storeService.createStore(dto)

    @PutMapping("/stores/{id}")
    fun putStore(@PathVariable id: UUID, @RequestBody dto: StoreDto.PutReq) = storeService.putStore(id, dto)

    @PatchMapping("/stores/{id}")
    fun patchStore(@PathVariable id: UUID, @RequestBody dto: StoreDto.PatchReq) = storeService.patchStore(id, dto)

    @DeleteMapping("/stores/{id}")
    fun deleteStore(@PathVariable id: UUID) = storeService.deleteStore(id)
}