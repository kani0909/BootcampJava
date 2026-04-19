package com.example.tictac.di;

import com.example.tictac.datasource.repository.GameRepository;
import com.example.tictac.datasource.repository.GameStorage;
import com.example.tictac.domain.service.GameServiceImpl;
import com.example.tictac.domain.service.GameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public GameStorage gameStorage() {
        return new GameStorage();
    }

    @Bean
    public GameRepository gameRepository(GameStorage gameStorage) {
        return new GameRepository(gameStorage);
    }

    @Bean
    public GameService gameService(GameRepository gameRepository) {
        return new GameServiceImpl(gameRepository);
    }
}
