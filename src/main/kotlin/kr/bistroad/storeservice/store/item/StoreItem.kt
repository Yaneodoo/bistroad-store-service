package kr.bistroad.storeservice.store.item

import kr.bistroad.storeservice.store.Store
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "store_items")
class StoreItem(
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(columnDefinition = "BINARY(16)")
        val id: UUID? = null,

        @ManyToOne
        @JoinColumn(name = "storeId")
        var store: Store? = null,

        var name: String,
        var description: String,
        var photoUri: String?,
        var price: Double,
        var stars: Double
)