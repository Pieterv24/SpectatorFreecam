package dev.pieterv24.freecam.mixin;

import dev.pieterv24.freecam.data.FreeCamEntity;
import dev.pieterv24.freecam.data.FreecamPlayerState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(PlayerEntity.class)
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

    @Inject(method = "writeCustomData", at=@At(value = "TAIL"))
    private void writeCustomData(WriteView view, CallbackInfo ci) {
        if (freecamPlayerState != null) {
            view.put(KEY, FreecamPlayerState.CODEC, freecamPlayerState);
        } else if (view.get(KEY) != null) {
            view.remove(KEY);
        }
    }

    @Inject(method = "readCustomData", at=@At(value = "TAIL"))
    private void readCustomData(ReadView view, CallbackInfo ci) {
        this.freecamPlayerState = view.read(KEY, FreecamPlayerState.CODEC).orElse(null);
    }
}
