package pl.anchormod.module.impl;

import net.minecraft.client.network.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;
import pl.anchormod.module.Module;

public class AutoSprint extends Module {
    public AutoSprint() { super("autosprint", "AutoSprint", GLFW.GLFW_KEY_G); }

    @Override
    public void onTick() {
        if (!isEnabled()) return;
        ClientPlayerEntity p = mc.player;
        if (p == null) return;
        if (p.input.movementForward > 0 && !p.isSneaking() && !p.horizontalCollision
                && p.getHungerManager().getFoodLevel() > 6) {
            p.setSprinting(true);
        }
    }
}
