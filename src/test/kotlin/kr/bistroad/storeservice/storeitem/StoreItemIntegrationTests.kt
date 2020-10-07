package kr.bistroad.storeservice.storeitem

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import kr.bistroad.storeservice.global.domain.Coordinate
import kr.bistroad.storeservice.store.domain.Owner
import kr.bistroad.storeservice.store.domain.Store
import kr.bistroad.storeservice.store.infrastructure.StoreRepository
import kr.bistroad.storeservice.storeitem.domain.StoreItem
import kr.bistroad.storeservice.storeitem.infrastructure.StoreItemRepository
import kr.bistroad.storeservice.storeitem.presentation.StoreItemRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*
import kr.bistroad.storeservice.storeitem.domain.Store as StoreItemStore

@SpringBootTest
@AutoConfigureMockMvc
class StoreItemIntegrationTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var storeRepository: StoreRepository

    @Autowired
    private lateinit var storeItemRepository: StoreItemRepository

    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    @AfterEach
    fun clear() {
        storeRepository.deleteAll()
        storeItemRepository.deleteAll()
    }

    @Test
    fun `Gets an item`() {
        val store = Store(
            owner = Owner(UUID.randomUUID()),
            name = "A store",
            phone = "02-123-4567",
            description = "The best store ever",
            address = "Seoul",
            location = Coordinate(0.1, 0.1)
        )
        val item = StoreItem(
            store = StoreItemStore(store),
            name = "Example",
            description = "example description",
            price = 1000.0,
            stars = 4.5
        )

        storeRepository.save(store)
        storeItemRepository.save(item)

        mockMvc.perform(
            get("/stores/${store.id}/items/${item.id}")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(item.id.toString()))
            .andExpect(jsonPath("\$.storeId").value(store.id.toString()))
            .andExpect(jsonPath("\$.description").value(item.description))
            .andExpect(jsonPath("\$.stars").value(item.stars))
    }

    @Test
    fun `Searches items`() {
        val storeA = Store(
            owner = Owner(UUID.randomUUID()),
            name = "A store",
            phone = "02-123-4567",
            description = "The best store ever",
            address = "Seoul",
            location = Coordinate(0.1, 0.1)
        )
        val storeB = Store(
            owner = Owner(UUID.randomUUID()),
            name = "B store",
            phone = "02-987-6543",
            description = "The worst store ever",
            address = "Seoul",
            location = Coordinate(0.15, -0.15)
        )

        val itemA1 = StoreItem(
            store = StoreItemStore(storeA),
            name = "Apple",
            description = "example description",
            price = 2000.0,
            stars = 4.5
        )
        val itemA2 = StoreItem(
            store = StoreItemStore(storeA),
            name = "Banana",
            description = "example description",
            price = 500.0,
            stars = 4.5
        )
        val itemA3 = StoreItem(
            store = StoreItemStore(storeA),
            name = "Peach",
            description = "example description",
            price = 3000.0,
            stars = 4.5
        )
        val itemB1 = StoreItem(
            store = StoreItemStore(storeB),
            name = "Steak",
            description = "example description",
            price = 10000.0,
            stars = 4.5
        )

        storeRepository.save(storeA)
        storeRepository.save(storeB)
        storeItemRepository.save(itemA1)
        storeItemRepository.save(itemA2)
        storeItemRepository.save(itemA3)
        storeItemRepository.save(itemB1)

        mockMvc.perform(
            get("/stores/${storeA.id}/items?sort=price,desc")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].name").value("Peach"))
            .andExpect(jsonPath("\$.[1].name").value("Apple"))
            .andExpect(jsonPath("\$.[2].name").value("Banana"))
    }

    @Test
    fun `Posts an item`() {
        val store = Store(
            owner = Owner(UUID.randomUUID()),
            name = "Fruit store",
            phone = "02-123-4567",
            description = "The best store ever",
            address = "Seoul",
            location = Coordinate(0.1, 0.1)
        )
        val body = StoreItemRequest.PostBody(
            name = "Apple",
            description = "Delicious apple",
            price = 1000.0
        )

        storeRepository.save(store)

        mockMvc.perform(
            post("/stores/${store.id}/items")
                .header("Authorization-Role", "ROLE_ADMIN")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").exists())
            .andExpect(jsonPath("\$.name").value(body.name))
            .andExpect(jsonPath("\$.description").value(body.description))
            .andExpect(jsonPath("\$.price").value(body.price))
            .andExpect(jsonPath("\$.orderCount").value(0))
    }

    @Test
    fun `Patches an item`() {
        val store = Store(
            owner = Owner(UUID.randomUUID()),
            name = "Fruit store",
            phone = "02-123-4567",
            description = "The best store ever",
            address = "Seoul",
            location = Coordinate(0.1, 0.1)
        )
        val item = StoreItem(
            store = StoreItemStore(store),
            name = "Apple",
            description = "example description",
            price = 2000.0,
            stars = 4.5
        )
        val body = StoreItemRequest.PatchBody(
            price = 1500.0
        )

        storeRepository.save(store)
        storeItemRepository.save(item)

        mockMvc.perform(
            patch("/stores/${store.id}/items/${item.id}")
                .header("Authorization-Role", "ROLE_ADMIN")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(item.id.toString()))
            .andExpect(jsonPath("\$.name").value("Apple"))
            .andExpect(jsonPath("\$.price").value(1500.0))
    }

    @Test
    fun `Deletes an item`() {
        val store = Store(
            owner = Owner(UUID.randomUUID()),
            name = "Fruit store",
            phone = "02-123-4567",
            description = "The best store ever",
            address = "Seoul",
            location = Coordinate(0.1, 0.1)
        )
        val itemA = StoreItem(
            store = StoreItemStore(store),
            name = "Apple",
            description = "example description",
            price = 2000.0,
            stars = 4.5
        )
        val itemB = StoreItem(
            store = StoreItemStore(store),
            name = "Banana",
            description = "example description",
            price = 500.0,
            stars = 4.5
        )

        storeRepository.save(store)
        storeItemRepository.save(itemA)
        storeItemRepository.save(itemB)

        mockMvc.perform(
            delete("/stores/${store.id}/items/${itemA.id}")
                .header("Authorization-Role", "ROLE_ADMIN")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)
            .andExpect(content().string(""))

        val items = storeItemRepository.findAll()
        items.shouldBeSingleton()
        items.first().shouldBe(itemB)
    }
}