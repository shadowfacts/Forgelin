package net.shadowfacts.forgelin

import net.minecraftforge.fml.Logging.SCAN
import net.minecraftforge.fml.javafmlmod.FMLJavaModLanguageProvider
import net.minecraftforge.forgespi.language.ILifecycleEvent
import net.minecraftforge.forgespi.language.IModLanguageProvider
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.stream.Collectors

/**
 * Forge {@link ILanguageAdapter} for Kotlin
 * Usage: Set the {@code modLanguageAdapter} field in your {@code @Mod} annotation to {@code net.shadowfacts.forgelin.FMLKotlinModLanguageProvider}
 * @author shadowfacts
 */
class FMLKotlinModLanguageProvider : IModLanguageProvider {
	private val log = LogManager.getLogger()

	override fun name(): String {
		return "kotlinfml"
	}

	override fun getFileVisitor(): Consumer<ModFileScanData> {
		return Consumer { scanResult ->
			val modTargetMap = scanResult.annotations.stream()
					.filter { ad -> ad.annotationType == FMLJavaModLanguageProvider.MODANNOTATION }
					.peek { ad -> log.debug(SCAN, "Found @Mod class {} with id {}", ad.classType.className, ad.annotationData["value"]) }
					.map { ad -> FMLKotlinModTarget(ad.classType.className, ad.annotationData["value"] as String) }
					.collect(Collectors.toMap(java.util.function.Function<FMLKotlinModTarget, String> { it.modId }, java.util.function.Function.identity<FMLKotlinModTarget>()))
			scanResult.addLanguageLoader(modTargetMap)
		}
	}

	override fun <R : ILifecycleEvent<R>?> consumeLifecycleEvent(consumeEvent: Supplier<R>?) {}
}