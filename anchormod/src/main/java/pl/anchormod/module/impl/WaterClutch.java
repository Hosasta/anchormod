package pl.anchormod.module.impl;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
import pl.anchormod.module.Module;

/**
 * Trzymasz dowolny blok (cobweb, obsydian...), patrzysz w dol i wciskasz klawisz:
 * modul w jednym ticku przelacza na wiadro z woda, stawia wode i wraca na poprzedni slot.
 * Opcjonalnie zbiera wode z powrotem po wyladowaniu.
 */
public class WaterClutch extends Module {
    private static final boolean PICKUP_WATER = true;
    private static final int PICKUP_TIMEOUT_TICKS = 100;

    private int pickupSlot = -1;
    private int pickupTicks = 0;

    public WaterClutch() { super("waterclutch", "WaterClutch", GLFW.GLFW_KEY_V); }

    /** To nie jest przelacznik - kazde wcisniecie wykonuje clutch. */
    @Override
    public void onKeyPressed() {
        ClientPlayerEntity p = mc.player;
        if (p == null || mc.interactionManager == null) return;

        int slot = findHotbar(p, Items.WATER_BUCKET);
        if (slot == -1) return;

        if (useSlot(p, slot)) {
            if (PICKUP_WATER) {
                pickupSlot = slot;   // wiadro jest teraz puste w tym samym slocie
                pickupTicks = 0;
            }
        }
    }

    @Override
    public void onTick() {
        if (pickupSlot == -1) return;
        ClientPlayerEntity p = mc.player;
        if (p == null) { pickupSlot = -1; return; }

        if (++pickupTicks > PICKUP_TIMEOUT_TICKS) { pickupSlot = -1; return; }

        // zbieramy wode dopiero po wyladowaniu (patrz w dol, zeby trafic w zrodlo)
        if (p.isOnGround() || p.isTouchingWater()) {
            if (p.getInventory().getStack(pickupSlot).isOf(Items.BUCKET)) {
                useSlot(p, pickupSlot);
            }
            pickupSlot = -1;
        }
    }

    /** Przelacza na slot, uzywa przedmiotu, wraca na poprzedni slot. */
    private boolean useSlot(ClientPlayerEntity p, int slot) {
        int previous = p.getInventory().selectedSlot;
        p.getInventory().selectedSlot = slot;
        // interactItem sam synchronizuje wybrany slot z serwerem przed uzyciem
        var result = mc.interactionManager.interactItem(p, Hand.MAIN_HAND);
        if (result.isAccepted()) p.swingHand(Hand.MAIN_HAND);
        p.getInventory().selectedSlot = previous;
        return result.isAccepted();
    }

    private int findHotbar(ClientPlayerEntity p, net.minecraft.item.Item item) {
        for (int i = 0; i < 9; i++) {
            if (p.getInventory().getStack(i).isOf(item)) return i;
        }
        return -1;
    }
}
