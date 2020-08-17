package kr.bistroad.storeservice.store

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import java.util.*

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class StoreIntegrationTests(
    private val ctx: WebApplicationContext
) {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var storeRepository: StoreRepository

    @BeforeEach
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build()
    }

    @Test
    fun `Show a list of stores`() {
        val storeA = Store(
            id = UUID.randomUUID(),
            ownerId = UUID.randomUUID(),
            name = "A store",
            phone = "01-234-567",
            description = "The best store ever",
            locationLat = 0.1,
            locationLng = 0.1
        )
        val storeB = Store(
            id = UUID.randomUUID(),
            ownerId = UUID.randomUUID(),
            name = "B store",
            phone = "12-3456-7890",
            description = "The worst store ever",
            locationLat = 0.15,
            locationLng = -0.15
        )

        given(storeRepository.findAll())
            .willReturn(listOf(storeA, storeB))

        mockMvc.perform(get("/stores").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(storeA.id!!.toString()))
            .andExpect(jsonPath("\$.[0].ownerId").value(storeA.ownerId.toString()))
            .andExpect(jsonPath("\$.[0].name").value(storeA.name))
            .andExpect(jsonPath("\$.[1].id").value(storeB.id!!.toString()))
            .andExpect(jsonPath("\$.[1].location.lat").value(storeB.locationLat))
            .andExpect(jsonPath("\$.[1].location.lng").value(storeB.locationLng))
    }
}