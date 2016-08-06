package net.shadowfacts.forgelin.extensions

import net.minecraft.entity.Entity
import net.minecraft.util.math.RayTraceResult

/**
 * @author shadowfacts
 */
fun Entity.rayTrace(distance: Double): RayTraceResult? {
	val eyePos = getPositionEyes(0f)
	val lookVec = getLook(0f)
	val vec = eyePos.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance)
	return worldObj.rayTraceBlocks(eyePos, vec, false, false, true)
}