package roguelike.data;

import roguelike.domain.model.GameData;
import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GameRepository {
    private static final String SAVE_DIR = "saves/";
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public void save(String filename, GameData data) {
        try {
            Files.createDirectories(Paths.get(SAVE_DIR));
            String json = gson.toJson(data);
            Files.write(Paths.get(SAVE_DIR + filename), json.getBytes());
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    public GameData load(String filename) {
        try {
            String json = Files.readString(Paths.get(SAVE_DIR + filename));
            System.out.println("Game loaded from: " + filename);
            return gson.fromJson(json, GameData.class);
        } catch (IOException e) {
            System.err.println("Load failed: " + e.getMessage() + ", file: " + filename);
            return null;
        }
    }

    public void saveStatistics(String filename, List<GameData.GameStatistics> stats) {
        try {
            Files.createDirectories(Paths.get(SAVE_DIR));
            String json = gson.toJson(stats);
            Files.write(Paths.get(SAVE_DIR + filename), json.getBytes());
        } catch (IOException e) {
            System.err.println("Stats save failed: " + e.getMessage());
        }
    }

    public List<GameData.GameStatistics> loadStatistics(String filename) {
        try {
            String json = Files.readString(Paths.get(SAVE_DIR + filename));
            GameData.GameStatistics[] array = gson.fromJson(json, GameData.GameStatistics[].class);
            return new ArrayList<>(Arrays.asList(array));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}