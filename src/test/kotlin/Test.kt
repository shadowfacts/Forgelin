import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

/**
 * @author shadowfacts
 */
@Mod(modid = "test", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object Test {

	@SidedProxy(clientSide = "ClientProxy", serverSide = "CommonProxy")
	lateinit var proxy: CommonProxy
		private set

	@Mod.EventHandler
	fun preInit(event: FMLPreInitializationEvent) {
		FMLLog.bigWarning("Hello from Kotlin!")
		proxy.someMethod()
	}

}

open class CommonProxy {
	open fun someMethod() {
		FMLLog.bigWarning("CommonProxy.someMethod")
	}
}

class ClientProxy: CommonProxy() {
	override fun someMethod() {
		super.someMethod()
		FMLLog.bigWarning("ClientProxy.someMethod")
	}
}