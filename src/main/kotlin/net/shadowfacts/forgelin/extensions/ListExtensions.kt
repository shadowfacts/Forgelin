package net.shadowfacts.forgelin.extensions

import net.minecraft.item.ItemStack

/**
 * @author shadowfacts
 */
fun List<ItemStack>.containsStack(stack: ItemStack): Boolean {
	forEach {
		if (it.item == stack.item && it.itemDamage == stack.itemDamage) {
			return true
		}
	}
	return false
}