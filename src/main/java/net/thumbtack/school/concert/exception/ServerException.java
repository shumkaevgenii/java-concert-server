package net.thumbtack.school.concert.exception;

import net.thumbtack.school.concert.dto.response.TypeOfResponse;

public class ServerException extends java.lang.Exception {
    private final TypeOfResponse type;

    public ServerException(TypeOfResponse type) {
        this.type = type;
    }

    public TypeOfResponse getType() {
        return type;
    }
}
