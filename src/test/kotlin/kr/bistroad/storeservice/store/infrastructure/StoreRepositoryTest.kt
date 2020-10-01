package kr.bistroad.storeservice.store.infrastructure

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kr.bistroad.storeservice.global.domain.Coordinate
import kr.bistroad.storeservice.store.domain.Store
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.data.geo.Metrics
import org.springframework.data.repository.findByIdOrNull
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
internal class StoreRepositoryTest {
    @Autowired
    private lateinit var storeRepository: StoreRepository

    @AfterEach
    fun clear() = storeRepository.deleteAll()

    @Test
    fun `Searches stores nearby`() {
        val store = Store(
            ownerId = UUID.randomUUID(),
            name = "A store",
            phone = "02-123-4567",
            description = "The best store ever",
            location = Coordinate(0.1, 0.1)
        )
        storeRepository.save(store)

        val foundStore = storeRepository.findByIdOrNull(store.id)

        foundStore.shouldNotBeNull()
        foundStore.shouldBe(store)
    }

    @Test
    fun `Finds stores`() {
        val storeA = Store(
            ownerId = UUID.randomUUID(),
            name = "", phone = "", description = "",
            location = Coordinate(37.61976485, 127.05975656)
        )
        val storeB = Store(
            ownerId = UUID.randomUUID(),
            name = "", phone = "", description = "",
            location = Coordinate(37.61978087, 127.0608598)
        )
        val storeC = Store(
            ownerId = UUID.randomUUID(),
            name = "", phone = "", description = "",
            location = Coordinate(37.61987435, 127.05740511)
        )
        storeRepository.save(storeA)
        storeRepository.save(storeB)
        storeRepository.save(storeC)

        val foundStores = storeRepository.searchNearby(
            origin = Coordinate(37.61960241, 127.05948651),
            distance = 100.0,
            pageable = Pageable.unpaged()
        )

        foundStores.shouldBeSingleton()
        with(foundStores.first()) {
            this.content.shouldBe(storeA)
            this.distance.`in`(Metrics.KILOMETERS).value.times(1000).shouldBeLessThan(100.0)
        }
    }

    @Test
    fun `Deletes a store`() {
        val store = Store(
            ownerId = UUID.randomUUID(),
            name = "A store",
            phone = "02-123-4567",
            description = "The best store ever",
            location = Coordinate(0.1, 0.1)
        )
        storeRepository.save(store)

        val storeId = store.id
        storeRepository.deleteById(storeId)

        storeRepository.findByIdOrNull(storeId).shouldBeNull()
        storeRepository.findAll().shouldBeEmpty()
    }
}