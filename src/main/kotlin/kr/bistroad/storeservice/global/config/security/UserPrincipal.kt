package kr.bistroad.storeservice.global.config.security

import java.util.*

data class UserPrincipal(
    val userId: UUID?,
    val role: String?
)