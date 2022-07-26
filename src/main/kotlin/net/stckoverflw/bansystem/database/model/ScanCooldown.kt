package net.stckoverflw.bansystem.database.model

import dev.kord.common.entity.Snowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScanCooldown(
    @SerialName("_id") val guildId: Snowflake,
    val lastScan: Long
)
