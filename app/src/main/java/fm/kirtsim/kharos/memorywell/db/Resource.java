package fm.kirtsim.kharos.memorywell.db;

public class Resource<Data> {

    private Status status;
    private Data data;
    private String message;

    private Resource(Status status, Data data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Status status() {
        return status;
    }

    public Data data() {
        return data;
    }

    public String message() {
        return message;
    }

    public static <Data> Resource<Data> success(Data data) {
        return new Resource<>(Status.SUCCESS, data, "");
    }

    public static <Data> Resource<Data> error(String message, Data data) {
        return new Resource<>(Status.ERROR, data, message);
    }

    public static <Data> Resource<Data> loading(Data data) {
        return new Resource<>(Status.LOADING, data, "");
    }
}
