package iot.rmi;


import com.hivemq.spi.plugin.meta.Information;

import com.hivemq.spi.HiveMQPluginModule;
import com.hivemq.spi.PluginEntryPoint;

@Information(name = "HiveMQ Intelligent Home Plugin", author = "Ed Bullick", version = "3.0.0")
public class Module extends HiveMQPluginModule{

	@Override
	protected void configurePlugin() {
	}

	@Override
	protected Class<? extends PluginEntryPoint> entryPointClass() {
		// points to main class
		return MainClass.class;
	}

}