package net.shadowfacts.forgelin.extensions

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.text.TextComponentString

/**
 * @author shadowfacts
 */
fun EntityPlayer.addChatMsg(msg: String) {
	addChatComponentMessage(TextComponentString(msg))
}

fun EntityPlayer.addChatMsg(msg: String, vararg params: Any) {
	addChatMsg(String.format(msg, params))
}