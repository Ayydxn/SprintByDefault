package me.yoarcane.sprintbydefault;

import me.yoarcane.sprintbydefault.config.SprintByDefaultConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

@Environment(EnvType.CLIENT)
public class SprintByDefaultClientMod implements ClientModInitializer
{
    private static final Logger LOGGER = (Logger) LogManager.getLogger("Sprint By Default");

    @Override
    public void onInitializeClient()
    {
        new SprintByDefaultConfig().initialize();

        ClientTickEvents.START_CLIENT_TICK.register(client ->
        {
            if (SprintByDefaultConfig.getInstance().isSprintByDefaultEnabled && client.player != null && client.player.input.hasForwardMovement())
                client.player.setSprinting(true);
        });
    }

    public static Logger getLogger() { return LOGGER; }
}
