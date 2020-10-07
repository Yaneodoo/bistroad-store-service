package kr.bistroad.storeservice.storeitem.application

import com.google.cloud.storage.Acl
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import kr.bistroad.storeservice.global.error.exception.InvalidFileTypeException
import kr.bistroad.storeservice.global.error.exception.StoreItemNotFoundException
import kr.bistroad.storeservice.storeitem.domain.Photo
import kr.bistroad.storeservice.storeitem.infrastructure.StoreItemRepository
import net.coobird.thumbnailator.Thumbnails
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

@Service
class StoreItemPhotoService(
    @Value("\${gcs.bucket-name:bistroad-kr-photo-bucket}")
    private val bucketName: String,

    @Autowired(required = false)
    private val storage: Storage? = null,

    private val storeItemRepository: StoreItemRepository
) {
    fun upload(storeId: UUID, itemId: UUID, file: MultipartFile) {
        require(file.contentType in ALLOWED_CONTENT_TYPES) { throw InvalidFileTypeException() }
        check(storage != null)

        val item = storeItemRepository.findByStoreIdAndId(storeId, itemId) ?: throw StoreItemNotFoundException()

        val thumbnailOutputStream = ByteArrayOutputStream()
        Thumbnails.of(file.inputStream)
            .width(RESIZE_WIDTH)
            .keepAspectRatio(true)
            .toOutputStream(thumbnailOutputStream)
        val thumbnailInputStream = ByteArrayInputStream(thumbnailOutputStream.toByteArray())

        val sourceBlob = createBlobFrom(file.inputStream, randomNameFor(file))
        val thumbnailBlob = createBlobFrom(thumbnailInputStream, randomNameFor(file))

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
        const val RESIZE_WIDTH = 100
    }
}