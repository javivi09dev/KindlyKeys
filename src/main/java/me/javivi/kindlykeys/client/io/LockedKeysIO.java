package me.javivi.kindlykeys.client.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import me.javivi.kindlykeys.shared.KindlyKeysShared;
import me.javivi.kindlykeys.shared.objects.IServerSideChecker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
public class LockedKeysIO {
    public static final LockedKeysIO INSTANCE = new LockedKeysIO();
    private static final Path CONFIG_PATH = Path.of("config", "locked_keys.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private Set<String> disabledKeys = new HashSet<>();
    private boolean debugEnabled = true;

    public LockedKeysIO() {}

    public void load() throws IOException {
        if (Files.exists(CONFIG_PATH)) {
            LockedKeysIO loaded = GSON.fromJson(Files.readString(CONFIG_PATH), LockedKeysIO.class);
            this.disabledKeys = loaded.disabledKeys;
        }
    }

    public void save() throws IOException {
        Files.writeString(CONFIG_PATH, GSON.toJson(this));
    }

    private void addDisabledKey(String key) {
        this.disabledKeys.add(key);
    }

    private void removeDisabledKey(String key) {
        this.disabledKeys.remove(key);
    }

    public boolean isKeyDisabled(String key) {
        return this.disabledKeys.contains(key);
    }

    public void setKeyLocked(String keyName, boolean locked) {
        if (locked) {
            this.addDisabledKey(keyName);
        } else {
            this.removeDisabledKey(keyName);
        }
        try {
            this.save();
        } catch (IOException e) {
            KindlyKeysShared.LOGGER.error("Failed to save locked keys", e);
        }
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public boolean isDebugEnabled() {
        return this.debugEnabled;
    }

    public Collection<String> getDisabledKeys() {
        return this.disabledKeys;
    }

    public static void cancelKeyIfRequired(int key, int scancode, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;

        if (player instanceof IServerSideChecker checker && checker.keyChanger_Shared$isServerSidePresent()) {
            KeyMapping mapping = null;
            for (KeyMapping keyMapping : client.options.keyMappings) {
                if (keyMapping.matches(key, scancode)) {
                    mapping = keyMapping;
                    break;
                }
            }

            if (mapping != null) {
                debug(String.format("Key %s pressed", mapping.getName()));
                if (INSTANCE.isKeyDisabled(mapping.getName())) {
                    debug(String.format("Key %s is disabled", mapping.getName()));
                    ci.cancel();
                }
            } else {
                checkExtraKeys(key, ci);
            }
        } else {
            ci.cancel();
        }
    }

    private static void checkExtraKeys(int key, CallbackInfo ci) {
        int checkKey = 290; // Starting key for extra keys
        for (int i = 0; i < 12; i++) {
            if (key == checkKey) {
                debug(String.format("Key extra.f%d pressed", i + 1));
                if (INSTANCE.isKeyDisabled("extra.f%d".formatted(i + 1))) {
                    debug(String.format("Key extra.f%d is disabled", i + 1));
                    ci.cancel();
                }
            }
            checkKey++;
        }
    }

    @Unique
    private static void debug(String message) {
        Player player = Minecraft.getInstance().player;
        if (player != null && INSTANCE.isDebugEnabled()) {
            player.sendSystemMessage(Component.literal(message));
        }
    }
}

