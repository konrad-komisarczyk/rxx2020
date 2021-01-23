package pl.edu.pw.gis.mykpyk.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainConf { //TODO use JavaProperties instead??
    public static double enemyShowRadius = 0.003;
    public static double enemySpawnRadius = 0.01;
    public static double regenerationDistance = 0.01;
    public static ArrayList<Integer> neededExpForLvl =
            new ArrayList<>(Arrays.asList(-1, 0, 100, 250, 500, 1000, 1800, 3000, 4500, 7000, 10000));
    public static double engageDistance = 0.001;
    public static Integer maxEnemyTypes = 20;
    public static double minimalProgress = 0.0005;
    public static Integer backpackSize = 20;
    public static Integer backpackArmorPosition = -2;
    public static Integer backpackWeaponPosition = -1;

    public static final Random random = new Random();

    public static double eps() {
        return random.nextDouble() * 0.0004 - 0.0002;
    }

}
