package net.shadowfacts.forgelin

import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModLoadingContext

object FMLKotlinModLoadingContext {
    fun get(): Context {
        return ModLoadingContext.get().extension()
    }

    class Context(private val container: FMLKotlinModContainer) {
        val modEventBus: IEventBus
            get() = container.eventBus
    }
}