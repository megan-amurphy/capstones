public class Seed {
    private int seedId;
    private String seedName;
    private int seedCost;
    private String seedLocation;
    private boolean isSpecial;

    public Seed(int seedId, String seedName, int seedCost, String seedLocation, boolean isSpecial) {
        this.seedId = seedId;
        this.seedName = seedName;
        this.seedCost = seedCost;
        this.seedLocation = seedLocation;
        this.isSpecial = isSpecial;
    }

    public int getSeedId() {
        return seedId;
    }

    public void setSeedId(int seedId) {
        this.seedId = seedId;
    }

    public String getSeedName() {
        return seedName;
    }

    public void setSeedName(String seedName) {
        this.seedName = seedName;
    }

    public int getSeedCost() {
        return seedCost;
    }

    public void setSeedCost(int seedCost) {
        this.seedCost = seedCost;
    }

    public String getSeedLocation() {
        return seedLocation;
    }

    public void setSeedLocation(String seedLocation) {
        this.seedLocation = seedLocation;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }
}
