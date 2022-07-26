package net.stckoverflw.bansystem.listener

import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.getChannelOf
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.rest.builder.message.create.embed
import net.stckoverflw.bansystem.BansystemListenerModule
import net.stckoverflw.bansystem.database.Database
import net.stckoverflw.bansystem.util.banButton

suspend fun BansystemListenerModule.memberJoinListener() = event<MemberJoinEvent> {
    action {
        val settings = Database.botSettingsCollection.findOneById(event.guildId) ?: return@action

        if (settings.logChannel == null) return@action

        val bannedUser = Database.bannedUserCollection.findOneById(event.member.id) ?: return@action

        event.guild.getChannelOf<TextChannel>(settings.logChannel).createMessage {
            content = settings.pingRoles.joinToString(", ") {id ->
                "<@&${id}>"
            }
            embed {
                title = "Reported User Joined the Server"
                description = "A User joined the Server that was reported on another Server"
                field {
                    name = "User"
                    value = "${event.member.mention} (${event.member.tag} / ${event.member.id})"
                }
                if (bannedUser.reasons.isNotEmpty()) {
                    field {
                        name = "Reason(s)"
                        value = bannedUser.reasons.joinToString(", \n", "`", "`")
                    }
                }
            }
            banButton(event.member)
        }
    }
}