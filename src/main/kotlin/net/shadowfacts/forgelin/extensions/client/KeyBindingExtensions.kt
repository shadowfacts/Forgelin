package net.shadowfacts.forgelin.extensions.client

import net.minecraft.client.settings.GameSettings
import net.minecraft.client.settings.KeyBinding

/**
 * @author shadowfacts
 */
fun KeyBinding.getDisplayString(): String {
	return GameSettings.getKeyDisplayString(keyCode)
}