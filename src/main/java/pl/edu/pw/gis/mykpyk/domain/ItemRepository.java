package pl.edu.pw.gis.mykpyk.domain;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ItemRepository  extends CrudRepository<Item, Long> {
}
