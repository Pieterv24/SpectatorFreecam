package dev.pieterv24.freecam.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;


public class FreecamClient implements ClientModInitializer {
    private static final Category CATEGORY = Category.create(Identifier.of("freecam", "category_name"));
    private static KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.freecam.toggle_freecam",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F7,
            CATEGORY
    ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && keyBinding.wasPressed()) {
                client.player.networkHandler.sendChatCommand("freecam");
            }
        });
    }
}
