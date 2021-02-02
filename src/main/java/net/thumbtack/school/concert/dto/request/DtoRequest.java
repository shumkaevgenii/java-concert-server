package net.thumbtack.school.concert.dto.request;

public abstract class DtoRequest {
    private String token;

    public DtoRequest(String token) {
        this.token = token;
    }

    public DtoRequest() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
