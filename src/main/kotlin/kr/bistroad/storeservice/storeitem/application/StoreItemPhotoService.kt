package kr.bistroad.storeservice.storeitem.application

import com.google.cloud.storage.Acl
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import kr.bistroad.storeservice.global.error.exception.InvalidFileTypeException
import kr.bistroad.storeservice.global.error.exception.StoreItemNotFoundException
import kr.bistroad.storeservice.global.util.ThumbnailUtils
import kr.bistroad.storeservice.storeitem.domain.Photo
import kr.bistroad.storeservice.storeitem.infrastructure.StoreItemRepository
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*

@Service
class StoreItemPhotoService(
    @Value("\${gcs.bucket-name:bistroad-kr-photo-bucket}")
    private val bucketName: String,

    @Value("\${app.thumbnail.min-width:500}")
    private val minWidth: Int,

    @Value("\${app.thumbnail.min-height:500}")
    private val minHeight: Int,

    @Autowired(required = false)
    private val storage: Storage? = null,

    private val storeItemRepository: StoreItemRepository
) {
    fun upload(storeId: UUID, itemId: UUID, file: MultipartFile) {
        require(file.contentType in ALLOWED_CONTENT_TYPES) { throw InvalidFileTypeException() }
        check(storage != null)

        val item = storeItemRepository.findByStoreIdAndId(storeId, itemId) ?: throw StoreItemNotFoundException()

        val sourceBlob = createBlobFrom(file.inputStream, randomNameFor(file))
        val thumbnailBlob = ByteArrayInputStream(ThumbnailUtils.optimize(file.bytes, minWidth, minHeight))
            .use {
                createBlobFrom(it, randomNameFor(file))
            }

        item.photo = Photo(
            sourceUrl = "$PUBLIC_URL/$bucketName/${sourceBlob.name}",
            thumbnailUrl = "$PUBLIC_URL/$bucketName/${thumbnailBlob.name}"
        )
        storeItemRepository.save(item)
    }

    private fun createBlobFrom(inputStream: InputStream, fileName: String) =
        storage!!.createFrom(
            BlobInfo.newBuilder(bucketName, fileName)
                .setAcl(listOf(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                .build(),
            inputStream
        )

    private fun randomNameFor(file: MultipartFile) =
        RandomStringUtils.randomAlphanumeric(8) + '.' + FilenameUtils.getExtension(file.originalFilename!!)

    companion object {
        val ALLOWED_CONTENT_TYPES: List<String> = listOf(
            "image/png", "image/jpeg", "image/gif"
        )

        const val PUBLIC_URL = "https://storage.googleapis.com"
    }
}