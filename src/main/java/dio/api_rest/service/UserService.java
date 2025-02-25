package dio.api_rest.service;

import dio.api_rest.domain.model.User;

public interface UserService {
    User findById(Long id);
    User create(User userToCreate);
}
