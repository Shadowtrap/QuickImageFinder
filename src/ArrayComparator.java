import java.awt.Color;
import java.util.Comparator;

public class ArrayComparator implements Comparator<int[]> {

    private final double THRESHOLD = 0.10;
    private final double PERCENT_OF_IMAGE_THRESHOLD = 0.96458;


    @Override
    public int compare(int[] o1, int[] o2) {
        int counter = 0;

        for(int i = 0; i < o1.length; i++){
            int o1R = new Color(o1[i]).getRed();
            int o1G = new Color(o1[i]).getBlue();
            int o1B = new Color(o1[i]).getGreen();
            int o2R = new Color(o2[i]).getRed();
            int o2G = new Color(o2[i]).getBlue();
            int o2B = new Color(o2[i]).getGreen();

            double distance = Math.sqrt(Math.pow(o1R - o2R, 2) + (Math.pow(o1B - o2B, 2) + (Math.pow(o1G - o2G, 2))));
            double outOf = Math.sqrt(Math.pow(255, 2) + (Math.pow(255, 2) + (Math.pow(255, 2))));
            double percentDistance = distance/outOf;

            if(percentDistance <= THRESHOLD)
                counter++;
        }

        double percentOfImage = (double)counter / o1.length;
        if(percentOfImage < PERCENT_OF_IMAGE_THRESHOLD)
            return -1;

        return 0;
    }
}
