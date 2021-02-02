package net.thumbtack.school.concert.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.thumbtack.school.concert.daoimpl.UserDaoImpl;
import net.thumbtack.school.concert.dto.request.user.*;
import net.thumbtack.school.concert.exception.ServerException;
import net.thumbtack.school.concert.dto.response.Response;
import net.thumbtack.school.concert.dto.response.TypeOfResponse;
import net.thumbtack.school.concert.model.User;


public class UserService {
    private final Gson gson = new Gson();
    private final UserDaoImpl userDao = new UserDaoImpl();


    public <T> T getDtoFromJson(String requestJsonString, Class<T> dtoClass) throws ServerException {
        try {
            return gson.fromJson(requestJsonString, dtoClass);
        } catch (JsonSyntaxException e) {
            throw new ServerException(TypeOfResponse.WRONG_JSON);
        }
    }

    public String registerUser(String requestJsonString) {
        try {
            RegisterUserDtoRequest dtoRequest = getDtoFromJson(requestJsonString, RegisterUserDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            User user = new User(dtoRequest.getFirstName(),
                    dtoRequest.getLastName(),
                    dtoRequest.getLogin(),
                    dtoRequest.getPassword());

            return gson.toJson(userDao.register(user), String.class);
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String login(String requestJsonString) {
        try {
            LoginUserDtoRequest dtoRequest = getDtoFromJson(requestJsonString, LoginUserDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return gson.toJson(userDao.login(dtoRequest.getLogin(), dtoRequest.getPassword()));
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String logout(String requestJsonString) {
        try {
            LogoutUserDtoRequest dtoRequest = getDtoFromJson(requestJsonString, LogoutUserDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return userDao.logout(dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

    public String quit(String requestJsonString) {
        try {
            QuitUserDtoRequest dtoRequest = getDtoFromJson(requestJsonString, QuitUserDtoRequest.class);

            if (!dtoRequest.validate()) {
                return new Response(TypeOfResponse.INVALID_DATA).toGson();
            }

            return userDao.quit(dtoRequest.getToken());
        } catch (ServerException e) {
            return new Response(e.getType()).toGson();
        }
    }

}
