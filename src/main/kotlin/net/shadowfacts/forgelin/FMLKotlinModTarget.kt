package net.shadowfacts.forgelin

import net.minecraftforge.fml.Logging.LOADING
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.IModLanguageProvider
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager

class FMLKotlinModTarget(private val className: String, val modId: String) : IModLanguageProvider.IModLanguageLoader {
    private val logger = LogManager.getLogger()

    override fun <T> loadMod(info: IModInfo, modClassLoader: ClassLoader, modFileScanResults: ModFileScanData): T {
        // This language class is loaded in the system level classloader - before the game even starts
        // So we must treat container construction as an arms length operation, and load the container
        // in the classloader of the game - the context classloader is appropriate here.
        try {
            val fmlContainer = Class.forName("net.shadowfacts.forgelin.FMLKotlinModContainer", true, Thread.currentThread().contextClassLoader)
            logger.debug(LOADING, "Loading FMLKotlinModContainer from classloader {} - got {}", Thread.currentThread().contextClassLoader, fmlContainer.classLoader)
            val constructor = fmlContainer.getConstructor(IModInfo::class.java, String::class.java, ClassLoader::class.java, ModFileScanData::class.java)
            return constructor.newInstance(info, className, modClassLoader, modFileScanResults) as T
        } catch (e: ReflectiveOperationException) {
            logger.fatal(LOADING, "Unable to load FMLKotlinModContainer, wut?", e)
            throw e
        }
    }
}