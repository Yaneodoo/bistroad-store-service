package kr.bistroad.storeservice.store

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.bistroad.storeservice.store.domain.Store
import kr.bistroad.storeservice.store.infrastructure.StoreRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class StoreIntegrationTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var storeRepository: StoreRepository

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

        every { storeRepository.search(any(), any()) } returns PageImpl(listOf(storeA, storeB))

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