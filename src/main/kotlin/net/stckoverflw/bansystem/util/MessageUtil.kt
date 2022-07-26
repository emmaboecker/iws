package net.stckoverflw.bansystem.util

import dev.kord.common.entity.ButtonStyle
import dev.kord.core.entity.User
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import dev.kord.rest.builder.message.create.actionRow

fun MessageCreateBuilder.banButton(user: User) {
    actionRow {
        interactionButton(ButtonStyle.Danger, "ban:${user.id.value}") {
            label = "Ban User"
        }
    }
}