package ru.nightwill.prison.npc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

@AllArgsConstructor
@Getter
public class NpcInfo{
    public String name;
    public String skin;
    public Location location;
    public EntityType entityType;
}