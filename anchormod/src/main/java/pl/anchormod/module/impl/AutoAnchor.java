package pl.anchormod.module.impl;

import net.minecraft.block.Block;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;
import pl.anchormod.module.Module;

/**
 * Patrzysz na respawn anchor i wciskasz klawisz: modul doladowuje go
 * glowstone'em z hotbara do pelna (4 ladunki), a potem wraca na
 * poprzedni trzymany przedmiot.
 */
public class AutoAnchor extends Module {
    public AutoAnchor() { super("autoanchor", "AutoAnchor", GLFW.GLFW_KEY_B); }

    @Override
    public void onKeyPressed() {
        ClientPlayerEntity p = mc.player;
        if (p == null || mc.interactionManager == null || mc.world == null) return;
        if (!(mc.crosshairTarget instanceof BlockHitResult hit)) return;
        if (hit.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = hit.getBlockPos();
        Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof RespawnAnchorBlock)) return;

        int charge = mc.world.getBlockState(pos).get(RespawnAnchorBlock.CHARGES);
        int missing = 4 - charge;
        if (missing <= 0) return;

        int glowstoneSlot = findHotbar(p, Items.GLOWSTONE);
        if (glowstoneSlot == -1) return;

        int previous = p.getInventory().selectedSlot;
        p.getInventory().selectedSlot = glowstoneSlot;

        for (int i = 0; i < missing; i++) {
            // sprawdzamy, czy nadal mamy glowstone i czy anchor nadal nie jest pelny
            if (p.getInventory().getStack(glowstoneSlot).isEmpty()) break;
            int currentCharge = mc.world.getBlockState(pos).get(RespawnAnchorBlock.CHARGES);
            if (currentCharge >= 4) break;

            var result = mc.interactionManager.interactBlock(p, Hand.MAIN_HAND, hit);
            if (result.isAccepted()) p.swingHand(Hand.MAIN_HAND);
        }

        p.getInventory().selectedSlot = previous;
    }

    private int findHotbar(ClientPlayerEntity p, net.minecraft.item.Item item) {
        for (int i = 0; i < 9; i++) {
            if (p.getInventory().getStack(i).isOf(item)) return i;
        }
        return -1;
    }
}
