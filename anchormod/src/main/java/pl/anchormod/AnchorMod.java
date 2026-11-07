package pl.anchormod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import pl.anchormod.module.Module;
import pl.anchormod.module.ModuleManager;

public class AnchorMod implements ClientModInitializer {
    public static final ModuleManager MODULES = new ModuleManager();

    @Override
    public void onInitializeClient() {
        MODULES.registerAll();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;
            MODULES.handleKeys();
            MODULES.tick(client);
        });

        // Lista aktywnych modulow w prawym gornym rogu
        HudRenderCallback.EVENT.register((ctx, tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.options.hudHidden) return;
            int y = 4;
            for (Module m : MODULES.getModules()) {
                if (!m.isEnabled()) continue;
                int w = mc.textRenderer.getWidth(m.getName());
                ctx.drawTextWithShadow(mc.textRenderer, m.getName(),
                        ctx.getScaledWindowWidth() - w - 4, y, 0x55FF55);
                y += 10;
            }
        });
    }
}
