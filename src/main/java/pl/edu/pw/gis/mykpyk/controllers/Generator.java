package pl.edu.pw.gis.mykpyk.controllers;

import pl.edu.pw.gis.mykpyk.domain.*;
import pl.edu.pw.gis.mykpyk.services.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class Generator {

    private static final Random random = new Random();


    /**
     * Converts a set of Integer to an array of int.
     */
    private static int[] setToArray(Set<Integer> aSet) {
        int[] result = new int[aSet.size()];
        int index = 0;
        for (int number : aSet) {
            result[index] = number;
            index++;
        }
        return result;
    }

    /**
     * Generates an array of min(count, maxValue) distinct random ints
     * from [0, maxValue - 1] range.
     * @param count The number of elements to be generated.
     * @param maxValue The upper bound of the range(exclusively).
     */
    private static int[] getDistinctRandomNumbers(int count, int maxValue) {
        Set<Integer> was = new HashSet<>();
        for (int i = Math.max(0, maxValue - count); i < maxValue; i++) {
            int curr = i == 0 ? 0 : random.nextInt(i);
            if (was.contains(curr))
                curr = i;
            was.add(curr);
        }
        return setToArray(was);
    }


    @Inject private HeroRepository heroRepository;
    @Inject private EnemyRepository enemyRepository;
    @Inject private EnemyTypeRepository enemyTypeRepository;

    void generateEnemies(double lat, double lon) {
        Random random = new Random();

        double n = lat + MainConf.enemySpawnRadius;
        double e = lon + MainConf.enemySpawnRadius;
        double s = lat - MainConf.enemySpawnRadius;
        double w = lon - MainConf.enemySpawnRadius;

        for (int i = 1; i <= MainConf.maxEnemyTypes; i++) {
            Optional<EnemyType> enemyTypeOptional = enemyTypeRepository.findById((long) i);
            if (enemyTypeOptional.isPresent()) {
                EnemyType enemyType = enemyTypeOptional.get();

                int numberNearby = 0; //TODO

                if (numberNearby <= Math.floor(enemyType.getSpawnIntensity())) {
                    int nOfEnemies = (int) Math.floor(random.nextDouble() + enemyType.getSpawnIntensity());

                    if (enemyType.getSpawnKey() == null) {
                        for (int j = 0; j < nOfEnemies; j++) {
                            Enemy newEnemy = new Enemy((int) (long) enemyType.getId(),
                                    w + (e - w) * random.nextDouble() + MainConf.eps(),
                                    s + (n - s) * random.nextDouble() + MainConf.eps()
                            );
                            System.out.println("GENERATED ENEMY AT " + newEnemy.getLat() + ", " + newEnemy.getLng() );
                            enemyRepository.save(newEnemy);
                        }
                    } else {
                        ElementsLocations el = OSMLoader.getCoordinatesOfObjects(n, e, s, w,
                                enemyType.getSpawnKey(), enemyType.getSpawnValue());
                        if (el == null) continue;
                        int[] disinctNumbers = getDistinctRandomNumbers(nOfEnemies, el.getLats().size());
                        for (int j : disinctNumbers) {
                            Enemy newEnemy = new Enemy((int) (long) enemyType.getId(),
                                    el.getLngs().get(j) + MainConf.eps(),
                                    el.getLats().get(j) + MainConf.eps()
                            );
                            System.out.println("GENERATED ENEMY AT " + newEnemy.getLat() + ", " + newEnemy.getLng() );
                            enemyRepository.save(newEnemy);
                        }
                    }
                }
            }
        }
    }
}





