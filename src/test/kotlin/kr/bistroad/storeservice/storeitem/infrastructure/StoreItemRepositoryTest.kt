package kr.bistroad.storeservice.storeitem.infrastructure

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kr.bistroad.storeservice.storeitem.domain.StoreOfItem
import kr.bistroad.storeservice.storeitem.domain.StoreItem
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import java.util.*

@DataMongoTest
internal class StoreItemRepositoryTest {
    @Autowired
    private lateinit var storeItemRepository: StoreItemRepository

    @AfterEach
    fun clear() = storeItemRepository.deleteAll()

    @Test
    fun `Saves an item`() {
        val item = StoreItem(
            store = StoreOfItem(
                id = UUID.randomUUID(),
                ownerId = UUID.randomUUID()
            ),
            name = "Apple",
            description = "example description",
            photoUri = null,
            price = 2000.0,
            stars = 4.5
        )
        storeItemRepository.save(item)

        val foundItem = storeItemRepository.findByStoreIdAndId(item.store.id, item.id)

        foundItem.shouldNotBeNull()
        foundItem.shouldBe(item)
    }

    @Test
    fun `Deletes an item`() {
        val item = StoreItem(
            store = StoreOfItem(
                id = UUID.randomUUID(),
                ownerId = UUID.randomUUID()
            ),
            name = "Apple",
            description = "example description",
            photoUri = null,
            price = 2000.0,
            stars = 4.5
        )
        storeItemRepository.save(item)

        val storeId = item.store.id
        val itemId = item.id
        storeItemRepository.removeByStoreIdAndId(storeId, itemId)

        storeItemRepository.findByStoreIdAndId(storeId, itemId).shouldBeNull()
        storeItemRepository.findAll().shouldBeEmpty()
    }
}