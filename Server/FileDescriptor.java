public class FileDescriptor {
    private int id;
    private String name;
    private byte[] data;
    private String type;

    public File(int id, String name, byte[] data, String type) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setType(String type) {
        this.type = type;
    }
}
