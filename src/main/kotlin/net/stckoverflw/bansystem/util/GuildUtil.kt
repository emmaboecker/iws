package net.stckoverflw.bansystem.util

import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.getChannelOf
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.rest.builder.message.create.embed
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mu.KotlinLogging
import net.stckoverflw.bansystem.database.Database

private val LOG = KotlinLogging.logger { }

suspend fun scanAllGuilds(kord: Kord, user: User) {
    coroutineScope {
        kord.with(EntitySupplyStrategy.rest).guilds.onEach {
            try {
                val member = it.getMemberOrNull(user.id) ?: return@onEach

                val settings = Database.botSettingsCollection.findOneById(it.id) ?: return@onEach

                if (settings.logChannel == null) return@onEach

                val bannedUser = Database.bannedUserCollection.findOneById(user.id) ?: return@onEach

                it.getChannelOf<TextChannel>(settings.logChannel).createMessage {
                    content = settings.pingRoles.joinToString(", ") { id ->
                        "<@&${id}>"
                    }
                    embed {
                        title = "New Reported User Found"
                        description = "There was a User found that was reported on another Server"
                        field {
                            name = "User"
                            value = "${member.mention} (${member.tag} / ${member.id})"
                        }
                        field {
                            name = "Reason(s)"
                            value = bannedUser.reasons.joinToString(", \n", "`", "`")
                        }
                    }
                    banButton(member)
                }
            } catch (exception: Exception) {
                LOG.info("Failed to scan Guild ${it.name} (${it.id}): ${exception.message}")
            }
        }.launchIn(this)
    }
}