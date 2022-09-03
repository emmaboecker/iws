package net.stckoverflw.bansystem.command

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.user
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.rest.builder.message.create.embed
import dev.schlaubi.mikbot.plugin.api.settings.guildAdminOnly
import net.stckoverflw.bansystem.BansystemCommandModule
import net.stckoverflw.bansystem.database.Database

suspend fun BansystemCommandModule.infoCommand() = ephemeralSlashCommand(::InfoCommandArguments) {
    name = "report-info"
    description = "Get information about a reported user"
    guildAdminOnly()

    action {
        val report = Database.bannedUserCollection.findOneById(arguments.user.id)

        if (report == null) {
            respond {
                content = "This user is not reported"
            }
            return@action
        }
        respond {
            embed {
                title = arguments.user.tag
                description = "Information about ${arguments.user.mention}"


                field {
                    name = "Reasons"
                    value =
                        if (report.reasons.isNotEmpty()) {
                            buildString {
                                report.reasons.forEachIndexed { index, reason ->
                                    append("${index + 1}. $reason\n")
                                }
                            }
                        } else {
                            "No reasons provided"
                        }
                }

                field {
                    name = "Reported on Server"
                    val guild = this@infoCommand.kord.getGuild(report.reportedOnServer)
                    value = if (guild == null) {
                        "Guild not found"
                    } else {
                        "${guild.name} (${guild.id})"
                    }
                }
            }
        }
    }
}

class InfoCommandArguments : Arguments() {
    val user by user {
        name = "user"
        description = "The user you want to get information about"
    }
}
