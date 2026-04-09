package dev.pieterv24.freecam.mixin;

import dev.pieterv24.freecam.data.FreeCamEntity;
import dev.pieterv24.freecam.data.FreecamPlayerState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(Player.class)
public abstract class PlayerEntityMixin implements FreeCamEntity {
    private static String KEY = "freecam_player_state";

    private FreecamPlayerState freecamPlayerState;

    @Override
    public FreecamPlayerState getFreecamPlayerState() {
        return this.freecamPlayerState;
    }

    @Override
    public void setFreecamPlayerState(FreecamPlayerState freecamPlayerState) {
        this.freecamPlayerState = freecamPlayerState;
    }

    @Inject(method = "addAdditionalSaveData", at=@At(value = "TAIL"))
    private void writeCustomData(ValueOutput view, CallbackInfo ci) {
        if (freecamPlayerState != null) {
            view.store(KEY, FreecamPlayerState.CODEC, freecamPlayerState);
        } else if (view.child(KEY) != null) {
            view.discard(KEY);
        }
    }

    @Inject(method = "readAdditionalSaveData", at=@At(value = "TAIL"))
    private void readCustomData(ValueInput view, CallbackInfo ci) {
        this.freecamPlayerState = view.read(KEY, FreecamPlayerState.CODEC).orElse(null);
    }
}
