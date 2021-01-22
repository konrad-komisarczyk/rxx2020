package pl.edu.pw.gis.mykpyk.domain;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface EnemyRepository extends CrudRepository<Enemy, Long> {

    @Query(nativeQuery = true,
            value = "select * from enemies e where e.lat < :north and e.lat > :south and e.lng < :east and e.lng > :west")
    List<Enemy> selectFromRectangle(Double east, Double west, Double south, Double north);
}
