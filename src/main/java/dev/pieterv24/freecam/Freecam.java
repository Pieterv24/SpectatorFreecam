package dev.pieterv24.freecam;

import dev.pieterv24.freecam.data.FreeCamEntity;
import dev.pieterv24.freecam.data.FreecamPlayerState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;

import java.util.Collections;

public class Freecam implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            commandDispatcher.register(
                CommandManager.literal("freecam")
                    .executes(context -> {
                        final ServerPlayerEntity player = context.getSource().getPlayer();
                        final FreecamPlayerState playerState = ((FreeCamEntity) player).getFreecamPlayerState();

                        if (playerState != null) {
                            returnPlayer(player, context.getSource().getServer());
                        } else {
                            FreecamPlayerState newPlayerState = new FreecamPlayerState(player.getEntityPos(), player.getRotationClient(), player.interactionManager.getGameMode(), player.getEntityWorld().getRegistryKey());
                            ((FreeCamEntity) player).setFreecamPlayerState(newPlayerState);
                            player.changeGameMode(GameMode.SPECTATOR);
                        }

                        return 1;
                    })
            );
        });

//        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
//            final ServerPlayerEntity player = handler.getPlayer();
//            final FreecamPlayerState playerState = ((FreeCamEntity) player).getFreecamPlayerState();
//
//            if (playerState != null) {
//                returnPlayer(player, server);
//            }
//        });
    }

    private void returnPlayer(final ServerPlayerEntity player, final MinecraftServer server) {
        final FreecamPlayerState state = ((FreeCamEntity) player).getFreecamPlayerState();
        final ServerWorld world = server.getWorld(state.world());

        player.teleport(world, state.position().x, state.position().y, state.position().z, Collections.emptySet(), state.rotation().y, state.rotation().x, false);
        player.changeGameMode(state.gameMode());

        ((FreeCamEntity) player).setFreecamPlayerState(null);
    }
}
