package me.javivi.kindlykeys.shared.objects;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;

public class Reference {
    // Accesor para comprobar si el lado del servidor está presente.
    public static final EntityDataAccessor<Boolean> SERVERSIDE_PRESENT;

    // Bloque estático para inicializar el accessor.
    static {
        SERVERSIDE_PRESENT = SynchedEntityData.<Boolean>defineId(Player.class, EntityDataSerializers.BOOLEAN);
    }

    // Constructor vacío, no es necesario pero se puede mantener si se planea expandir la clase en el futuro.
    public Reference() {
    }
}