package pl.edu.pw.gis.mykpyk;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import pl.edu.pw.gis.mykpyk.domain.User;
import pl.edu.pw.gis.mykpyk.domain.UserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Inject
    private UserRepository userRepository;

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            String login = authenticationRequest.getIdentity().toString();
            List<User> userFound = userRepository.findByLogin(login);

            if (userFound.size() == 1 && authenticationRequest.getSecret().equals(userFound.get(0).getPassword())) {
                UserDetails userDetails = new UserDetails((String) authenticationRequest.getIdentity(), new ArrayList<>());
                emitter.onNext(userDetails);
                emitter.onComplete();
            } else {
                emitter.onError(new AuthenticationException(new AuthenticationFailed()));
            }

        }, BackpressureStrategy.ERROR);
    }
}