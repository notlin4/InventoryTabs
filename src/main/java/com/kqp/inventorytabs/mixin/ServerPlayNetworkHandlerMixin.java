package com.kqp.inventorytabs.mixin;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Inject( method = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"))
    public void sendPacket(Packet<?> packet, CallbackInfo ci) {

        // Check for inventory movement packet
        if(packet instanceof ClickSlotC2SPacket) {
            ClickSlotC2SPacket clickSlotPacket = (ClickSlotC2SPacket) packet;
            //System.out.println("TESTING: Inventory movement packet"+packet);
            System.out.println("syncId: " + clickSlotPacket.getSyncId()+", slot: " + clickSlotPacket.getSlot()+", button: " + clickSlotPacket.getButton()+", stack: " + clickSlotPacket.getStack()+", action: " + clickSlotPacket.getActionType()+", ModifiedStacks: " + clickSlotPacket.getModifiedStacks());
        }
    }
}
