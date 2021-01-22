package pl.edu.pw.gis.mykpyk.domain;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface HeroRepository  extends CrudRepository<Hero, Long> {
    List<Hero> findByUserId(Long userId);
}
