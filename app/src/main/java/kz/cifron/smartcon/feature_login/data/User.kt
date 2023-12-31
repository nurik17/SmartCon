package kz.cifron.smartcon.feature_login.data

data class User(
    val id: Int,
    val controler_id: Long,
    val ID_RAYON: Int,
    val TIP: String?,
    val TAB_NUM: String?,
    val RAYON_NAME: String,
    val name: String,
    val fizYur: Int
)

data class LoginResponse(
    val user: User,
    val token: String
)

data class LoginRequestBody(
    val email: String,
    val password: String
)