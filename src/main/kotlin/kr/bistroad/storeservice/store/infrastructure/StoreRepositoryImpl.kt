package kr.bistroad.storeservice.store.infrastructure

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.NumberExpression
import kr.bistroad.storeservice.store.domain.QStore.store
import kr.bistroad.storeservice.store.application.StoreDto
import kr.bistroad.storeservice.store.domain.Store
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

    override fun searchNearby(dto: StoreDto.SearchNearbyReq, pageable: Pageable): Page<Store> {
        val query = from(store)
            .where(
                dist2(dto).lt(dto.radius * dto.radius)
            )

        val list = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(list, pageable, query.fetchCount())
    }

    private fun dist2(dto: StoreDto.SearchNearbyReq): NumberExpression<Double> {
        val latDiff = store.locationLat.subtract(dto.originLat)
        val lngDiff = store.locationLng.subtract(dto.originLng)

        return latDiff.multiply(latDiff)
            .add(lngDiff.multiply(lngDiff))
    }
}