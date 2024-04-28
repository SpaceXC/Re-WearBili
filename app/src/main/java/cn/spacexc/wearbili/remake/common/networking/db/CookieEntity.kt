package cn.spacexc.wearbili.remake.common.networking.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.CryptoManager
import cn.spacexc.wearbili.remake.app.cache.domain.database.MapNullableTypeConverter
import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import io.ktor.util.date.GMTDate

@Entity
@TypeConverters(MapNullableTypeConverter::class)
data class CookieEntity(
    val name: String,
    val value: String,
    val encoding: CookieEncoding,
    val maxAge: Int = 0,
    val expires: Long? = null,
    val domain: String? = null,
    val path: String? = null,
    val secure: Boolean = false,
    val httpOnly: Boolean = false,
    val extensions: Map<String, String?> = emptyMap(),
    val uid: Long?,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

suspend fun Cookie.toCookieEntity(cryptoManager: CryptoManager) = CookieEntity(
    name,
    //if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) value else cryptoManager.encrypt(value),
    value,
    encoding,
    maxAge,
    expires?.timestamp,
    domain,
    path,
    secure,
    httpOnly,
    extensions,
    UserUtils.mid()
)

fun CookieEntity.toKtorCookie(cryptoManager: CryptoManager) = Cookie(
    name,
    //if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) value else String(cryptoManager.decrypt(value.byteInputStream())),
    value,
    encoding,
    maxAge,
    GMTDate(expires),
    domain,
    path,
    secure,
    httpOnly,
    extensions
)