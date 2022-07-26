package net.stckoverflw.bansystem.command

import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.schlaubi.mikbot.plugin.api.settings.guildAdminOnly
import dev.schlaubi.mikbot.plugin.api.util.safeGuild
import net.stckoverflw.bansystem.BansystemCommandModule
import net.stckoverflw.bansystem.database.Database
import net.stckoverflw.bansystem.database.model.BanSystemSettings

suspend fun BansystemCommandModule.autoReportCommand() = ephemeralSlashCommand {
    name = "auto-report"
    description = "Toggle auto-reporting of users when they're banned"

    guildAdminOnly()

    action {
        val settings = Database.botSettingsCollection.findOneById(safeGuild.id)
            ?: BanSystemSettings(safeGuild.id, autoReport = true)

        val newValue = !settings.autoReport

        Database.botSettingsCollection.save(
            settings.copy(
                autoReport = newValue
            )
        )

        if (newValue) {
            respond {
                content = "Bans will now be automatically reported"
            }
        } else {
            respond {
                content = "Bans will not be automatically reported anymore"
            }
        }
    }
}