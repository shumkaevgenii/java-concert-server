package net.thumbtack.school.concert.dto.request.user;

import net.thumbtack.school.concert.dto.request.Validations;

public class LoginUserDtoRequest {
    private String Login;
    private String Password;

    public LoginUserDtoRequest(String login, String password) {
        Login = login;
        Password = password;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean validate() {
        return (Validations.loginIsValid(getLogin()) &&
                Validations.passwordIsValid(getPassword()));
    }
}
