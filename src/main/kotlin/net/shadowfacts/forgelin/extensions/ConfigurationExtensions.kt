package net.shadowfacts.forgelin.extensions

import net.minecraftforge.common.config.Configuration

/**
 * @author shadowfacts
 */
fun Configuration.getLong(name: String, category: String, defaultInt: Int, defaultLong: Long, minValue: Int, maxValue: Int, comment: String): Long {
	return get(category, name, defaultInt, comment, minValue, maxValue).getLong(defaultLong)
}