package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.ChestTab;
import com.kqp.inventorytabs.tabs.tab.SimpleBlockTab;
import com.kqp.inventorytabs.tabs.tab.SimpleEntityTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import com.kqp.inventorytabs.util.ChestUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleEntityTabProvider extends EntityTabProvider {
    private final Set<Identifier> entities = new HashSet<>();

    public SimpleEntityTabProvider() {
    }

    @Override
    public void addAvailableTabs(ClientPlayerEntity player, List<Tab> tabs) {
        super.addAvailableTabs(player, tabs);
        Set<SimpleEntityTab> tabsToRemove = new HashSet<>();
        List<SimpleEntityTab> entityTabs = tabs.stream().filter(tab -> tab instanceof SimpleEntityTab).map(tab -> (SimpleEntityTab) tab)
                .filter(tab -> entities.contains(tab.entityId)).toList();
        World world = player.world;
    }

    @Override
    public boolean matches(Entity entity) {


        //for (Identifier entityId : entities) {
        //    System.out.println("SimpleEntityTabProvider.matches: "+ entities.contains(Registry.ENTITY_TYPE.get(entity.getId()))+", "+entity.getType()+", "+Registry.ENTITY_TYPE.get(entity.getId()));
            //if (Registry.ENTITY_TYPE.getId(entity.getType()).equals(entityId)) {
            //    return true;
            //}
        //}

        return entities.contains(new Identifier("minecraft:entity.minecraft.chest_minecart"));
        //return entities.contains(Registry.ENTITY_TYPE.getId(entity.getType()));
        //return entityPos.distanceTo(MinecraftClient.getInstance().player.getPos()) < 5;
    }

    public void addEntity(Identifier entityId) {
        entities.add(entityId);
    }

    @Override
    public Tab createTab(Entity entity) {
        return new SimpleEntityTab(entity);
    }
}
