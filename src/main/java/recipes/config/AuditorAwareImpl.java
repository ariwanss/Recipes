package recipes.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import recipes.user.User;
import recipes.userDetail.UserDetailsImpl;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<User> {


    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(
                ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUser());
    }
}
