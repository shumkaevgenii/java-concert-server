package net.thumbtack.school.concert.dto.request.user;

import net.thumbtack.school.concert.dto.request.Validations;

public class RegisterUserDtoRequest {
    private String FirstName;
    private String LastName;
    private String Login;
    private String Password;

    public RegisterUserDtoRequest(String firstName, String lastName, String login, String password) {
        FirstName = firstName;
        LastName = lastName;
        Login = login;
        Password = password;
    }

    public RegisterUserDtoRequest() {

    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
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

    public String getFirstName() {
        return FirstName;
    }

    public boolean validate() {
        return (Validations.firstNameIsValid(getFirstName()) &&
                Validations.lastNameIsValid(getLastName()) &&
                Validations.loginIsValid(getLogin()) &&
                Validations.passwordIsValid(getPassword()));

    }

}
