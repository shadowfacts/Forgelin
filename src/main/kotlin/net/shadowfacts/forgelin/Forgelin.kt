package net.shadowfacts.forgelin

import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLConstructionEvent

/**
 * @author shadowfacts
 */
@Mod(modid = Forgelin.MOD_ID, name = Forgelin.NAME, version = Forgelin.VERSION, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object Forgelin {

	const val MOD_ID = "forgelin"
	const val NAME = "Forgelin"
	const val VERSION = "@VERSION@"

	@EventHandler
	fun onConstruction(event: FMLConstructionEvent) {
		Loader.instance().modList.forEach {
			ForgelinAutomaticEventSubscriber.subscribeAutomatic(it, event.asmHarvestedData, FMLCommonHandler.instance().side)
		}
	}
}
