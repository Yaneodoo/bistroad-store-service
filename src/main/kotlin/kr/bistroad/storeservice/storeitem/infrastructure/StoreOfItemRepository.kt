package kr.bistroad.storeservice.storeitem.infrastructure

import kr.bistroad.storeservice.store.domain.Store
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface StoreOfItemRepository : MongoRepository<Store, UUID>