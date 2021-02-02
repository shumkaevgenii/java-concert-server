package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.exception.ServerException;
import net.thumbtack.school.concert.model.User;

public interface UserDao {
    String register(User user) throws ServerException;
    String login(String login, String password) throws ServerException;
    String logout(String token) throws ServerException;
    String quit(String token) throws ServerException;
}
