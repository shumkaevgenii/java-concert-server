package net.thumbtack.school.concert.server.server;

import com.google.gson.Gson;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {
    public String path = "database.txt";

    @BeforeEach
    public void init(){
        Database.getInstance().clear();
    }

    @Test
    public void startServer() {
        Server server = new Server();
        server.startServer(null);
        assertFalse(Database.getInstance().getUsers().containsKey("login"));
    }

    @Test
    public void stopServer() throws FileNotFoundException {
        Server server = new Server();
        server.startServer(null);

        // Registration new user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        server.registerUser(new Gson().toJson(user));

        // Clear database file
        PrintWriter writer = new PrintWriter(new File(path));
        writer.print("");
        writer.close();

        // Stop server and write data to database.txt
        server.stopServer(path);

        assertTrue(new File(path).exists());
    }

}
