package net.stckoverflw.bansystem.listener

import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.core.event.guild.BanAddEvent
import kotlinx.datetime.Clock
import net.stckoverflw.bansystem.BansystemListenerModule
import net.stckoverflw.bansystem.database.Database
import net.stckoverflw.bansystem.database.model.BanSystemSettings
import net.stckoverflw.bansystem.database.model.BannedUser
import net.stckoverflw.bansystem.util.scanAllGuilds

suspend fun BansystemListenerModule.banListener() = event<BanAddEvent> {
    action {
        val settings = Database.botSettingsCollection.findOneById(event.guildId)
            ?: BanSystemSettings(event.guildId, autoReport = true)

        if (!settings.autoReport) return@action

        val existingBan = Database.bannedUserCollection.findOneById(event.user.id)
        val reason = event.getBan().reason

        if (existingBan != null) {
            if (!reason.isNullOrBlank()) {
                if (!reason.startsWith("Banned by", true) && !reason.endsWith("using Button", true)) {
                    Database.bannedUserCollection.save(
                        existingBan.copy(
                            reasons = existingBan.reasons + reason
                        )
                    )
                }
            }
            return@action
        }

        Database.bannedUserCollection.save(
            BannedUser(
                discordId = event.getBan().userId,
                reportedOnServer = event.guildId,
                reportedBy = null, // I sadly don't get this info here
                reportedAt = Clock.System.now(),
                reasons = if (reason != null) arrayListOf(reason) else emptyList()
            )
        )

        scanAllGuilds(kord, event.getBan().getUser())
    }
}