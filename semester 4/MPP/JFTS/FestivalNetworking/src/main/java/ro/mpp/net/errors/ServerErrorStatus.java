package ro.mpp.net.errors;

public class ServerErrorStatus extends RuntimeException {
    public ServerErrorStatus(String message) {
        super(message);
    }
}
