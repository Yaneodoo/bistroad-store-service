package kr.bistroad.storeservice.store.item.photo

import io.swagger.annotations.Api
import kr.bistroad.storeservice.store.item.StoreItemService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@Api(tags = ["/stores/**/items/**/upload-photo"])
class StoreItemPhotoController(
    val storeItemService: StoreItemService
) {
    @PostMapping("/stores/{storeId}/items/{id}/upload-photo")
    fun uploadPhoto(@PathVariable storeId: UUID, @PathVariable id: UUID): Nothing = TODO("WIP")
}