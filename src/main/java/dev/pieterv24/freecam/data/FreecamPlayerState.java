package dev.pieterv24.freecam.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public record FreecamPlayerState(Vec3 position, Vec2 rotation, GameType gameMode, ResourceKey<Level> world) {
    public static final Codec<FreecamPlayerState> CODEC = RecordCodecBuilder.create(i -> i.group(
            Vec3.CODEC.fieldOf("position").forGetter(FreecamPlayerState::position),
            Vec2.CODEC.fieldOf("rotation").forGetter(FreecamPlayerState::rotation),
            GameType.CODEC.fieldOf("gameMode").forGetter(FreecamPlayerState::gameMode),
            ServerLevel.RESOURCE_KEY_CODEC.fieldOf("world").forGetter(FreecamPlayerState::world)
        ).apply(i, FreecamPlayerState::new)
    );
}