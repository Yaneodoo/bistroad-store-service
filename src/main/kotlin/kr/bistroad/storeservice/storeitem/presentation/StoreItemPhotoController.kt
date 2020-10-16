package kr.bistroad.storeservice.storeitem.presentation

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.bistroad.storeservice.storeitem.application.StoreItemPhotoService
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@Api(tags = ["/stores/*/items/*/photo"])
class StoreItemPhotoController(
    private val storeItemPhotoService: StoreItemPhotoService
) {
    @PostMapping("/stores/{storeId}/items/{id}/photo", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ApiOperation("\${swagger.doc.operation.store-item.post-store-item-photo.description}")
    @PreAuthorize("isAuthenticated() and ( hasRole('ROLE_ADMIN') or hasPermission(#id, 'Store', 'write') )")
    fun postPhoto(@PathVariable storeId: UUID, @PathVariable id: UUID, @RequestPart file: MultipartFile) =
        storeItemPhotoService.upload(storeId, id, file)
}