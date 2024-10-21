package me.javivi.kindlykeys.client;

import java.io.IOException;
import me.javivi.kindlykeys.client.io.LockedKeysIO;
import me.javivi.kindlykeys.shared.KindlyKeysShared;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(
        modid = "kindlykeys",
        bus = Bus.MOD
)
public class KeyChangerClient {
    public KeyChangerClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        try {
            LockedKeysIO.INSTANCE.load();
        } catch (IOException var2) {
            IOException e = var2;
            KindlyKeysShared.LOGGER.error("Failed to load locked keys", e);
        }

    }
}
