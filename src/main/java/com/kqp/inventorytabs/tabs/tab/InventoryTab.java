package com.kqp.inventorytabs.tabs.tab;

import com.google.common.collect.ImmutableList;
import com.kqp.inventorytabs.mixin.accessor.ClientPlayerInteractionManagerAccessor;
import com.kqp.inventorytabs.tabs.TabManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

public class InventoryTab extends Tab {
    public final ItemStack itemStack;
    public final Item item;

    public int slotId;
    public InventoryTab(ItemStack itemStack, int slotId) {
        super(itemStack);
        this.itemStack = itemStack;
        this.slotId = slotId;
        this.item = itemStack.getItem();
    }
    @Override
    public void preOpen() {
        System.out.println("PRE_OPEN");
        MinecraftClient client = MinecraftClient.getInstance();
        client.setScreen(new InventoryScreen(client.player));
    }

    @Override
    public void open() {
        System.out.println("TESTING: Opening inventory tab");
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        ClientPlayerInteractionManager clientPlayerInteractionManager = MinecraftClient.getInstance().interactionManager;
        ScreenHandler currentScreenHandler = player.currentScreenHandler;
        int syncId = currentScreenHandler.syncId;
        PlayerInventory inventory = player.getInventory();
        int selectedSlot = inventory.selectedSlot;
        int slot_id = inventory.getSlotWithStack(itemStack);



        selectedSlot = currentScreenHandler.slots.size() - 10 + selectedSlot;
        if (PlayerInventory.isValidHotbarIndex(slot_id)) {
            slot_id = currentScreenHandler.slots.size() - 10 + slot_id;
            slotId = slot_id;
        }

        System.out.println("selectedSlot: " + selectedSlot+" tabSlot: " + slot_id+" combinedInventory.size(): " + (currentScreenHandler.slots.size()- 10));
        if (clientPlayerInteractionManager != null) {
            if (slot_id != selectedSlot) {
                clientPlayerInteractionManager.clickSlot(syncId, slot_id, 0, SlotActionType.PICKUP, player);
                clientPlayerInteractionManager.clickSlot(syncId, selectedSlot, 0, SlotActionType.PICKUP, player);
                clientPlayerInteractionManager.clickSlot(syncId, slot_id, 0, SlotActionType.PICKUP, player);
            }
            MinecraftClient.getInstance().interactionManager.interactItem(player, player.getWorld(), player.getActiveHand());
        }
    }
    @Override
    public void onClose() {
        System.out.println("ON_CLOSE");
        //MinecraftClient client = MinecraftClient.getInstance();
        //client.setScreen(new InventoryScreen(client.player));
        //TabManager.getInstance().currentTab = null;
        //if (client.player.currentScreenHandler != null) {
        //    client.getNetworkHandler()
        //            .sendPacket(new CloseHandledScreenC2SPacket(client.player.currentScreenHandler.syncId));
        //}
    }
    @Override
    public void postClose() {
        System.out.println("POST_CLOSE");
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        ClientPlayerInteractionManager clientPlayerInteractionManager = MinecraftClient.getInstance().interactionManager;
        ScreenHandler currentScreenHandler = player.currentScreenHandler;
        int syncId = currentScreenHandler.syncId;
        int selectedSlot = currentScreenHandler.slots.size() - 10 + player.getInventory().selectedSlot;

        System.out.println("selectedSlot: " + selectedSlot+" tabSlot: " + slotId+" combinedInventory.size(): " + currentScreenHandler+", "+(currentScreenHandler.slots.size()- 10));
        if (clientPlayerInteractionManager != null) {
            if (slotId != selectedSlot) {
                clientPlayerInteractionManager.clickSlot(syncId, slotId, 0, SlotActionType.PICKUP, player);
                clientPlayerInteractionManager.clickSlot(syncId, selectedSlot, 0, SlotActionType.PICKUP, player);
                clientPlayerInteractionManager.clickSlot(syncId, slotId, 0, SlotActionType.PICKUP, player);
            }
        }
    }

    @Override
    public boolean shouldBeRemoved() {

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        //PlayerInventory inventory = player.getInventory();
        //int selectedSlot = inventory.selectedSlot;
        //System.out.println("TESTING: Selected slot: " + selectedSlot+"Item:"+item+", Slot ID: " + slotId);
        //return (player == null || !(player.currentScreenHandler instanceof PlayerScreenHandler) && player.currentScreenHandler || !player.getInventory().contains(itemStack));
        return (player == null || !player.getInventory().contains(itemStack));
    }

    @Override
    public Text getHoverText() {
        return item.getName();
    }
}
