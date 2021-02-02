package net.thumbtack.school.concert.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.thumbtack.school.concert.daoimpl.SongDaoImpl;
import net.thumbtack.school.concert.dto.request.comment.*;
import net.thumbtack.school.concert.dto.request.concert.GetConcertDtoRequest;
import net.thumbtack.school.concert.dto.request.rating.*;
import net.thumbtack.school.concert.dto.request.song.*;
import net.thumbtack.school.concert.exception.ServerException;
import net.thumbtack.school.concert.dto.response.*;
import net.thumbtack.school.concert.model.Song;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SongService {
    private final Gson gson = new Gson();
    private final SongDaoImpl songDao = new SongDaoImpl();

    public <T> T getDtoFromJson(String requestJsonString, Class<T> dtoClass) throws ServerException {
        try {
            return gson.fromJson(requestJsonString, dtoClass);
        } catch (JsonSyntaxException e) {
            throw new ServerException(TypeOfResponse.WRONG_JSON);
        }
    }

    public String addSongs(String requestJsonString) {
        try {
            AddSongsDtoRequest dtoRequest = getDtoFromJson(requestJsonString, AddSongsDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            Set<Song> songs = new HashSet<>();

            for (int i = 0; i < dtoRequest.getNumberOfSongs(); i++) {
                Song song = new Song(dtoRequest.getNameOfSong().get(i),
                        dtoRequest.getComposers().get(i),
                        dtoRequest.getAuthors().get(i),
                        dtoRequest.getPerformer().get(i),
                        dtoRequest.getSongDuration().get(i));
                songs.add(song);
            }

            return songDao.addSongs(songs, dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String removeSongs(String requestJsonString) {
        try {
            RemoveSongsDtoRequest dtoRequest = getDtoFromJson(requestJsonString, RemoveSongsDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return songDao.removeSongs(dtoRequest.getNamesSong(), dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }

    }


    public String getSongs(String requestJsonString) {
        try {
            GetSongsDtoRequest dtoRequest = getDtoFromJson(requestJsonString, GetSongsDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
            Type type = new TypeToken<HashSet<Song>>(){}.getType();
            return gson.toJson(songDao.getSongs(dtoRequest.getToken()), type);
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String getSongsByComposers(String requestJsonString) {
        try {
            GetSongsByComposersDtoRequest dtoRequest = getDtoFromJson(requestJsonString, GetSongsByComposersDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
            Type type = new TypeToken<HashSet<Song>>(){}.getType();
            return gson.toJson(songDao.getSongsByComposers(dtoRequest.getComposers(), dtoRequest.getToken()), type);
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String getSongsByAuthors(String requestJsonString) {
        try {
            GetSongsByAuthorsDtoRequest dtoRequest = getDtoFromJson(requestJsonString, GetSongsByAuthorsDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
            Type type = new TypeToken<HashSet<Song>>(){}.getType();
            return gson.toJson(songDao.getSongsByAuthors(dtoRequest.getAuthors(), dtoRequest.getToken()), type);
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String getSongsByPerformer(String requestJsonString) {
        try {
            GetSongsByPerformerDtoRequest dtoRequest = getDtoFromJson(requestJsonString, GetSongsByPerformerDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.WRONG_TOKEN).toGson();
            }

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
            Type type = new TypeToken<HashSet<Song>>(){}.getType();
            return gson.toJson(songDao.getSongsByPerformer(dtoRequest.getPerformer(), dtoRequest.getToken()), type);
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }


    public String addRating(String requestJsonString) {
        try {
            AddRatingDtoRequest dtoRequest = getDtoFromJson(requestJsonString, AddRatingDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return songDao.pushRating(dtoRequest.getNameSong(), dtoRequest.getRating(), dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String removeRating(String requestJsonString) {
        try {
            RemoveRatingDtoRequest dtoRequest = getDtoFromJson(requestJsonString, RemoveRatingDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return songDao.removeRating(dtoRequest.getNameSong(), dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String editRating(String requestJsonString) {
        try {
            EditRatingDtoRequest dtoRequest = getDtoFromJson(requestJsonString, EditRatingDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return songDao.pushRating(dtoRequest.getNameSong(), dtoRequest.getRating(), dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }


    public String addComment(String requestJsonString) {
        try {
            AddCommentDtoRequest dtoRequest = getDtoFromJson(requestJsonString, AddCommentDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return songDao.addComment(dtoRequest.getNameSong(), dtoRequest.getText(), dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String removeComment(String requestJsonString) {
        try {
            RemoveCommentDtoRequest dtoRequest = getDtoFromJson(requestJsonString, RemoveCommentDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return songDao.removeComment(dtoRequest.getNameSong(), dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String editComment(String requestJsonString) {
        try {
            EditCommentDtoRequest dtoRequest = getDtoFromJson(requestJsonString, EditCommentDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return songDao.editComment(dtoRequest.getNameSong(), dtoRequest.getComment(), dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String likeComment(String requestJsonString) {
        try {
            LikeCommentDtoRequest dtoRequest = getDtoFromJson(requestJsonString, LikeCommentDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return songDao.likeComment(dtoRequest.getNameSong(), dtoRequest.getLoginAuthorOfComment(), dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String removeLikeComment(String requestJsonString) {
        try {
            RemoveLikeDtoRequest dtoRequest = getDtoFromJson(requestJsonString, RemoveLikeDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return songDao.removeLikeComment(dtoRequest.getNameSong(), dtoRequest.getLoginAuthorOfComment(), dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }


    public String getConcert(String requestJsonString) {
        try {
            GetConcertDtoRequest dtoRequest = getDtoFromJson(requestJsonString, GetConcertDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.WRONG_TOKEN).toGson();
            }

            return gson.toJson(songDao.getConcert(dtoRequest.getToken()));
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }
}
