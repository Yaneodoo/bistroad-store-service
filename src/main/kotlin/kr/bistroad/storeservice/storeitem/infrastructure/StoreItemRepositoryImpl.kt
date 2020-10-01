package kr.bistroad.storeservice.storeitem.infrastructure

import kr.bistroad.storeservice.global.util.toPage
import kr.bistroad.storeservice.storeitem.domain.StoreItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import java.util.*

@Component
class StoreItemRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : StoreItemRepositoryCustom {
    override fun search(storeId: UUID, pageable: Pageable): Page<StoreItem> {
        val query = Query()
            .addCriteria(Criteria.where("store.id").`is`(storeId))
            .with(pageable)
        return mongoTemplate.find<StoreItem>(query).toPage(pageable)
    }
}