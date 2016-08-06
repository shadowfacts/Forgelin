import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

/**
 * @author shadowfacts
 */
@Mod(modid = "test", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object Test {

	@Mod.EventHandler
	fun preInit(event: FMLPreInitializationEvent) {
		FMLLog.bigWarning("Hello from Kotlin!")
	}

}