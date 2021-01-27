package pl.edu.pw.gis.mykpyk.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainConf {
    public static final double enemyShowRadius = 0.0025;
    public static final double enemySpawnRadius = 0.01;
    public static final double regenerationDistance = 0.0025;
    public static final double engageDistance = 0.001;

    public static final ArrayList<Integer> neededExpForLvl =
            new ArrayList<>(Arrays.asList(-1, 0, 100, 250, 500, 1000, 1800, 3000, 4500, 7000,
                    10000, 16000, 30000, 62500, 125000, 250000, 500000, 1000000, Integer.MAX_VALUE));

    public static final Integer backpackSize = 20;
    public static final Integer backpackArmorPosition = -2;
    public static final Integer backpackWeaponPosition = -1;

    public static final Integer maxEnemyTypes = 16;
    public static final Random random = new Random();
    public static double eps() {
        return random.nextDouble() * 0.0004 - 0.0002;
    }

}
