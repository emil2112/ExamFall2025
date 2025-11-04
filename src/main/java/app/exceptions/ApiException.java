package app.exceptions;

public class ApiException extends RuntimeException {
    private int statusCode;

    public ApiException(int code, String msg){
        super(msg);
        this.statusCode = code;
    }

    public int getStatusCode() {
        return statusCode;
    }
}