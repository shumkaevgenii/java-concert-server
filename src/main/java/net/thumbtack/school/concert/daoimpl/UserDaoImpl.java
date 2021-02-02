package net.thumbtack.school.concert.daoimpl;

import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.exception.ServerException;
import net.thumbtack.school.concert.model.User;


public class UserDaoImpl implements UserDao {
    private final Database database = Database.getInstance();

    @Override
    public String register(User user) throws ServerException {
        return database.register(user);
    }

    @Override
    public String login(String login, String password) throws ServerException {
        return database.login(login, password);
    }

    @Override
    public String logout(String token) throws ServerException {
        return database.logout(token);
    }

    @Override
    public String quit(String token) throws ServerException {
        return database.quit(token);
    }
}
