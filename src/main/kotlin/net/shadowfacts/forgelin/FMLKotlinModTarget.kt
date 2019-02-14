package net.shadowfacts.forgelin

import net.minecraftforge.fml.Logging.LOADING
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.IModLanguageProvider
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager
import java.lang.Exception

class FMLKotlinModTarget(private val className: String, val modId: String) : IModLanguageProvider.IModLanguageLoader {
    private val logger = LogManager.getLogger()

    override fun <T> loadMod(info: IModInfo, modClassLoader: ClassLoader, modFileScanResults: ModFileScanData): T {
        try {
            return FMLKotlinModContainer(info, className, modClassLoader, modFileScanResults) as T

            /*logger.debug(LOADING, "Loading FMLKotlinModContainer from classloader {}", Thread.currentThread().contextClassLoader)
            val fmlContainer = Class.forName("net.shadowfacts.forgelin.FMLKotlinModContainer", true, Thread.currentThread().contextClassLoader)
            logger.debug(LOADING, "Loading FMLKotlinModContainer got {}", fmlContainer.classLoader)
            val constructor = fmlContainer.getConstructor(IModInfo::class.java, String::class.java, ClassLoader::class.java, ModFileScanData::class.java)
            return constructor.newInstance(info, className, modClassLoader, modFileScanResults) as T*/
        } catch (e: Exception) {
            logger.fatal(LOADING, "Unable to load FMLKotlinModContainer, wut?", e)
            throw e
        }
    }
}