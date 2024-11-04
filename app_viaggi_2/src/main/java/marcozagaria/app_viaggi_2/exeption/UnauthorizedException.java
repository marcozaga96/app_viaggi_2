package marcozagaria.app_viaggi_2.exeption;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
