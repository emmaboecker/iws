package net.stckoverflw.bansystem.database.model

import dev.kord.common.entity.Snowflake
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BannedUser(
    @SerialName("_id") val discordId: Snowflake,
    val reportedOnServer: Snowflake,
    val reportedBy: Snowflake? = null,
    val reportedAt: Instant? = null,
    val reasons: List<String> = arrayListOf()
)
