package net.shadowfacts.forgelin

import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod("forgelin_test")
object ForgelinTest {
	val logger: Logger = LogManager.getLogger()

	init {
		// You either need to specify generic type explicitly and use a consumer
		FMLKotlinModLoadingContext.get().modEventBus.addListener<FMLCommonSetupEvent> { setup(it) }
		// use a consumer with parameter types specified
		FMLKotlinModLoadingContext.get().modEventBus.addListener { event: FMLCommonSetupEvent -> setup2(event) }
		// or just register whole object and mark needed method with SubscribeEvent annotations.
		FMLKotlinModLoadingContext.get().modEventBus.register(this)
	}

	fun setup(event: FMLCommonSetupEvent) {
		logger.info("HELLO from setup")
	}

	fun setup2(event: FMLCommonSetupEvent) {
		logger.info("HELLO from setup2")
	}

	@SubscribeEvent
	fun setup3(event: FMLCommonSetupEvent) {
		logger.info("HELLO from setup3")
	}

	@Mod.EventBusSubscriber
	object EventSubscriber {
		// doesn't work
		@SubscribeEvent
		fun testNonStatic(event: EntityJoinWorldEvent) {
			logger.info("HELLO from testNonStatic")
		}

		@JvmStatic
		@SubscribeEvent
		fun testStatic(event: EntityJoinWorldEvent) {
			logger.info("HELLO from testStatic")
		}
	}
}
