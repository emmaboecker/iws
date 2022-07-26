package net.stckoverflw.bansystem.database.model

import dev.kord.common.entity.Snowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BanSystemSettings(
    @SerialName("_id") val guildId: Snowflake,
    val logChannel: Snowflake? = null,
    val pingRoles: List<Snowflake> = arrayListOf(),
    val autoReport: Boolean = true
)
