package models;

public class Homework extends Entity<String> {
    private String desc;

    public Homework(String s, String desc) {
        super(s);
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Homework{" +
                "desc='" + desc + '\'' +
                '}';
    }
}
