package com.example.tictac.datasource.repository;

import com.example.tictac.datasource.model.GameEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameStorage {
    private final Map<UUID, GameEntity> storage =  new ConcurrentHashMap<>();

    public GameEntity save(GameEntity game){
        if (game == null || game.getId() == null) {
            throw new IllegalArgumentException("Game and ID cannot be null");
        }
        storage.put(game.getId(), game);
        return game;
    }

    public Optional<GameEntity> findById(UUID id) {
        return id == null ? Optional.empty() : Optional.ofNullable(storage.get(id));
    }

    public boolean exists(UUID id) {
        return id == null ? false : storage.containsKey(id);
    }

    public void delete(UUID id) {
        if (id != null) storage.remove(id);
    }

    public int size() {
        return  storage.size();
    }

    public Map<UUID, GameEntity> findAll() {
       return new HashMap<>(storage);
    }

    public void clear() {
        storage.clear();
    }
}
