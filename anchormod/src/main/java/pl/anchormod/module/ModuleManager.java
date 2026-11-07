package pl.anchormod.module;

import net.minecraft.client.MinecraftClient;
import pl.anchormod.module.impl.AutoAnchor;
import pl.anchormod.module.impl.AutoSprint;
import pl.anchormod.module.impl.Fly;
import pl.anchormod.module.impl.WaterClutch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void registerAll() {
        modules.add(new WaterClutch());
        modules.add(new AutoAnchor());
        modules.add(new AutoSprint());
        modules.add(new Fly());
    }

    public List<Module> getModules() { return Collections.unmodifiableList(modules); }

    public void handleKeys() {
        for (Module m : modules) {
            while (m.getKey().wasPressed()) m.onKeyPressed();
        }
    }

    public void tick(MinecraftClient client) {
        for (Module m : modules) m.onTick();
    }
}
