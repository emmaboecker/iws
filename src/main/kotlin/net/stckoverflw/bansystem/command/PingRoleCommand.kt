package net.stckoverflw.bansystem.command

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.role
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.schlaubi.mikbot.plugin.api.settings.guildAdminOnly
import dev.schlaubi.mikbot.plugin.api.util.safeGuild
import net.stckoverflw.bansystem.BansystemCommandModule
import net.stckoverflw.bansystem.database.Database
import net.stckoverflw.bansystem.database.model.BanSystemSettings

suspend fun BansystemCommandModule.pingRoleCommand() = ephemeralSlashCommand(::PingRoleCommandArguments) {
    name = "ping-role"
    description = "Add/Remove a Role to Ping on a new report"

    guildAdminOnly()

    action {
        val doc = Database.botSettingsCollection.findOneById(safeGuild.id)

        if (doc != null) {
            if (doc.pingRoles.contains(arguments.role.id)) {
                Database.botSettingsCollection.save(
                    doc.copy(
                        pingRoles = doc.pingRoles - arguments.role.id
                    )
                )
                respond {
                    content = "The Role ${arguments.role.mention} was successfully removed from the roles to ping"
                }
            } else {
                Database.botSettingsCollection.save(
                    doc.copy(
                        pingRoles = doc.pingRoles + arguments.role.id
                    )
                )
                respond {
                    content = "The Role ${arguments.role.mention} was successfully added to the roles to ping"
                }
            }
            return@action
        }

        Database.botSettingsCollection.save(
            BanSystemSettings(
                safeGuild.id,
                pingRoles = arrayListOf(arguments.role.id)
            )
        )
        respond {
            content =
                "The Role ${arguments.role.mention} was successfully added to the roles to ping.\n" +
                        "**Btw, you haven't set a warning channel yet, please set one using `/warning-channel`**"
        }
    }
}

class PingRoleCommandArguments : Arguments() {
    val role by role {
        name = "role"
        description = "The Role to add/remove to the pinging roles"
    }
}
