package pl.anchormod.module.impl;

import net.minecraft.client.network.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;
import pl.anchormod.module.Module;

/** Prosty fly oparty o predkosc. Dziala w singleplayerze / na wlasnym serwerze z wylaczonym anty-cheatem. */
public class Fly extends Module {
    private static final double SPEED = 0.6;
    private static final double VERTICAL = 0.4;

    public Fly() { super("fly", "Fly", GLFW.GLFW_KEY_H); }

    @Override
    public void onTick() {
        if (!isEnabled()) return;
        ClientPlayerEntity p = mc.player;
        if (p == null) return;

        float f = p.input.movementForward;
        float s = p.input.movementSideways;
        double yaw = Math.toRadians(p.getYaw());
        double x = (s * Math.cos(yaw) - f * Math.sin(yaw)) * SPEED;
        double z = (f * Math.cos(yaw) + s * Math.sin(yaw)) * SPEED;

        double y = 0;
        if (mc.options.jumpKey.isPressed()) y = VERTICAL;
        else if (mc.options.sneakKey.isPressed()) y = -VERTICAL;

        p.setVelocity(x, y, z);
        p.fallDistance = 0;
    }
}
