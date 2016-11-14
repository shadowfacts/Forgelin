package net.shadowfacts.forgelin

import net.minecraftforge.fml.common.FMLModContainer
import net.minecraftforge.fml.common.ILanguageAdapter
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Forge {@link ILanguageAdapter} for Kotlin
 * Usage: Set the {@code modLanguageAdapter} field in your {@code @Mod} annotation to {@code net.shadowfacts.forgelin.KotlinAdapter}
 * @author shadowfacts
 */
class KotlinAdapter : ILanguageAdapter {

	private val log = LogManager.getLogger("KotlinAdapter")

	override fun supportsStatics(): Boolean {
		return false
	}

	override fun setProxy(target: Field, proxyTarget: Class<*>, proxy: Any) {
		log.debug("Setting proxy: ${target.declaringClass.simpleName}.${target.name} -> $proxy")
		if (proxyTarget.fields.any { x -> x.name == "INSTANCE" }) {
			// Singleton
			try {
				log.debug("Setting proxy on INSTANCE; singleton target")
				val obj = proxyTarget.getField("INSTANCE").get(null)
				target.set(obj, proxy)
			} catch (e: Exception) {
				throw KotlinAdapterException(e)
			}
		} else {
			target.set(proxyTarget, proxy)
		}
	}

	override fun getNewInstance(container: FMLModContainer, objectClass: Class<*>, classLoader: ClassLoader, factoryMarkedAnnotation: Method?): Any {
		log.debug("FML has asked for ${objectClass.simpleName} to be constructed")
		try {
			// Try looking for an object type
			val f = objectClass.getField("INSTANCE")
			val obj = f.get(null) ?: throw NullPointerException()
			log.debug("Found an object INSTANCE reference in ${objectClass.simpleName}, using that. ${obj}")
			return obj
		} catch (e: Exception) {
			// Try looking for a class type
			log.debug("Failed to get object reference, trying class construction")
			try {
				val obj = objectClass.newInstance() ?: throw NullPointerException()
				log.debug("Constructed an object from a class type ($objectClass), using that. $obj")
				return obj
			} catch (e: Exception) {
				throw KotlinAdapterException(e)
			}
		}
	}

	override fun setInternalProxies(mod: ModContainer?, side: Side?, loader: ClassLoader?) {
		// Nothing to do; FML's got this covered for Kotlin
	}

	private class KotlinAdapterException(e: Exception) : RuntimeException("Kotlin adapter error - do not report to Forge!", e)

}