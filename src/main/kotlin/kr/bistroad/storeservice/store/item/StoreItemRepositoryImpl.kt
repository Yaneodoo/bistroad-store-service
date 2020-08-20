package kr.bistroad.storeservice.store.item

import kr.bistroad.storeservice.store.item.QStoreItem.storeItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Component
import java.util.*

@Component
class StoreItemRepositoryImpl : QuerydslRepositorySupport(StoreItem::class.java), StoreItemRepositoryCustom {
    override fun search(storeId: UUID, pageable: Pageable): Page<StoreItem> {
        val query = from(storeItem)
            .where(storeItem.id.eq(storeId))

        val list = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(list, pageable, query.fetchCount())
    }
}