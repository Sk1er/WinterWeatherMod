package club.sk1er.mods.wintermod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Mod(modid = WinterWeatherMod.MODID, version = WinterWeatherMod.VERSION)
public class WinterWeatherMod {
    public static final String MODID = "winter_weather";
    public static final String VERSION = "1.0";
    private boolean modEnabled = true;
    private final SnowRenderHandler snowRenderer = new SnowRenderHandler();
    private IRenderHandler normalRenderer;
    private Sk1erMod sk1erMod;
    private File configFile = null;

    @Mod.Instance(WinterWeatherMod.MODID)
    public static WinterWeatherMod mod;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configFile = event.getSuggestedConfigurationFile();
        try {
            JsonObject object = new JsonParser().parse(FileUtils.readFileToString(configFile)).getAsJsonObject();
            if (object.has("enabled")) {
                modEnabled = object.get("enabled").getAsBoolean();
            }
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        sk1erMod = new Sk1erMod(MODID, VERSION, "Winter Weather Mod");
        sk1erMod.checkStatus();
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new CommandWinterWeather());
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        normalRenderer = event.world.provider.getWeatherRenderer();
        if (modEnabled) {
            event.world.provider.setWeatherRenderer(snowRenderer);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            snowRenderer.rendererUpdateCount++;
        }
    }

    public boolean toggleActive() {
        if (modEnabled) {
            Minecraft.getMinecraft().theWorld.provider.setWeatherRenderer(normalRenderer);
        } else {
            Minecraft.getMinecraft().theWorld.provider.setWeatherRenderer(snowRenderer);
        }
        modEnabled = !modEnabled;
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("enabled", modEnabled);
            FileUtils.writeStringToFile(configFile, jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modEnabled;
    }
}
