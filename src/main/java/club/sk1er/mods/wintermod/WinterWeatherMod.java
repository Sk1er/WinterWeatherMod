package club.sk1er.mods.wintermod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = WinterWeatherMod.MODID, version = WinterWeatherMod.VERSION)
public class WinterWeatherMod {
    public static final String MODID = "winter_weather";
    public static final String VERSION = "1.0";
    private final SnowRenderHandler snowRenderer = new SnowRenderHandler();
    private Sk1erMod sk1erMod;
    private Minecraft mc;
    ;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        sk1erMod = new Sk1erMod(MODID, VERSION, "Winter Weather Mod");
        sk1erMod.checkStatus();
        MinecraftForge.EVENT_BUS.register(this);
        mc = Minecraft.getMinecraft();

    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        event.world.provider.setWeatherRenderer(snowRenderer);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            snowRenderer.rendererUpdateCount++;
        }
    }

}
