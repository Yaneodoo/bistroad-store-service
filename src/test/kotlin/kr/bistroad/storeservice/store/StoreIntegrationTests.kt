package kr.bistroad.storeservice.store

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import kr.bistroad.storeservice.store.domain.Store
import kr.bistroad.storeservice.store.infrastructure.StoreRepository
import kr.bistroad.storeservice.store.presentation.StoreRequest
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
class StoreIntegrationTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var storeRepository: StoreRepository

    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    @Test
    fun `Gets a store`() {
        val store = Store(
            ownerId = UUID.randomUUID(),
            name = "A store",
            phone = "02-123-4567",
            description = "The best store ever",
            locationLat = 0.1,
            locationLng = 0.1
        )

        storeRepository.save(store)

        mockMvc.perform(
            get("/stores/${store.id}")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(store.id.toString()))
            .andExpect(jsonPath("\$.ownerId").value(store.ownerId.toString()))
            .andExpect(jsonPath("\$.name").value(store.name))
    }

    @Test
    fun `Searches stores`() {
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
        val storeC = Store(
            ownerId = UUID.randomUUID(),
            name = "C store",
            phone = "02-000-0000",
            description = "Not a bad store",
            locationLat = 0.0,
            locationLng = 0.0
        )

        storeRepository.save(storeA)
        storeRepository.save(storeB)
        storeRepository.save(storeC)

        mockMvc.perform(
            get("/stores?sort=phone,desc")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(storeB.id.toString()))
            .andExpect(jsonPath("\$.[0].ownerId").value(storeB.ownerId.toString()))
            .andExpect(jsonPath("\$.[0].name").value(storeB.name))
            .andExpect(jsonPath("\$.[1].id").value(storeA.id.toString()))
            .andExpect(jsonPath("\$.[1].location.lat").value(storeA.locationLat))
            .andExpect(jsonPath("\$.[1].location.lng").value(storeA.locationLng))
    }

    @Test
    fun `Searches nearby stores`() {
        val storeA = Store(
            ownerId = UUID.randomUUID(),
            name = "A store",
            phone = "02-123-4567",
            description = "The best store ever",
            locationLat = 0.99,
            locationLng = 1.01
        )
        val storeB = Store(
            ownerId = UUID.randomUUID(),
            name = "B store",
            phone = "02-987-6543",
            description = "The worst store ever",
            locationLat = 0.15,
            locationLng = -0.15
        )
        val storeC = Store(
            ownerId = UUID.randomUUID(),
            name = "C store",
            phone = "02-000-0000",
            description = "Not a bad store",
            locationLat = 0.0,
            locationLng = 0.0
        )

        storeRepository.save(storeA)
        storeRepository.save(storeB)
        storeRepository.save(storeC)

        mockMvc.perform(
            get("/stores/nearby?originLat=1&originLng=1&radius=0.1")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(storeA.id.toString()))
    }

    @Test
    fun `Posts a store`() {
        val body = StoreRequest.PostBody(
            ownerId = UUID.randomUUID(),
            name = "A store",
            phone = "02-123-4567",
            description = "The best store ever",
            location = StoreRequest.Location(
                lat = 0.1,
                lng = -0.1
            )
        )

        mockMvc.perform(
            post("/stores")
                .header("Authorization-Role", "ROLE_ADMIN")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").exists())
            .andExpect(jsonPath("\$.ownerId").value(body.ownerId.toString()))
            .andExpect(jsonPath("\$.description").value(body.description))
            .andExpect(jsonPath("\$.location.lat").value(body.location.lat))
            .andExpect(jsonPath("\$.location.lng").value(body.location.lng))
    }

    @Test
    fun `Patches a store`() {
        val store = Store(
            ownerId = UUID.randomUUID(),
            name = "A store",
            phone = "02-123-4567",
            description = "The best store ever",
            locationLat = 0.99,
            locationLng = 1.01
        )
        val body = StoreRequest.PatchBody(
            name = "B store"
        )

        storeRepository.save(store)

        mockMvc.perform(
            patch("/stores/${store.id}")
                .header("Authorization-Role", "ROLE_ADMIN")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(store.id.toString()))
            .andExpect(jsonPath("\$.name").value("B store"))
            .andExpect(jsonPath("\$.description").value("The best store ever"))
    }

    @Test
    fun `Deletes a store`() {
        val storeA = Store(
            ownerId = UUID.randomUUID(),
            name = "A store",
            phone = "02-123-4567",
            description = "The best store ever",
            locationLat = 0.99,
            locationLng = 1.01
        )
        val storeB = Store(
            ownerId = UUID.randomUUID(),
            name = "B store",
            phone = "02-987-6543",
            description = "The worst store ever",
            locationLat = 0.15,
            locationLng = -0.15
        )

        storeRepository.save(storeA)
        storeRepository.save(storeB)

        mockMvc.perform(
            delete("/stores/${storeA.id}")
                .header("Authorization-Role", "ROLE_ADMIN")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)
            .andExpect(content().string(""))

        val stores = storeRepository.findAll()
        stores.shouldBeSingleton()
        stores.first().shouldBe(storeB)
    }
}