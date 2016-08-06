package net.shadowfacts.forgelin.extensions

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB

/**
 * @author shadowfacts
 */
fun AxisAlignedBB.rotateFace(side: EnumFacing): AxisAlignedBB {
	when (side) {
		EnumFacing.DOWN -> return this
		EnumFacing.UP -> return AxisAlignedBB(minX, 1 - maxY, minZ, maxX, 1 - minY, maxZ)
		EnumFacing.NORTH -> return AxisAlignedBB(minX, minZ, minY, maxX, maxZ, maxY)
		EnumFacing.SOUTH -> return AxisAlignedBB(minX, minZ, 1 - maxY, maxX, maxZ, 1 - minY)
		EnumFacing.WEST -> return AxisAlignedBB(minY, minZ, minX, maxY, maxZ, maxX)
		EnumFacing.EAST -> return AxisAlignedBB(1 - maxY, minZ, minX, 1 - minY, maxZ, maxX)
		else -> return this
	}
}