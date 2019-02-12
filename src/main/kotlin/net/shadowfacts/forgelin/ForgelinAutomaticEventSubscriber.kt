package net.shadowfacts.forgelin

import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.Logging
import net.minecraftforge.fml.ModContainer
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLEnvironment
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager
import org.objectweb.asm.Type
import java.util.*
import java.util.stream.Collectors

object ForgelinAutomaticEventSubscriber {
    private val AUTO_SUBSCRIBER = Type.getType(Mod.EventBusSubscriber::class.java)

    private val logger = LogManager.getLogger()

    fun inject(mod: ModContainer, scanData: ModFileScanData?, loader: ClassLoader) {
        if(scanData == null) {
            return
        }

        logger.debug(Logging.LOADING, "Attempting to inject @EventBusSubscriber classes into the eventbus for {}", mod.modId)
        val targets = scanData.annotations.stream()
                .filter { annotationData -> annotationData.annotationType == AUTO_SUBSCRIBER }
                .filter { annotationData -> shouldBeRegistered(mod.modId, annotationData) }
                .collect(Collectors.toList())

        targets.forEach { ad ->
            val busTargetHolder = ad.annotationData.getOrDefault("bus", ModAnnotation.EnumHolder(null, "FORGE")) as ModAnnotation.EnumHolder
            val busTarget = Mod.EventBusSubscriber.Bus.valueOf(busTargetHolder.value)

            try {
                logger.debug(Logging.LOADING, "Auto-subscribing {} to {}", ad.classType.className, busTarget)
                val className = Class.forName(ad.classType.className, true, loader)
                busTarget.bus().get().register(className.kotlin.objectInstance ?: className)
            } catch (e: ClassNotFoundException) {
                logger.fatal(Logging.LOADING, "Failed to load mod class {} for @EventBusSubscriber annotation", ad.classType, e)
                throw e
            }
        }
    }

    private fun shouldBeRegistered(modId: String, ad: ModFileScanData.AnnotationData): Boolean {
        val sidesValue = ad.annotationData.getOrDefault("value", Arrays.asList(ModAnnotation.EnumHolder(null, "CLIENT"), ModAnnotation.EnumHolder(null, "DEDICATED_SERVER"))) as List<ModAnnotation.EnumHolder>
        val sides = sidesValue.stream()
                .map { eh -> Dist.valueOf(eh.value) }
                .collect(Collectors.toCollection { EnumSet.noneOf(Dist::class.java) }) as EnumSet<Dist>
        val annotationModId = ad.annotationData.getOrDefault("modid", modId) as String

        return modId == annotationModId && sides.contains(FMLEnvironment.dist)
    }
}
