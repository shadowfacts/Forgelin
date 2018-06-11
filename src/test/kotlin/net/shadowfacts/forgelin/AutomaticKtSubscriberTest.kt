package net.shadowfacts.forgelin

import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod(modid = AutomaticKtSubscriberTest.MODID, modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object AutomaticKtSubscriberTest {
	const val MODID = "ktsubtest"

	@EventBusSubscriber(modid = AutomaticKtSubscriberTest.MODID)
	object EventSubscriber {
		@SubscribeEvent
		fun onRightClickBlock(event: RightClickBlock) {
			println("Automatic KT subscriber: Right click ${event.pos}")
		}

		@JvmStatic
		@SubscribeEvent
		fun onRightClickItem(event: PlayerInteractEvent.RightClickItem) {
			println("Right click item")
		}
	}
}
