package db.model;


//가게 정보에 대한 항목 및 특성 데이터 처리
public class DB2024Team13_restaurantInfo {
    private String name;
    private String location;
    private String bestMenu;
    private String category;
    private boolean breakTime;
    private boolean eatAlone;
    private int orderCount;
    private double avgRating;

    public DB2024Team13_restaurantInfo(String name, String location, String bestMenu, String category, boolean breakTime, boolean eatAlone, int orderCount, double avgRating) {
        this.name = name;
        this.location = location;
        this.bestMenu = bestMenu;
        this.category = category;
        this.breakTime = breakTime;
        this.eatAlone = eatAlone;
        this.orderCount = orderCount;
        this.avgRating = avgRating;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getBestMenu() {
        return bestMenu;
    }

    public String getCategory() {
        return category;
    }

    public boolean isBreakTime() {
        return breakTime;
    }

    public boolean isEatAlone() {
        return eatAlone;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public double getAvgRating() {
        return avgRating;
    }
}