package me.yoarcane.sprintbydefault.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.yoarcane.sprintbydefault.SprintByDefaultClientMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.Collections;

public class SprintByDefaultConfig
{
    private static SprintByDefaultConfig INSTANCE = null;

    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().resolve("sprint-by-default") +
            "\\sprint-by-default-settings.json");

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    public boolean isSprintByDefaultEnabled = true;

    public void initialize()
    {
        INSTANCE = this;

        if (CONFIG_FILE.exists())
            loadConfig();
        else
            writeConfig();
    }

    public void loadConfig()
    {
        try
        {
            StringBuilder configFileContents = new StringBuilder();
            BufferedReader fileReader = new BufferedReader(new FileReader(CONFIG_FILE));

            String currentLine;
            while ((currentLine = fileReader.readLine()) != null)
                configFileContents.append(currentLine).append("\n");

            INSTANCE = GSON.fromJson(configFileContents.toString(), SprintByDefaultConfig.class);
        }
        catch (IOException exception)
        {
            SprintByDefaultClientMod.getLogger().error("Failed to read the Sprint By Default config!");

            exception.printStackTrace();
        }
    }

    public void writeConfig()
    {
        try
        {
            if (!Files.exists(CONFIG_FILE.getParentFile().toPath()) || !Files.exists(CONFIG_FILE.toPath()))
                Files.createDirectories(CONFIG_FILE.getParentFile().toPath());

            Files.write(CONFIG_FILE.toPath(), Collections.singleton(GSON.toJson(INSTANCE)));
        }
        catch (IOException exception)
        {
            SprintByDefaultClientMod.getLogger().error("Failed to write the Sprint By Default config!");

            exception.printStackTrace();
        }
    }

    public static SprintByDefaultConfig getInstance() { return INSTANCE; }
}
