package net.shadowfacts.forgelin

import net.minecraftforge.eventbus.EventBusErrorMessage
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.IEventListener
import net.minecraftforge.fml.*
import net.minecraftforge.fml.Logging.LOADING
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager
import java.util.*
import java.util.function.Consumer

class FMLKotlinModContainer(
        info: IModInfo,
        private val className: String,
        modClassLoader: ClassLoader,
        private val scanResults: ModFileScanData
) : ModContainer(info) {
    private val logger = LogManager.getLogger()

    val eventBus: IEventBus
    private var mod: Any? = null
    private val modClass: Class<*>

    init {
        logger.debug(LOADING, "Creating FMLModContainer instance for {} with classLoader {} & {}", className, modClassLoader, javaClass.classLoader)
        triggerMap[ModLoadingStage.CONSTRUCT] = dummy().andThen(::beforeEvent).andThen(::constructMod).andThen(::afterEvent)
        triggerMap[ModLoadingStage.CREATE_REGISTRIES] = dummy().andThen(::beforeEvent).andThen(::fireEvent).andThen(::afterEvent)
        triggerMap[ModLoadingStage.LOAD_REGISTRIES] = dummy().andThen(::beforeEvent).andThen(::fireEvent).andThen(::afterEvent)
        triggerMap[ModLoadingStage.COMMON_SETUP] = dummy().andThen(::beforeEvent).andThen(::fireEvent).andThen(::afterEvent)
        triggerMap[ModLoadingStage.SIDED_SETUP] = dummy().andThen(::beforeEvent).andThen(::fireEvent).andThen(::afterEvent)
        triggerMap[ModLoadingStage.ENQUEUE_IMC] = dummy().andThen(::beforeEvent).andThen(::fireEvent).andThen(::afterEvent)
        triggerMap[ModLoadingStage.PROCESS_IMC] = dummy().andThen(::beforeEvent).andThen(::fireEvent).andThen(::afterEvent)
        triggerMap[ModLoadingStage.COMPLETE] = dummy().andThen(::beforeEvent).andThen(::fireEvent).andThen(::afterEvent)
        eventBus = IEventBus.create(::onEventFailed)
        configHandler = Optional.of<Consumer<ModConfig.ModConfigEvent>>(Consumer { event -> eventBus.post(event) })

        try {
            // Here, we won't init the class, meaning static {} blocks (init {} in kotlin) won't get triggered
            // but we will still have to do it later, on CONSTRUCT phase.
            modClass = Class.forName(className, false, modClassLoader)
            logger.debug(LOADING, "Loaded modclass {} with {}", modClass.name, modClass.classLoader)
        } catch (e: Throwable) {
            logger.error(LOADING, "Failed to load class {}", className, e)
            throw ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e)
        }

    }

    private fun constructMod(event: LifecycleEventProvider.LifecycleEvent) {
        try {
            logger.debug(LOADING, "Loading mod instance {} of type {}", getModId(), modClass.name)
            // Now we can load the class, so that static {} block gets called
            Class.forName(className)
            // Then we check whether it's a kotlin object and return it, or if not we create a new instance of kotlin class.
            this.mod = modClass.kotlin.objectInstance ?: modClass.newInstance()
            logger.debug(LOADING, "Loaded mod instance {} of type {}", getModId(), modClass.name)
        } catch (e: Throwable) {
            logger.error(LOADING, "Failed to create mod instance. ModID: {}, class {}", getModId(), modClass.name, e)
            throw ModLoadingException(modInfo, event.fromStage(), "fml.modloading.failedtoloadmod", e, modClass)
        }

        logger.debug(LOADING, "Injecting Automatic event subscribers for {}", getModId())
        ForgelinAutomaticEventSubscriber.inject(this, this.scanResults, this.modClass.classLoader)
        logger.debug(LOADING, "Completed Automatic event subscribers for {}", getModId())
    }

    private fun dummy(): Consumer<LifecycleEventProvider.LifecycleEvent> = Consumer {}

    private fun beforeEvent(lifecycleEvent: LifecycleEventProvider.LifecycleEvent) {
        FMLKotlinModLoadingContext.get().activeContainer = this
        ModThreadContext.get().activeContainer = this
    }

    private fun fireEvent(lifecycleEvent: LifecycleEventProvider.LifecycleEvent) {
        val event = lifecycleEvent.getOrBuildEvent(this)
        logger.debug(LOADING, "Firing event for modid {} : {}", this.getModId(), event)
        try {
            eventBus.post(event)
            logger.debug(LOADING, "Fired event for modid {} : {}", this.getModId(), event)
        } catch (e: Throwable) {
            logger.error(LOADING, "Caught exception during event {} dispatch for modid {}", event, this.getModId(), e)
            throw ModLoadingException(modInfo, lifecycleEvent.fromStage(), "fml.modloading.errorduringevent", e)
        }

    }

    private fun afterEvent(lifecycleEvent: LifecycleEventProvider.LifecycleEvent) {
        ModThreadContext.get().activeContainer = null
        FMLKotlinModLoadingContext.get().activeContainer = null
        if (currentState == ModLoadingStage.ERROR) {
            logger.error(LOADING, "An error occurred while dispatching event {} to {}", lifecycleEvent.fromStage(), getModId())
        }
    }

    private fun onEventFailed(iEventBus: IEventBus, event: Event, iEventListeners: Array<IEventListener>, i: Int, throwable: Throwable) {
        logger.error(EventBusErrorMessage(event, i, iEventListeners, throwable))
    }

    override fun matches(mod: Any): Boolean = mod === this.mod
    override fun getMod(): Any? = mod
}