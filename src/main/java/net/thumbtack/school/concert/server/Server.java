package net.thumbtack.school.concert.server;

import com.google.gson.Gson;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.service.*;
import java.io.*;

public class Server {
    private final SongService songService;
    private final UserService userService;
    private final Gson gson = new Gson();

    public Server() {
        this.songService = new SongService();
        this.userService = new UserService();
    }

    public void startServer(String savedDataFileName) {
        if (savedDataFileName != null) {
            try (BufferedReader in = new BufferedReader(new FileReader(new File(savedDataFileName)))) {
                Database.setInstance(gson.fromJson(in, Database.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer(String savedDataFileName) {
        Database.getInstance().getTokenToUser().clear();
        try (BufferedWriter out = new BufferedWriter(new FileWriter(savedDataFileName))) {
            gson.toJson(Database.getInstance(), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String registerUser(String requestJsonString) {
        return userService.registerUser(requestJsonString);
    }

    public String login(String requestJsonString) {
        return userService.login(requestJsonString);
    }

    public String logout(String requestJsonString) {
        return userService.logout(requestJsonString);
    }

    public String quit(String requestJsonString) {
        return userService.quit(requestJsonString);
    }

    public String addSongs(String requestJsonString) {
        return songService.addSongs(requestJsonString);
    }

    public String removeSongs(String requestJsonString) {
        return songService.removeSongs(requestJsonString);
    }

    public String getSongs(String requestJsonString) {
        return songService.getSongs(requestJsonString);
    }

    public String getSongsByComposers(String requestJsonString) {
        return songService.getSongsByComposers(requestJsonString);
    }

    public String getSongsByAuthors(String requestJsonString) {
        return songService.getSongsByAuthors(requestJsonString);
    }

    public String getSongsByPerformer(String requestJsonString) {
        return songService.getSongsByPerformer(requestJsonString);
    }

    public String addRating(String requestJsonString) {
        return songService.addRating(requestJsonString);
    }

    public String removeRating(String requestJsonString) {
        return songService.removeRating(requestJsonString);
    }

    public String editRating(String requestJsonString) {
        return songService.editRating(requestJsonString);
    }

    public String addComment(String requestJsonString) {
        return songService.addComment(requestJsonString);
    }

    public String removeComment(String requestJsonString) {
        return songService.removeComment(requestJsonString);
    }

    public String editComment(String requestJsonString) {
        return songService.editComment(requestJsonString);
    }

    public String likeComment(String requestJsonString) {
        return songService.likeComment(requestJsonString);
    }

    public String removeLikeComment(String requestJsonString) {
        return songService.removeLikeComment(requestJsonString);
    }

    public String getConcert(String requestJsonString) {
        return songService.getConcert(requestJsonString);
    }
}
