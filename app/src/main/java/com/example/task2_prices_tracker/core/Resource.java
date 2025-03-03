package com.example.task2_prices_tracker.core;

public abstract class Resource<T> {

    private final T data;
    private final String message;

    protected Resource(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class Success<T> extends Resource<T> {
        public Success(T data) {
            super(data, null);
        }
    }

    public static class Error<T> extends Resource<T> {
        public Error(String message) {
            super(null, message);
        }
    }

    public static class Loading<T> extends Resource<T> {
        public Loading() {
            super(null, null);
        }
    }
}