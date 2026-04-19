package com.example.tictac.datasource.repository;

import com.example.tictac.datasource.mapper.GameMapper;
import com.example.tictac.datasource.model.GameEntity;
import com.example.tictac.domain.model.Game;

import java.util.*;
import java.util.stream.Collectors;

public class GameRepository {
    private final GameStorage storage;

    public GameRepository(GameStorage storage) {
        this.storage = storage;
    }

    public Game save(Game game) {
        if(game  == null) {
            throw new IllegalArgumentException("Not game");
        }
        GameEntity entity = GameMapper.toEntity(game);
        GameEntity saveEntity = storage.save(entity);
        return GameMapper.toDomain(saveEntity);
    }

    public Optional<Game> findById(UUID id) {
        if(id == null) {
            return Optional.empty();
        }
        return storage.findById(id)
                .map(GameMapper::toDomain);
    }

    public boolean exists(UUID id) {
        return storage.exists(id);
    }

    public void delete(UUID id) {
        if(id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        storage.delete(id);
    }

    public List<Game> findAll() {
        Map<UUID, GameEntity> entityMap = storage.findAll();
        Collection<GameEntity> entities = entityMap.values();

        return entities.stream()
                .map(GameMapper::toDomain)
                .collect(Collectors.toList());
    }
}
