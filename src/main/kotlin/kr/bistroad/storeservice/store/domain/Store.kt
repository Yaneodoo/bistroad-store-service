package kr.bistroad.storeservice.store.domain

import kr.bistroad.storeservice.storeitem.domain.StoreItem
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "stores")
class Store(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID? = null,

    @OneToMany(mappedBy = "store", cascade = [CascadeType.ALL], orphanRemoval = true)
    @Column(name = "menuItems")
    val _menuItems: MutableList<StoreItem> = mutableListOf(),

    @Column(columnDefinition = "BINARY(16)")
    var ownerId: UUID,

    var name: String,
    var phone: String,
    var description: String,
    var locationLat: Double,
    var locationLng: Double
) {
    val menuItems: Collection<StoreItem>
        get() = _menuItems

    fun addMenuItem(item: StoreItem) {
        item.store = this
        _menuItems += item
    }

    fun removeMenuItem(item: StoreItem) {
        item.store = null
        _menuItems += item
    }
}