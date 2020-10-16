package kr.bistroad.storeservice.store.presentation

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.bistroad.storeservice.store.application.StorePhotoService
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@Api(tags = ["/stores/*/photo"])
class StorePhotoController(
    private val storePhotoService: StorePhotoService
) {
    @PostMapping("/stores/{id}/photo", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ApiOperation("\${swagger.doc.operation.store.post-store-photo.description}")
    @PreAuthorize("isAuthenticated() and ( hasRole('ROLE_ADMIN') or hasPermission(#id, 'Store', 'write') )")
    fun postPhoto(@PathVariable id: UUID, @RequestPart file: MultipartFile) =
        storePhotoService.upload(id, file)
}