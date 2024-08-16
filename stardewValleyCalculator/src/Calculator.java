import java.util.ArrayList;
import java.util.List;

public class Calculator {

    //display each item that was added
    public List<Crop> displayCropsSold(String [] userCropsList){
        List <Crop> cropsSold = new ArrayList<>();

        //loop through adding each crop name, quality and price to the array List.

        return cropsSold;
    }

    public int countQualityCrops (){
        int count = 0;
        //this counts the duplicate crops with same name AND same quality level
        return count;

       /* import java.util.HashMap;
import java.util.List;
import java.util.Map;

        public class CopilotPractice {
            public static Map<Object, Integer> countObjectsWithSameProperty(List<Object> objects, String name, String property) {
                Map<Object, Integer> counts = new HashMap<>();
                for (Object obj : objects) {
                    if (obj instanceof YourObjectType) { // Replace YourObjectType with the actual type of your objects
                        YourObjectType yourObject = (YourObjectType) obj;
                        if (yourObject.getName().equals(name) && yourObject.getCropQuality().equals(property)) {
                            counts.put(yourObject, counts.getOrDefault(yourObject, 0) + 1);
                        } else {
                            counts.put(yourObject, counts.getOrDefault(yourObject, 0));
                        }
                    }
                }
                return counts;
            }
        }*/
    }

    public int calculatedRevenueSingleCrop (String cropName, String cropQuality ){
        int revenue = 0;
        //calculate revenue
        //multiply the number of same quality crops by the cost of the unit.
        //Add the total up

        return revenue;
    }

    public int calculateNetProfitSingleCrop (){
        int netProfit = 0;

        //retrieve the revenue

        return netProfit;
    }
    //calculate net profit
}
