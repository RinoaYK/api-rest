package dio.api_rest.service.impl;

import dio.api_rest.domain.model.User;
import dio.api_rest.service.exception.BusinessException;
import dio.api_rest.domain.repository.UserRepository;
import dio.api_rest.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import static java.util.Optional.ofNullable;

@Service
public class UserServiceImpl implements UserService {

    private static final Long UNCHANGEABLE_USER_ID = 1L;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return  userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() { return this.userRepository.findAll();}

    @Transactional
    public User create(User userToCreate) {
        ofNullable(userToCreate).orElseThrow(() -> new BusinessException("Campo de usuário não pode ser nulo."));
        ofNullable(userToCreate.getAccount()).orElseThrow(() -> new BusinessException("Account não pode ser nulo."));
        ofNullable(userToCreate.getCard()).orElseThrow(() -> new BusinessException("Cartão não pode ser nulo."));

        this.validateChangeableId(userToCreate.getId(), "created");
        if (userRepository.existsByAccountNumber(userToCreate.getAccount().getNumber())) {
            throw new BusinessException("Esse número de conta já existe.");
        }
        if (userRepository.existsByCardNumber(userToCreate.getCard().getNumber())) {
            throw new BusinessException("Esse número de cartão já existe.");
        }
        return this.userRepository.save(userToCreate);
    }

    @Transactional
    public User update(Long id, User userToUpdate) {
        this.validateChangeableId(id, "updated");
        User dbUser = this.findById(id);
        if (!dbUser.getId().equals(userToUpdate.getId())) {
            throw new BusinessException("Os IDs devem ser iguais.");
        }

        dbUser.setName(userToUpdate.getName());
        dbUser.setAccount(userToUpdate.getAccount());
        dbUser.setCard(userToUpdate.getCard());
        dbUser.setFeatures(userToUpdate.getFeatures());
        dbUser.setNews(userToUpdate.getNews());

        return this.userRepository.save(dbUser);
    }

    @Transactional
    public void delete(Long id) {
        this.validateChangeableId(id, "deleted");
        User dbUser = this.findById(id);
        this.userRepository.delete(dbUser);
    }

    private void validateChangeableId(Long id, String operation) {
        if (UNCHANGEABLE_USER_ID.equals(id)) {
            throw new BusinessException("User com ID %d não pode ser %s.".formatted(UNCHANGEABLE_USER_ID, operation));
        }
    }
}
