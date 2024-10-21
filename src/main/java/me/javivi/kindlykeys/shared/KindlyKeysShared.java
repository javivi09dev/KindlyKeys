package me.javivi.kindlykeys.shared;

import com.mojang.logging.LogUtils;
import me.javivi.kindlykeys.shared.net.ModPacketHandler;
import me.javivi.kindlykeys.shared.net.ModPacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod("keychanger_shared")
public class KindlyKeysShared {
    public static final String MOD_ID = "keychanger";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KindlyKeysShared() {
        me.javivi.kindlykeys.shared.net.ModPacketHandler.register();
        MinecraftForge.EVENT_BUS.register(this);
    }
}