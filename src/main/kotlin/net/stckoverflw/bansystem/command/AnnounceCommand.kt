package net.stckoverflw.bansystem.command

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.getChannelOf
import dev.kord.core.entity.channel.TextChannel
import dev.schlaubi.mikbot.plugin.api.owner.ownerOnly
import net.stckoverflw.bansystem.BansystemCommandModule
import net.stckoverflw.bansystem.database.Database

suspend fun BansystemCommandModule.announceCommand() = ephemeralSlashCommand(::AnnounceCommandArguments) {
    name = "announce"
    description = "Send a Announcement to all Servers"
    ownerOnly()

    action {
        val guildSettings = Database.botSettingsCollection.find().toList()

        guildSettings.forEach { setting ->
            setting.logChannel?.let { channel ->
                val guild = this@announceCommand.kord.getGuild(setting.guildId)
                guild?.getChannelOf<TextChannel>(channel)?.createMessage {
                    content = setting.pingRoles.joinToString { "<@&${it.value}>" } + "\n" + arguments.message
                }
            }
        }

        respond {
            content = "Announcement sent to all Servers that have a log channel set"
        }
    }
}

class AnnounceCommandArguments : Arguments() {
    val message by string {
        name = "message"
        description = "The message you want to announce"
    }
}
