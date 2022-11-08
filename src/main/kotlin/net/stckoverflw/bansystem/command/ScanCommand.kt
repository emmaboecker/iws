package net.stckoverflw.bansystem.command

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalChannel
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.core.behavior.channel.asChannelOfOrNull
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.edit
import dev.kord.core.entity.Member
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.rest.builder.message.create.embed
import dev.kord.rest.builder.message.modify.embed
import dev.schlaubi.mikbot.plugin.api.settings.guildAdminOnly
import dev.schlaubi.mikbot.plugin.api.util.safeGuild
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.stckoverflw.bansystem.BansystemCommandModule
import net.stckoverflw.bansystem.coroutineScope
import net.stckoverflw.bansystem.database.Database
import net.stckoverflw.bansystem.database.model.ScanCooldown
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

suspend fun BansystemCommandModule.scanCommand() = ephemeralSlashCommand(::ScanArguments) {
    name = "scan"
    description = "Scan the Server for reported Users"

    guildAdminOnly()

    action {
        val channelArg = arguments.channel ?: channel

        val channel = channelArg.asChannelOfOrNull<MessageChannel>()

        if (channel == null) {
            respond {
                content = "The channel you selected is not a text channel"
            }
            return@action
        }

        val scan = Database.scansCollection.findOneById(safeGuild.id)
        if (scan != null && scan.lastScan >= System.currentTimeMillis() - 12.hours.inWholeMilliseconds) {
            respond {
                content = "You have to wait for 12 hours between every scan"
            }
            return@action
        }

        Database.scansCollection.save(ScanCooldown(safeGuild.id, System.currentTimeMillis()))

        val message = channel.createMessage {
            embed {
                title = "Scan report"
                description = "<a:loading:972893675145265262> Scanning for Reported Members..."
            }
        }

        respond {
            content =
                "This can take up to 10-30 minutes. Please be patient, a scan report will be created in ${channelArg.mention}"
        }

        coroutineScope.launch {
            val foundMembers = arrayListOf<Member>()

            Database.bannedUserCollection.find().consumeEach {
                val member = safeGuild.withStrategy(EntitySupplyStrategy.rest).getMemberOrNull(it.discordId)

                if (member != null) {
                    foundMembers += member
                }

                // So we don't hit rate limit
                delay(1.seconds)
            }

            if (foundMembers.isEmpty()) {
                message.edit {
                    embed {
                        title = "Scan report"
                        description = "There were no reported Members found"
                    }
                }
                return@launch
            }

            message.edit {
                embed {
                    title = "Scan report: Found Users"
                    description = foundMembers.joinToString {
                        "${it.mention} (${it.tag} / ${it.id.value}) \n"
                    }
                }
            }
        }
    }
}

class ScanArguments : Arguments() {
    val channel by optionalChannel {
        name = "channel"
        description = "channel to send the scan report into"
    }
}