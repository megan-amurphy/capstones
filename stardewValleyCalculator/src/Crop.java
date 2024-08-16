public class Crop {
    private int cropId;
    private String cropName;

    private String cropQuality;
    private int normalSellingPrice;
    private int silverSellingPrice;
    private int goldSellingPrice;
    private int iridiumSellingPrice;

    public Crop(int cropId, String cropName, String cropQuality, int normalSellingPrice,
                int silverSellingPrice, int goldSellingPrice, int iridiumSellingPrice) {
        this.cropId = cropId;
        this.cropName = cropName;
        this.normalSellingPrice = normalSellingPrice;
        this.silverSellingPrice = silverSellingPrice;
        this.goldSellingPrice = goldSellingPrice;
        this.iridiumSellingPrice = iridiumSellingPrice;
    }

    public Crop (){

    }

    public int getCropId() {
        return cropId;
    }

    public void setCropId(int cropId) {
        this.cropId = cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropQuality() {
        return cropQuality;
    }

    public void setCropQuality(String cropQuality) {
        this.cropQuality = cropQuality;
    }

    public int getNormalSellingPrice() {
        return normalSellingPrice;
    }

    public void setNormalSellingPrice(int normalSellingPrice) {
        this.normalSellingPrice = normalSellingPrice;
    }

    public int getSilverSellingPrice() {
        return silverSellingPrice;
    }

    public void setSilverSellingPrice(int silverSellingPrice) {
        this.silverSellingPrice = silverSellingPrice;
    }

    public int getGoldSellingPrice() {
        return goldSellingPrice;
    }

    public void setGoldSellingPrice(int goldSellingPrice) {
        this.goldSellingPrice = goldSellingPrice;
    }

    public int getIridiumSellingPrice() {
        return iridiumSellingPrice;
    }

    public void setIridiumSellingPrice(int iridiumSellingPrice) {
        this.iridiumSellingPrice = iridiumSellingPrice;
    }

    @Override
    public String toString() {
        return "Crop{" +
                "cropId=" + cropId +
                ", cropName='" + cropName + '\'' +
                ", normalSellingPrice=" + normalSellingPrice +
                ", silverSellingPrice=" + silverSellingPrice +
                ", goldSellingPrice=" + goldSellingPrice +
                ", iridiumSellingPrice=" + iridiumSellingPrice +
                '}';
    }
}
