package kr.bistroad.storeservice.storeitem.infrastructure

import kr.bistroad.storeservice.store.domain.Store
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository("StoreItem_StoreRepository")
interface StoreRepository : MongoRepository<Store, UUID>