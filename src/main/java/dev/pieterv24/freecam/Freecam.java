package dev.pieterv24.freecam;

import dev.pieterv24.freecam.data.FreeCamEntity;
import dev.pieterv24.freecam.data.FreecamPlayerState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import java.util.Collections;

public class Freecam implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            commandDispatcher.register(
                Commands.literal("freecam")
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        final FreecamPlayerState playerState = ((FreeCamEntity) player).getFreecamPlayerState();

                        if (playerState != null) {
                            returnPlayer(player, context.getSource().getServer());
                        } else {
                            FreecamPlayerState newPlayerState = new FreecamPlayerState(player.position(), player.getRotationVector(), player.gameMode.getGameModeForPlayer(), player.level().dimension());
                            ((FreeCamEntity) player).setFreecamPlayerState(newPlayerState);
                            player.setGameMode(GameType.SPECTATOR);
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

    private void returnPlayer(final ServerPlayer player, final MinecraftServer server) {
        final FreecamPlayerState state = ((FreeCamEntity) player).getFreecamPlayerState();
        final ServerLevel world = server.getLevel(state.world());

        player.teleportTo(world, state.position().x, state.position().y, state.position().z, Collections.emptySet(), state.rotation().y, state.rotation().x, false);
        player.setGameMode(state.gameMode());

        ((FreeCamEntity) player).setFreecamPlayerState(null);
    }
}
