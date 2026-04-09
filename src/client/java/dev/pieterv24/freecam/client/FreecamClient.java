package dev.pieterv24.freecam.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyMapping.Category;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

import static dev.pieterv24.freecam.Freecam.MOD_ID;


public class FreecamClient implements ClientModInitializer {
    private static final Category CATEGORY = Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "category_name"));
    private static KeyMapping keyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.freecam.toggle_freecam",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F7,
            CATEGORY
    ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && keyMapping.consumeClick()) {
                client.player.connection.sendCommand("freecam");
            }
        });
    }
}
