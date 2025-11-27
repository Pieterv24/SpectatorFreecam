package dev.pieterv24.freecam.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

public record FreecamPlayerState(Vec3d position, Vec2f rotation, GameMode gameMode, RegistryKey<World> world) {
    public static final Codec<FreecamPlayerState> CODEC = RecordCodecBuilder.create(i -> i.group(
            Vec3d.CODEC.fieldOf("position").forGetter(FreecamPlayerState::position),
            Vec2f.CODEC.fieldOf("rotation").forGetter(FreecamPlayerState::rotation),
            GameMode.CODEC.fieldOf("gameMode").forGetter(FreecamPlayerState::gameMode),
            ServerWorld.CODEC.fieldOf("world").forGetter(FreecamPlayerState::world)
        ).apply(i, FreecamPlayerState::new)
    );
}