package com.kqp.inventorytabs.tabs.tab;

import com.kqp.inventorytabs.mixin.accessor.ScreenAccessor;
import com.kqp.inventorytabs.tabs.provider.BlockTabProvider;
import com.kqp.inventorytabs.tabs.render.TabRenderInfo;
import com.kqp.inventorytabs.util.ChestUtil;
import net.minecraft.block.BarrierBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.kqp.inventorytabs.util.ChestUtil.getOtherChestBlockPos;

public class SimpleEntityTab extends Tab {
    public final Vec3d entityPos;
    public final Identifier entityId;
    public final Entity entity;

    //ItemStack itemStack;

    public SimpleEntityTab(Entity entity) {
        super(new ItemStack(Registry.ITEM.get(new Identifier("minecraft", "barrier"))));
        this.entity = entity;
        this.entityPos = entity.getPos();
        this.entityId = EntityType.getId(entity.getType());
    }

    @Override
    public void open() {
        //EntityHitResult entityHitResult = new EntityHitResult(entity, entity.getBoundingBox().getCenter());
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        MinecraftClient.getInstance().interactionManager.interactEntity(player, entity, player.getActiveHand());
        //System.out.println("Opening entity tab");

        //System.out.println(entity.interact(player, player.getActiveHand()));
        //entity.interact(player, player.getActiveHand());
    }

    @Override
    public boolean shouldBeRemoved() {
        //System.out.println("test: "+(entity));
        //
        //
        if (entity.isRemoved()) {
            return true;
        }
        return entityPos.distanceTo(MinecraftClient.getInstance().player.getPos()) > 5;
        //Vec3d playerHead = player.getPos().add(0D, player.getEyeHeight(player.getPose()), 0D);
        //return Vec3d.ofCenter(entityPos).subtract(playerHead).lengthSquared() > BlockTabProvider.SEARCH_DISTANCE
        //        * BlockTabProvider.SEARCH_DISTANCE;
    }

    @Override
    public Text getHoverText() {
        return entity.getName();
    }

    @Override
    public void renderTabIcon(MatrixStack matrices, TabRenderInfo tabRenderInfo, HandledScreen<?> currentScreen) {
        ItemStack itemStack = getItemStack();
        ItemRenderer itemRenderer = ((ScreenAccessor) currentScreen).getItemRenderer();
        TextRenderer textRenderer = ((ScreenAccessor) currentScreen).getTextRenderer();
        itemRenderer.zOffset = 100.0F;
        itemRenderer.renderInGuiWithOverrides(itemStack, tabRenderInfo.itemX, tabRenderInfo.itemY);
        itemRenderer.renderGuiItemOverlay(textRenderer, itemStack, tabRenderInfo.itemX, tabRenderInfo.itemY);
        itemRenderer.zOffset = 0.0F;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleEntityTab tab = (SimpleEntityTab) o;
        return Objects.equals(entityId, tab.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId);
    }

    public ItemStack getItemStack() {
        return entity.getPickBlockStack() != null ? entity.getPickBlockStack() : new ItemStack(Registry.ITEM.get(new Identifier("minecraft", "barrier")));
    }
}
