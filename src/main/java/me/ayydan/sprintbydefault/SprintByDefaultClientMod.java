package me.ayydan.sprintbydefault;

import me.ayydan.sprintbydefault.config.SprintByDefaultConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("sprint-by-default")
                .then(CommandManager.literal("on").executes(context ->
                {
                    if (SprintByDefaultConfig.getInstance().isSprintByDefaultEnabled)
                    {
                        context.getSource().sendMessage(Text.literal("Sprint by Default is already enabled!").formatted(Formatting.GOLD));
                        return 1;
                    }

                    SprintByDefaultConfig.getInstance().isSprintByDefaultEnabled = true;
                    SprintByDefaultConfig.getInstance().writeConfig();

                    context.getSource().sendMessage(Text.literal("Enabled Sprint by Default").formatted(Formatting.GREEN));

                    return 1;
                })).then(CommandManager.literal("off").executes(context ->
                {
                    if (!SprintByDefaultConfig.getInstance().isSprintByDefaultEnabled)
                    {
                        context.getSource().sendMessage(Text.literal("Sprint by Default is already disabled!").formatted(Formatting.GOLD));
                        return 1;
                    }

                    SprintByDefaultConfig.getInstance().isSprintByDefaultEnabled = false;
                    SprintByDefaultConfig.getInstance().writeConfig();

                    context.getSource().sendMessage(Text.literal("Disabled Sprint by Default").formatted(Formatting.RED));
                    return 1;
                }))));

        ClientTickEvents.START_CLIENT_TICK.register(client ->
        {
            if (SprintByDefaultConfig.getInstance().isSprintByDefaultEnabled && client.player != null && client.player.input.hasForwardMovement())
                client.player.setSprinting(true);
        });
    }

    public static Logger getLogger() { return LOGGER; }
}
