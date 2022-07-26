package net.stckoverflw.bansystem.database.model

import dev.kord.common.entity.Snowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BannedUser(
    @SerialName("_id") val discordId: Snowflake,
    val reportedOnServer: Snowflake,
    val reasons: List<String> = arrayListOf()
)
