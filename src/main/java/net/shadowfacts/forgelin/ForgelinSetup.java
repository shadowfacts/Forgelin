package net.shadowfacts.forgelin;

import net.minecraftforge.fml.relauncher.IFMLCallHook;

import java.util.Map;

/**
 * @author shadowfacts
 */
public class ForgelinSetup implements IFMLCallHook {

	@Override
	public void injectData(Map<String, Object> data) {
		ClassLoader loader = (ClassLoader)data.get("classLoader");
		try {
			loader.loadClass("net.shadowfacts.forgelin.ForgelinAdapter");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Void call() throws Exception {
		return null;
	}

}
