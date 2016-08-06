package net.shadowfacts.forgelin.extensions

import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagList

/**
 * @author shadowfacts
 */
fun NBTTagList.forEach(action: (NBTBase) -> Unit) {
	for (i in 0.until(tagCount())) {
		action(get(i))
	}
}