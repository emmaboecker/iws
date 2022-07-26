package net.stckoverflw.bansystem.database

import com.kotlindiscord.kord.extensions.koin.KordExKoinComponent
import dev.schlaubi.mikbot.plugin.api.io.getCollection
import dev.schlaubi.mikbot.plugin.api.util.database
import net.stckoverflw.bansystem.database.model.BanSystemSettings
import net.stckoverflw.bansystem.database.model.BannedUser
import net.stckoverflw.bansystem.database.model.ScanCooldown

object Database : KordExKoinComponent {

    val bannedUserCollection = database.getCollection<BannedUser>("banned_users")
    val botSettingsCollection = database.getCollection<BanSystemSettings>("ban_settings")
    val scansCollection = database.getCollection<ScanCooldown>("scans")

}