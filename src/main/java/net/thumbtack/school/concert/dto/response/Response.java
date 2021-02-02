package net.thumbtack.school.concert.dto.response;

import com.google.gson.Gson;

public class Response {
    private final TypeOfResponse type;

    public TypeOfResponse getType() {
        return type;
    }

    public Response(TypeOfResponse type) {
        this.type = type;
    }

    public String toGson(){
        return new Gson().toJson(this, this.getClass());
    }
}
