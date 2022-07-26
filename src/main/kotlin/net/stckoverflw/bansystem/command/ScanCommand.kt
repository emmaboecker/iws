package net.stckoverflw.bansystem.command

import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.core.entity.Member
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.rest.builder.message.create.embed
import dev.schlaubi.mikbot.plugin.api.settings.guildAdminOnly
import dev.schlaubi.mikbot.plugin.api.util.safeGuild
import kotlin.time.Duration.Companion.hours
import net.stckoverflw.bansystem.BansystemCommandModule
import net.stckoverflw.bansystem.database.Database
import net.stckoverflw.bansystem.database.model.ScanCooldown

suspend fun BansystemCommandModule.scanCommand() = ephemeralSlashCommand {
    name = "scan"
    description = "Scan the Server for reported Users"

    guildAdminOnly()

    action {
        val scan = Database.scansCollection.findOneById(safeGuild.id)
        if (scan != null && scan.lastScan >= System.currentTimeMillis() - 2.hours.inWholeMilliseconds) {
            respond {
                content = "You have to wait for 2 hours between every scan"
            }
            return@action
        }

        Database.scansCollection.save(ScanCooldown(safeGuild.id, System.currentTimeMillis()))

        val foundMembers = arrayListOf<Member>()

        Database.bannedUserCollection.find().consumeEach {
            val member = safeGuild.withStrategy(EntitySupplyStrategy.rest).getMemberOrNull(it.discordId)

            if (member != null) {
                foundMembers += member
            }
        }

        if (foundMembers.isEmpty()) {
            respond {
                content = "There were no reported Members found"
            }
            return@action
        }

        respond {
            embed {
                title = "Found Users"
                description = foundMembers.joinToString {
                    "${it.mention} (${it.tag} / ${it.id.value}) \n"
                }
            }
        }
    }
}