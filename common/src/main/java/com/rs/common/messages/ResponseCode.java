package com.rs.common.messages;

public enum ResponseCode {
    ACCESS_DENIED("Доступ запрещен"),
    ALREADY_LOGGED_IN("Уже залогинен", false),
    DIRECTORY_NOT_FOUND("Директория не найдена"),
    FILE_NOT_FOUND("Файл не найден"),
    NO_ACCESS("Нет доступа"),
    INVALID_LOGIN("Неверный логин или пароль"),
    OK("", false),
    CANNOT_SAVE_FILE("Невозможно сохранить файл"),
    FILE_CORRUPTED("Файл поврежден");


    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return isError;
    }

    private String message;
    private boolean isError = true;

    ResponseCode(String message) {
        this.message = message;
    }

    ResponseCode(String message, boolean isError) {
        this.message = message;
        this.isError = isError;
    }


}
