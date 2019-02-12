package net.shadowfacts.forgelin

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ExtensionPoint
import net.minecraftforge.fml.config.ModConfig
import java.util.function.Supplier

object FMLKotlinModLoadingContext {
    private val context = ThreadLocal.withInitial { FMLKotlinModLoadingContext }
    var activeContainer: FMLKotlinModContainer? = null

    val modEventBus: IEventBus
        get() = activeContainer!!.eventBus

    fun get(): FMLKotlinModLoadingContext {
        return context.get()
    }

    fun <T> registerExtensionPoint(point: ExtensionPoint<T>, extension: Supplier<T>) {
        activeContainer!!.registerExtensionPoint(point, extension)
    }

    fun registerConfig(type: ModConfig.Type, spec: ForgeConfigSpec) {
        activeContainer!!.addConfig(ModConfig(type, spec, activeContainer!!))
    }

    fun registerConfig(type: ModConfig.Type, spec: ForgeConfigSpec, fileName: String) {
        activeContainer!!.addConfig(ModConfig(type, spec, activeContainer, fileName))
    }
}