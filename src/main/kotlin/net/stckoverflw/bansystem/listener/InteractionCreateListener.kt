package net.stckoverflw.bansystem.listener

import com.kotlindiscord.kord.extensions.extensions.event
import com.kotlindiscord.kord.extensions.utils.hasPermission
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.ban
import dev.kord.core.event.interaction.ComponentInteractionCreateEvent
import dev.schlaubi.mikbot.plugin.api.util.respondEphemeral
import net.stckoverflw.bansystem.BansystemListenerModule

suspend fun BansystemListenerModule.interactionCreateListener() = event<ComponentInteractionCreateEvent> {
    action {
        val interaction = event.interaction

        val userId = interaction.user.id
        val guild = interaction.message.getGuild()
        val member = guild.getMember(userId)

        if (!member.hasPermission(Permission.BanMembers)) {
            interaction.respondEphemeral {
                content = "You don't have Permission to ban members"
            }
            return@action
        }

        val customId = interaction.component.data.customId.value ?: return@action

        guild.ban(Snowflake(customId.split(":")[1])) {
            reason = "Banned by ${member.username} using Button"
        }

        interaction.respondEphemeral {
            content = "User was banned"
        }
    }
}