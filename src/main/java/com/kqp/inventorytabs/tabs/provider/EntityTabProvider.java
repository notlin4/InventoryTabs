package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.init.InventoryTabs;
import com.kqp.inventorytabs.tabs.tab.Tab;
import com.kqp.inventorytabs.util.BlockUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Stream;

public abstract class EntityTabProvider implements TabProvider {
    public static final int SEARCH_DISTANCE = 5;
    @Override
    public void addAvailableTabs(ClientPlayerEntity player, List<Tab> tabs) {
        World world = player.world;
        //List<StorageMinecartEntity> list = player.world.getEntitiesByClass(StorageMinecartEntity.class, player.getBoundingBox().expand(16.0), EntityPredicates.VALID_INVENTORIES);
        List<Entity> entityList = world.getNonSpectatingEntities(Entity.class, new Box(player.getBlockPos().getX()-SEARCH_DISTANCE, player.getBlockPos().getY()-SEARCH_DISTANCE, player.getBlockPos().getZ()-SEARCH_DISTANCE, player.getBlockPos().getX()+SEARCH_DISTANCE, player.getBlockPos().getY()+SEARCH_DISTANCE, player.getBlockPos().getZ()+SEARCH_DISTANCE));

        for (Entity entity : entityList) {
            if (!(entity instanceof PlayerEntity) && ((entity instanceof Inventory) || (entity instanceof InventoryOwner) || (entity instanceof InventoryChangedListener))) {
                //System.out.println(entity.getClass().getName());
                if (matches(entity)) {
                    boolean add = false;

                    Vec3d playerHead = player.getPos().add(0D, player.getEyeHeight(player.getPose()), 0D);
                    Vec3d blockVec = new Vec3d(entity.getX() + 0.5D, entity.getY() + 0.5D,
                            entity.getZ() + 0.5D);

                    if (blockVec.subtract(playerHead).lengthSquared() <= SEARCH_DISTANCE * SEARCH_DISTANCE) {
                        add = true;
                    }


                    if (add) {
                        Tab tab = createTab(entity);

                        if (!tabs.contains(tab)) {
                            tabs.add(tab);
                        }
                    }
                }
                //Tab tab = createTab(world, entity, entity.getPos());
                //if (!tabs.contains(tab)) {
                //    tabs.add(tab);
                //}
            }
        }
        /*
        if (!entityList.isEmpty()) {
            for (int x = -SEARCH_DISTANCE; x <= SEARCH_DISTANCE; x++) {
                for (int y = -SEARCH_DISTANCE; y <= SEARCH_DISTANCE; y++) {
                    for (int z = -SEARCH_DISTANCE; z <= SEARCH_DISTANCE; z++) {
                        BlockPos blockPos = player.getBlockPos().add(x, y, z);

                        if (matches(world, blockPos)) {
                            boolean add = false;

                            if (InventoryTabs.getConfig().doSightChecksFlag) {
                                BlockHitResult hitResult = BlockUtil.getLineOfSight(blockPos, player, 5D);

                                if (hitResult != null) {
                                    add = true;
                                }
                            } else {
                                Vec3d playerHead = player.getPos().add(0D, player.getEyeHeight(player.getPose()), 0D);
                                Vec3d blockVec = new Vec3d(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D,
                                        blockPos.getZ() + 0.5D);

                                if (blockVec.subtract(playerHead).lengthSquared() <= SEARCH_DISTANCE * SEARCH_DISTANCE) {
                                    add = true;
                                }
                            }

                            if (add) {
                                Tab tab = createTab(world, blockPos);

                                if (!tabs.contains(tab)) {
                                    tabs.add(tab);
                                }
                            }
                        }
                    }
                }
            }
        }*/
    }
    /**
     * Checks to see if block at passsed block position matches criteria.
     *
     * @param entity
     * @return
     */
    public abstract boolean matches(Entity entity);

    /**
     * Method to create tabs.
     *
     * @param entity
     * @return
     */
    public abstract Tab createTab(Entity entity);
}
