package net.thumbtack.school.concert.server.database;

import com.google.gson.Gson;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.request.comment.*;
import net.thumbtack.school.concert.dto.request.song.AddSongsDtoRequest;
import net.thumbtack.school.concert.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.concert.dto.response.Response;
import net.thumbtack.school.concert.dto.response.TypeOfResponse;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class CommentTest {
    private Server server;
    private final Gson gson = new Gson();

    private List<String> nameOfSong;
    private List<Set<String>> composers;
    private List<Set<String>> authors;
    private List<String> performer;
    private List<Integer> songDuration;

    @BeforeEach
    public void before() {
        Database.getInstance().clear();
        server = new Server();
        server.startServer(null);

        // Init song dto
        nameOfSong = Stream.of("song").collect(Collectors.toList());
        Set<String> cmps = Stream.of("composer").collect(Collectors.toSet());
        composers = new ArrayList<>();
        composers.add(cmps);
        Set<String> athrs = Stream.of("author").collect(Collectors.toSet());
        authors = new ArrayList<>();
        authors.add(athrs);
        performer = Stream.of("performer").collect(Collectors.toList());
        songDuration = Stream.of(250).collect(Collectors.toList());
    }

    @Test
    public void invalidRequestAddComment() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add comment with wrong token
        AddCommentDtoRequest addCommentDtoRequest = new AddCommentDtoRequest("song","comment", UUID.randomUUID().toString());
        Response response = gson.fromJson(server.addComment(gson.toJson(addCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_TOKEN, response.getType());

        // Add comment wrong song
        addCommentDtoRequest.setToken(token);
        addCommentDtoRequest.setNameSong("wrong");
        response = gson.fromJson(server.addComment(gson.toJson(addCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_SONG, response.getType());

        // Add comment
        addCommentDtoRequest.setNameSong("song");
        server.addComment(gson.toJson(addCommentDtoRequest));

        // Add comment again
        response = gson.fromJson(server.addComment(gson.toJson(addCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_ADD_COMMENT, response.getType());
    }

    @Test
    public void isAddComment() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add comment
        AddCommentDtoRequest addCommentDtoRequest = new AddCommentDtoRequest("song", "comment", token);
        Response response = gson.fromJson(server.addComment(gson.toJson(addCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertTrue(Database.getInstance().getSongs().get("song").getComments().containsKey(new User("name", "surname", "login", "passpass")));
    }

    @Test
    public void invalidRequestEditComment() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Edit comment with wrong token
        EditCommentDtoRequest editCommentDtoRequest = new EditCommentDtoRequest("song","comment", UUID.randomUUID().toString());
        Response response = gson.fromJson(server.editComment(gson.toJson(editCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_TOKEN, response.getType());

        // Edit comment wrong song
        editCommentDtoRequest.setToken(token);
        editCommentDtoRequest.setNameSong("wrong");
        response = gson.fromJson(server.editComment(gson.toJson(editCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_SONG, response.getType());

        // Edit wrong comment
        editCommentDtoRequest.setComment("wrong");
        editCommentDtoRequest.setNameSong("song");
        response = gson.fromJson(server.editComment(gson.toJson(editCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_COMMENT, response.getType());
    }

    @Test
    public void isEditComment() {
        // Register users
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"","");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add comment
        AddCommentDtoRequest addCommentDtoRequest = new AddCommentDtoRequest("song", "comment", token);
        server.addComment(gson.toJson(addCommentDtoRequest));

        // Edit comment
        EditCommentDtoRequest editCommentDtoRequest = new EditCommentDtoRequest("song", "newComment", token);
        Response response = gson.fromJson(server.editComment(gson.toJson(editCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());

        // Add like comment
        LikeCommentDtoRequest likeCommentDtoRequest = new LikeCommentDtoRequest("song", "login", token2);
        server.likeComment(gson.toJson(likeCommentDtoRequest));

        // Edit like comment
        editCommentDtoRequest.setComment("comment");
        server.editComment(gson.toJson(editCommentDtoRequest));

        assertFalse(Database.getInstance().getSongs().get("song").getCommentsOfCommunity().isEmpty());
        assertEquals("comment", Database.getInstance().getSongs().get("song").getComments().get(new User("name", "surname", "login", "passpass")).getText());
    }

    @Test
    public void invalidRequestRemoveComment() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"","");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Remove comment with wrong token
        RemoveCommentDtoRequest removeCommentDtoRequest = new RemoveCommentDtoRequest("song", UUID.randomUUID().toString());
        Response response = gson.fromJson(server.removeComment(gson.toJson(removeCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_TOKEN, response.getType());

        // Remove comment wrong song
        removeCommentDtoRequest.setToken(token);
        removeCommentDtoRequest.setNameSong("wrong");
        response = gson.fromJson(server.removeComment(gson.toJson(removeCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_SONG, response.getType());

        // Edit wrong comment
        removeCommentDtoRequest.setNameSong("song");
        removeCommentDtoRequest.setToken(token2);
        response = gson.fromJson(server.removeComment(gson.toJson(removeCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_COMMENT, response.getType());
    }

    @Test
    public void isRemoveComment() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"","");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add comment
        AddCommentDtoRequest addCommentDtoRequest = new AddCommentDtoRequest("song", "comment", token);
        server.addComment(gson.toJson(addCommentDtoRequest));

        // Remove comment
        RemoveCommentDtoRequest removeCommentDtoRequest = new RemoveCommentDtoRequest("song", token);
        Response response = gson.fromJson(server.removeComment(gson.toJson(removeCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertFalse(Database.getInstance().getSongs().get("song").getComments().containsKey(new User("name", "surname", "login", "passpass")));
        assertFalse(Database.getInstance().getUserToCommentSongs().containsKey(new User("name", "surname", "login", "passpass")));

        // Add comment
        server.addComment(gson.toJson(addCommentDtoRequest));

        // Add like comment
        LikeCommentDtoRequest likeCommentDtoRequest = new LikeCommentDtoRequest("song", "login", token2);
        server.likeComment(gson.toJson(likeCommentDtoRequest));

        // Remove comment
        response = gson.fromJson(server.removeComment(gson.toJson(removeCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertFalse(Database.getInstance().getSongs().get("song").getCommentsOfCommunity().isEmpty());
    }

    @Test
    public void invalidRequestLikeComment() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"","");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add comment
        AddCommentDtoRequest addCommentDtoRequest = new AddCommentDtoRequest("song", "comment", token);
        server.addComment(gson.toJson(addCommentDtoRequest));

        // Like comment with wrong token
        LikeCommentDtoRequest likeCommentDtoRequest = new LikeCommentDtoRequest("song", "login", UUID.randomUUID().toString());
        Response response = gson.fromJson(server.likeComment(gson.toJson(likeCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_TOKEN, response.getType());

        // Like comment wrong song
        likeCommentDtoRequest.setToken(token2);
        likeCommentDtoRequest.setNameSong("wrong");
        response = gson.fromJson(server.likeComment(gson.toJson(likeCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_SONG, response.getType());

        // Like wrong comment
        likeCommentDtoRequest.setNameSong("song");
        likeCommentDtoRequest.setLoginAuthorOfComment("wrong");
        response = gson.fromJson(server.likeComment(gson.toJson(likeCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_COMMENT, response.getType());
    }

    @Test
    public void isLikeComment() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"","");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add comment
        AddCommentDtoRequest addCommentDtoRequest = new AddCommentDtoRequest("song", "comment", token);
        server.addComment(gson.toJson(addCommentDtoRequest));

        // Like comment
        LikeCommentDtoRequest likeCommentDtoRequest = new LikeCommentDtoRequest("song", "login", token2);
        Response response = gson.fromJson(server.likeComment(gson.toJson(likeCommentDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertTrue(Database.getInstance().getSongs().get("song").getComments().get(new User("name", "surname", "login", "passpass")).isMark());
    }

    @Test
    public void invalidRequestRemoveLikeComment() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"","");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add comment
        AddCommentDtoRequest addCommentDtoRequest = new AddCommentDtoRequest("song", "comment", token);
        server.addComment(gson.toJson(addCommentDtoRequest));

        // Add like comment
        LikeCommentDtoRequest likeCommentDtoRequest = new LikeCommentDtoRequest("song", "login", token2);
        server.likeComment(gson.toJson(likeCommentDtoRequest));

        // Remove like comment with wrong token
        RemoveLikeDtoRequest removeLikeDtoRequest = new RemoveLikeDtoRequest("song", "login", UUID.randomUUID().toString());
        Response response = gson.fromJson(server.removeLikeComment(gson.toJson(removeLikeDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_TOKEN, response.getType());

        // Remove like comment wrong song
        removeLikeDtoRequest.setToken(token2);
        removeLikeDtoRequest.setNameSong("wrong");
        response = gson.fromJson(server.removeLikeComment(gson.toJson(removeLikeDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_SONG, response.getType());

        // Remove like wrong comment
        removeLikeDtoRequest.setNameSong("song");
        removeLikeDtoRequest.setLoginAuthorOfComment("wrong");
        response = gson.fromJson(server.removeLikeComment(gson.toJson(removeLikeDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_COMMENT, response.getType());
    }

    @Test
    public void isRemoveLikeComment() {
        Database database = Database.getInstance();
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"","");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add comment
        AddCommentDtoRequest addCommentDtoRequest = new AddCommentDtoRequest("song", "comment", token);
        server.addComment(gson.toJson(addCommentDtoRequest));

        // Add like comment
        LikeCommentDtoRequest likeCommentDtoRequest = new LikeCommentDtoRequest("song", "login", token2);
        server.likeComment(gson.toJson(likeCommentDtoRequest));

        // Remove like
        RemoveLikeDtoRequest removeLikeDtoRequest = new RemoveLikeDtoRequest("song", "login", token2);
        Response response = gson.fromJson(server.removeLikeComment(gson.toJson(removeLikeDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertFalse(Database.getInstance().getSongs().get("song").getComments().get(new User("name", "surname", "login", "passpass")).isMark());
    }





}


