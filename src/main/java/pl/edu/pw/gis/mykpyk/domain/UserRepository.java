package pl.edu.pw.gis.mykpyk.domain;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import pl.edu.pw.gis.mykpyk.domain.User;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByLogin(String login);

    User save(@NotNull String login, @NotNull String password);
}
