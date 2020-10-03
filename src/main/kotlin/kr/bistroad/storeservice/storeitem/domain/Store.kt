package kr.bistroad.storeservice.storeitem.domain

import kr.bistroad.storeservice.store.domain.Store
import java.util.*

data class Store(
    val id: UUID,
    val ownerId: UUID
) {
    constructor(store: Store) : this(store.id, store.owner.id)
}