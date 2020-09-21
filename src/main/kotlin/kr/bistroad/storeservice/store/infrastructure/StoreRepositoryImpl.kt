package kr.bistroad.storeservice.store.infrastructure

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.NumberExpression
import kr.bistroad.storeservice.store.domain.QStore.store
import kr.bistroad.storeservice.store.domain.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Component
import java.util.*

@Component
class StoreRepositoryImpl : QuerydslRepositorySupport(Store::class.java), StoreRepositoryCustom {
    override fun search(
        ownerId: UUID?,
        pageable: Pageable
    ): Page<Store> {
        val booleanBuilder = BooleanBuilder()
        if (ownerId != null) booleanBuilder.and(store.ownerId.eq(ownerId))

        val query = from(store)
            .where(booleanBuilder)

        val list = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(list, pageable, query.fetchCount())
    }

    override fun searchNearby(
        originLat: Double,
        originLng: Double,
        radius: Double,
        pageable: Pageable
    ): Page<Store> {
        val query = from(store)
            .where(
                dist2(originLat, originLng).lt(radius * radius)
            )

        val list = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(list, pageable, query.fetchCount())
    }

    private fun dist2(originLat: Double, originLng: Double): NumberExpression<Double> {
        val latDiff = store.locationLat.subtract(originLat)
        val lngDiff = store.locationLng.subtract(originLng)

        return latDiff.multiply(latDiff)
            .add(lngDiff.multiply(lngDiff))
    }
}