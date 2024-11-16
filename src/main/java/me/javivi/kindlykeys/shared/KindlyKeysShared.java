// src/main/java/me/javivi/kindlykeys/shared/KindlyKeysShared.java
package me.javivi.kindlykeys.shared;

import com.mojang.logging.LogUtils;
import me.javivi.kindlykeys.shared.net.ModPacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.api.distmarker.Dist;
import org.slf4j.Logger;

@Mod("keychanger_shared")
public class KindlyKeysShared {
    public static final String MOD_ID = "keychanger";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KindlyKeysShared() {
        ModPacketHandler.register();
        MinecraftForge.EVENT_BUS.register(this);

        // Use DistExecutor to ensure client-only code is not executed on the server
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientOnly::loadKeys);
    }

    // Inner class to handle client-only code
    private static class ClientOnly {
        public static void loadKeys() {
            me.javivi.kindlykeys.client.io.LockedKeysIO.loadKeys();
        }
    }
}