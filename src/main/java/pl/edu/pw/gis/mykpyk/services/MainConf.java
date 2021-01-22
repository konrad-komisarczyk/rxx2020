package pl.edu.pw.gis.mykpyk.services;

import java.util.ArrayList;
import java.util.Arrays;

public class MainConf { //TODO use JavaProperties instead??
    public static double enemyShowRadius = 0.002;
    public static double enemySpawnRadius = 0.004;
    public static double regenerationDistance = 0.01;
    public static ArrayList<Integer> neededExpForLvl =
            new ArrayList<>(Arrays.asList(-1, 0, 100, 250, 500, 1000, 1800, 3000, 4500, 7000, 10000));
    public static double engageDistance = 0.001;
    public static Integer maxEnemyTypes = 20;
    public static double minimalProgress = 0.0005;

    public static double eps() { // +/- 100m
        return Math.random() * 0.002 - 0.001;
    }

}
