package kr.bistroad.storeservice.storeitem

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import kr.bistroad.storeservice.store.domain.Store
import kr.bistroad.storeservice.store.infrastructure.StoreRepository
import kr.bistroad.storeservice.storeitem.domain.StoreItem
import kr.bistroad.storeservice.storeitem.infrastructure.StoreItemRepository
import kr.bistroad.storeservice.storeitem.presentation.StoreItemRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StoreItemIntegrationTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var storeRepository: StoreRepository

    @Autowired
    private lateinit var storeItemRepository: StoreItemRepository

    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    @Test
    fun `Gets an item`() {
        val store = Store(
            ownerId = UUID.randomUUID(),
            name = "A store",
            phone = "02-123-4567",
            description = "The best store ever",
            locationLat = 0.1,
            locationLng = 0.1
        )
        val item = StoreItem(
            name = "Example",
            description = "example description",
            photoUri = null,
            price = 1000.0,
            stars = 4.5
        )
        store.addMenuItem(item)

        storeRepository.save(store)

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
            ownerId = UUID.randomUUID(),
            name = "A store",
            phone = "02-123-4567",
            description = "The best store ever",
            locationLat = 0.1,
            locationLng = 0.1
        )
        val storeB = Store(
            ownerId = UUID.randomUUID(),
            name = "B store",
            phone = "02-987-6543",
            description = "The worst store ever",
            locationLat = 0.15,
            locationLng = -0.15
        )

        val itemA1 = StoreItem(
            name = "Apple",
            description = "example description",
            photoUri = null,
            price = 2000.0,
            stars = 4.5
        )
        val itemA2 = StoreItem(
            name = "Banana",
            description = "example description",
            photoUri = null,
            price = 500.0,
            stars = 4.5
        )
        val itemA3 = StoreItem(
            name = "Peach",
            description = "example description",
            photoUri = null,
            price = 3000.0,
            stars = 4.5
        )
        val itemB1 = StoreItem(
            name = "Steak",
            description = "example description",
            photoUri = null,
            price = 10000.0,
            stars = 4.5
        )
        storeA.addMenuItem(itemA1)
        storeA.addMenuItem(itemA2)
        storeA.addMenuItem(itemA3)
        storeB.addMenuItem(itemB1)

        storeRepository.save(storeA)
        storeRepository.save(storeB)

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
            ownerId = UUID.randomUUID(),
            name = "Fruit store",
            phone = "02-123-4567",
            description = "The best store ever",
            locationLat = 0.1,
            locationLng = 0.1
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
    }

    @Test
    fun `Patches an item`() {
        val store = Store(
            ownerId = UUID.randomUUID(),
            name = "Fruit store",
            phone = "02-123-4567",
            description = "The best store ever",
            locationLat = 0.1,
            locationLng = 0.1
        )
        val item = StoreItem(
            name = "Apple",
            description = "example description",
            photoUri = null,
            price = 2000.0,
            stars = 4.5
        )
        store.addMenuItem(item)
        val body = StoreItemRequest.PatchBody(
            price = 1500.0
        )

        storeRepository.save(store)

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
            .andExpect(jsonPath("\$.price").value(item.price))
    }

    @Test
    fun `Deletes an item`() {
        val store = Store(
            ownerId = UUID.randomUUID(),
            name = "Fruit store",
            phone = "02-123-4567",
            description = "The best store ever",
            locationLat = 0.1,
            locationLng = 0.1
        )
        val itemA = StoreItem(
            name = "Apple",
            description = "example description",
            photoUri = null,
            price = 2000.0,
            stars = 4.5
        )
        val itemB = StoreItem(
            name = "Banana",
            description = "example description",
            photoUri = null,
            price = 500.0,
            stars = 4.5
        )

        store.addMenuItem(itemA)
        store.addMenuItem(itemB)
        storeRepository.save(store)

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