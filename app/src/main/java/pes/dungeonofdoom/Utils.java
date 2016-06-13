package pes.dungeonofdoom;

/**
 * Created by lylin on 12.06.16.
 */
public class Utils {
    public static int rnd(int min, int max) {
        return (int)((max - min + 1)*Math.random() + min);
    }
}
