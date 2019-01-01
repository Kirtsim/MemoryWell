package fm.kirtsim.kharos.memorywell.db;

public class Resource<Data> {

    private Status status;
    private Data data;
    private String message;

    private Resource(Status status, Data data, String message) {
        this.status = status;
        this.data = data;
        this.message = emptyStringIfMessageIsNull(message);
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

    private String emptyStringIfMessageIsNull(String message) {
        if (message == null)
            return "";
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Resource))
            return false;
        if (this == obj)
            return true;
        Resource<Data> other = null;
        try {
             other = (Resource<Data>) obj;
        } catch (Exception ex) {
            return false;
        }

        return this.message.equals(other.message)&&
                other.status == status &&
                data.equals(other.data);
    }

    @Override
    public String toString() {
        return "Resource<" + data.getClass().getSimpleName() + ">\n" +
                "- status: " + status.toString() +
                "\n- message: " + message +
                "\n- data: " + data.toString();
    }
}
