package model;

public class MarkDetail {
    int id,max,obtain;

    public MarkDetail() {
    }

    public MarkDetail(int max, int obtain, String name) {
        this.max = max;
        this.obtain = obtain;
        this.name = name;
    }

    public MarkDetail(int id, int max, int obtain, String name) {
        this.id = id;
        this.max = max;
        this.obtain = obtain;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getObtain() {
        return obtain;
    }

    public void setObtain(int obtain) {
        this.obtain = obtain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
}
