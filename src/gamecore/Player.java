package gamecore;

public class Player {
    private static Integer GLOBAL_ID = 0;
    private String name;
    private Integer id;

    public Player(String name) {
        this.name = name;
        this.id = generateId();
    }

    private Integer generateId() {
        GLOBAL_ID++;
        return GLOBAL_ID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        // 5,hozer
        return id+","+name;
    }

    public Integer getId() {
        return id;
    }
}
