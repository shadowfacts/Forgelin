package net.shadowfacts.forgelin

import net.minecraftforge.fml.common.FMLModContainer
import net.minecraftforge.fml.common.ILanguageAdapter
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Kotlin implementation of FML's ILanguageAdapter.
 *
 * Use by setting <pre>modLanguageAdapter = "io.drakon.forgelinFR.KotlinAdapter"</pre> in the Mod annotation
 * (Forge 1.8-11.14.1.1371 or above required).
 *
 * @author Arkan <arkan@drakon.io>
 */
public class KotlinAdapter : ILanguageAdapter {

	companion object metadata {
		public final val ADAPTER_VERSION: String = "@VERSION@-@KOTLIN@"
	}

	private val log = LogManager.getLogger("ILanguageAdapter/Kotlin")

	override fun supportsStatics(): Boolean {
		return false
	}

	override fun setProxy(target: Field, proxyTarget: Class<*>, proxy: Any) {
		log.debug("Setting proxy: {}.{} -> {}", target.getDeclaringClass().getSimpleName(), target.getName(), proxy)
		if (proxyTarget.getFields().any { x -> x.getName().equals("INSTANCE") }) {
			// Singleton
			try {
				log.debug("Setting proxy on INSTANCE; singleton target.")
				val obj = proxyTarget.getField("INSTANCE").get(null)
				target.set(obj, proxy)
			} catch (ex: Exception) {
				throw KotlinAdapterException(ex)
			}
		} else {
			//TODO Log?
			target.set(proxyTarget, proxy)
		}
	}

	override fun getNewInstance(container: FMLModContainer?, objectClass: Class<*>, classLoader: ClassLoader, factoryMarkedAnnotation: Method?): Any? {
		log.debug("FML has asked for {} to be constructed...", objectClass.getSimpleName())
		try {
			// Try looking for an object type
			val f = objectClass.getField("INSTANCE")
			val obj = f.get(null)
			if (obj == null) throw NullPointerException()
			log.debug("Found an object INSTANCE reference in {}, using that. ({})", objectClass.getSimpleName(), obj)
			return obj
		} catch (ex: Exception) {
			// Try looking for a class type
			log.debug("Failed to get object reference, trying class construction.")
			try {
				val obj = objectClass.newInstance()
				if (obj == null) throw NullPointerException()
				log.debug("Constructed an object from a class type ({}), using that. ({})", objectClass, obj)
				log.warn("Hey, you, modder who owns {} - you should be using 'object' instead of 'class' on your @Mod class.", objectClass.getSimpleName())
				return obj
			} catch (ex: Exception) {
				throw KotlinAdapterException(ex)
			}
		}
	}

	override fun setInternalProxies(mod: ModContainer?, side: Side?, loader: ClassLoader?) {
		// Nothing to do; FML's got this covered for Kotlin.
	}

	private class KotlinAdapterException(ex: Exception) : RuntimeException("Kotlin adapter error - do not report to Forge!", ex)

}