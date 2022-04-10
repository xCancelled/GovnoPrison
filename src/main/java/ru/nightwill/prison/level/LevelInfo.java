package ru.nightwill.prison.level;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

@Getter
@AllArgsConstructor
public class LevelInfo {
    public int level;
    public double price;
    public ConcurrentHashMap<String, Integer> blocks;
}
