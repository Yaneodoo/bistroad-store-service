package kr.bistroad.storeservice.store

import com.querydsl.core.BooleanBuilder
import kr.bistroad.storeservice.store.QStore.store
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Component

@Component
class StoreRepositoryImpl : QuerydslRepositorySupport(Store::class.java), StoreRepositoryCustom {
    override fun search(dto: StoreDto.SearchReq, pageable: Pageable): Page<Store> {
        val booleanBuilder = BooleanBuilder()
        if (dto.ownerId != null) booleanBuilder.and(store.ownerId.eq(dto.ownerId))

        val query = from(store)
            .where(booleanBuilder)

        val list = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(list, pageable, query.fetchCount())
    }
}