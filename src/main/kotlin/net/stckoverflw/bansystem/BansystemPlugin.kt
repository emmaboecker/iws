package net.stckoverflw.bansystem

import com.kotlindiscord.kord.extensions.builders.ExtensibleBotBuilder
import dev.schlaubi.mikbot.plugin.api.Plugin
import dev.schlaubi.mikbot.plugin.api.PluginContext
import dev.schlaubi.mikbot.plugin.api.PluginMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import net.stckoverflw.bansystem.command.*
import net.stckoverflw.bansystem.listener.banListener
import net.stckoverflw.bansystem.listener.interactionCreateListener
import net.stckoverflw.bansystem.listener.memberJoinListener
import com.kotlindiscord.kord.extensions.extensions.Extension as KordExtension

val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

@PluginMain
class BansystemPlugin(context: PluginContext) : Plugin(context) {

    override suspend fun ExtensibleBotBuilder.apply() {
        intents(true, addExtensionIntents = true) {}
    }

    override fun ExtensibleBotBuilder.ExtensionsBuilder.addExtensions() {
        add(::BansystemCommandModule)
        add(::BansystemListenerModule)
    }

}

class BansystemCommandModule : KordExtension() {
    override val name: String = "bansystem command module"

    override suspend fun setup() {
        reportCommand()
        scanCommand()
        warningChannelCommand()
        pingRoleCommand()
        autoReportCommand()
        infoCommand()
        announceCommand()
    }
}

class BansystemListenerModule : KordExtension() {
    override val name: String = "bansystem listener module"

    override suspend fun setup() {
        interactionCreateListener()
        memberJoinListener()
        banListener()
    }
}

