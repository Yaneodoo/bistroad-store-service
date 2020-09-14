package kr.bistroad.storeservice.global.config.security

import kr.bistroad.storeservice.store.application.StoreService
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*

@Component
class StorePermissionEvaluator(
    private val storeService: StoreService
) : PermissionEvaluator {
    override fun hasPermission(authentication: Authentication?, targetDomainObject: Any?, permission: Any?) =
        throw UnsupportedOperationException()

    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        if (authentication != null && targetType == "Store" && permission is String) {
            val userId = (authentication.principal as UserPrincipal).userId
            val store = storeService.readStore(targetId as UUID)

            when (permission) {
                "read" -> return true
                "write" -> return (store != null && store.ownerId == userId)
            }
        }
        return false
    }
}