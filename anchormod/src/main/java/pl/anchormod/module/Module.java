package pl.anchormod.module;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private final KeyBinding key;
    private boolean enabled;

    protected Module(String id, String name, int defaultKey) {
        this.name = name;
        this.key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.anchormod." + id, InputUtil.Type.KEYSYM, defaultKey, "category.anchormod"));
    }

    public String getName() { return name; }
    public KeyBinding getKey() { return key; }
    public boolean isEnabled() { return enabled; }

    /** Wywolywane po wcisnieciu klawisza. Domyslnie przelacza modul. */
    public void onKeyPressed() { toggle(); }

    public void toggle() { setEnabled(!enabled); }

    public void setEnabled(boolean value) {
        if (enabled == value) return;
        enabled = value;
        if (value) onEnable(); else onDisable();
    }

    protected void onEnable() {}
    protected void onDisable() {}

    /** Wywolywane co tick (zawsze, tez gdy modul wylaczony - sprawdzaj isEnabled sam jesli trzeba). */
    public void onTick() {}
}
