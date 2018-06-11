package net.shadowfacts.forgelin

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.LoaderException
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.common.discovery.ASMDataTable
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation.EnumHolder
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Modifier
import java.util.EnumSet
import kotlin.reflect.full.companionObjectInstance

object ForgelinAutomaticEventSubscriber {
    private val DEFAULT_SUBSCRIPTION_SIDES = EnumSet.allOf(Side::class.java)
    private val LOGGER = LogManager.getLogger(ForgelinAutomaticEventSubscriber::class.java)

    fun subscribeAutomatic(mod: ModContainer, asm: ASMDataTable, currentSide: Side) {
        LOGGER.debug("Attempting to register Kotlin @EventBusSubscriber objects for {}", mod.modId)

        val modAnnotations = asm.getAnnotationsFor(mod) ?: return

        val containedMods = modAnnotations.get(Mod::class.java.name)
        val subscribers = modAnnotations.get(Mod.EventBusSubscriber::class.java.name)
                .filter { parseTargetSides(it).contains(currentSide) }

        val loader = Loader.instance().modClassLoader

        for (subscriber in subscribers) {
            try {
                val ownerModId = parseModId(containedMods, subscriber)
                if (ownerModId.isNullOrEmpty()) {
                    LOGGER.warn("Could not determine owning mod for @EventBusSubscriber on {} for mod {}", subscriber.className, mod.modId)
                    continue
                }

                if (mod.modId != ownerModId) {
                    LOGGER.debug("Skipping @EventBusSubscriber injection for {} since it is not for mod {}", subscriber.className, mod.modId)
                    continue
                }

                LOGGER.debug("Registering @EventBusSubscriber object for {} for mod {}", subscriber.className, mod.modId)

                val subscriberClass = Class.forName(subscriber.className, false, loader) ?: continue
                val kotlinClass = subscriberClass.kotlin
                val objectInstance = kotlinClass.objectInstance ?: kotlinClass.companionObjectInstance ?: continue

                if (!hasStaticEventHandlers(subscriberClass)) {
                    MinecraftForge.EVENT_BUS.unregister(subscriberClass)
                    LOGGER.debug("Unregistered static @EventBusSubscriber class {}", subscriber.className)
                }
                if (hasObjectEventHandlers(objectInstance)) {
                    MinecraftForge.EVENT_BUS.register(objectInstance)
                    LOGGER.debug("Registered @EventBusSubscriber object instance {}", subscriber.className)
                }
            } catch (e: Throwable) {
                LOGGER.error("An error occurred trying to load an @EventBusSubscriber object {} for modid {}", mod.modId, e)
                throw LoaderException(e)
            }
        }
    }

    private fun hasObjectEventHandlers(objectInstance: Any): Boolean {
        return objectInstance.javaClass.methods.any {
            !Modifier.isStatic(it.modifiers) && it.isAnnotationPresent(SubscribeEvent::class.java)
        }
    }

    private fun hasStaticEventHandlers(clazz: Class<*>): Boolean {
        return clazz.methods.any {
            Modifier.isStatic(it.modifiers) && it.isAnnotationPresent(SubscribeEvent::class.java)
        }
    }

    private fun parseModId(containedMods: MutableSet<ASMData>, subscriber: ASMData): String? {
        val parsedModId: String? = subscriber.annotationInfo["modid"] as? String
        if (parsedModId.isNullOrEmpty()) {
            return parsedModId
        }

        return ASMDataTable.getOwnerModID(containedMods, subscriber)
    }

    private fun parseTargetSides(subscriber: ASMData): EnumSet<Side> {
        val parsedSides: List<EnumHolder>? = subscriber.annotationInfo["value"] as? List<EnumHolder>
        if (parsedSides != null) {
            val targetSides = EnumSet.noneOf(Side::class.java)
            for (parsed in parsedSides) {
                targetSides.add(Side.valueOf(parsed.value))
            }
            return targetSides
        }
        return DEFAULT_SUBSCRIPTION_SIDES
    }
}
