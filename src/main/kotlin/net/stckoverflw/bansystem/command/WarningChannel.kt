package net.stckoverflw.bansystem.command

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.channel
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.entity.ChannelType
import dev.kord.core.entity.channel.TextChannel
import dev.schlaubi.mikbot.plugin.api.settings.guildAdminOnly
import dev.schlaubi.mikbot.plugin.api.util.safeGuild
import net.stckoverflw.bansystem.BansystemCommandModule
import net.stckoverflw.bansystem.database.Database
import net.stckoverflw.bansystem.database.model.BanSystemSettings

suspend fun BansystemCommandModule.warningChannelCommand() = ephemeralSlashCommand(::WarningChannelCommandArguments) {
    name = "warning-channel"
    description = "Sets the warning Channel where malicious Users will be posted"

    guildAdminOnly()

    action {
        if (arguments.channel.type == ChannelType.GuildText) {
            Database.botSettingsCollection.save(
                BanSystemSettings(
                    guildId = safeGuild.id,
                    logChannel = arguments.channel.id
                )
            )
            respond {
                content = "You set the warning channel to ${arguments.channel.mention}"
            }
        } else {
            respond {
                content = "That is not a Text-Channel"
            }
        }
    }
}

class WarningChannelCommandArguments : Arguments() {
    val channel by channel {
        name = "channel"
        description = "channel to set the warning channel to. Should be admin/mod-only"
    }

}
