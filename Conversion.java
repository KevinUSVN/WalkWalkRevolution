package edu.ucsd.cse110.team19.walkwalkrevolution;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Conversion {
    private static final int FT_TO_INCHES = 12;
    private static final float FT_MI_RATI0 = 5280;
    private static final NumberFormat formatter = new DecimalFormat("###0.0##");

    public static String heightFloatToString(float height) {
        int feet = (int)(height);
        int inches = (int)((height - (float)feet)*FT_TO_INCHES);
        return feet + "\'" + inches + "\"";
    }

    public static float stepsToMiles(float steps, float ratio) {
        float ft = steps * ratio;
        return ft/FT_MI_RATI0;
    }

    public static String formatMiles(float miles) {
        return formatter.format(miles);
    }

}
