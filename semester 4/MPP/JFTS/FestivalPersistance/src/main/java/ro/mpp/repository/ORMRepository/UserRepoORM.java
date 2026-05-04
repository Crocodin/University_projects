package ro.mpp.repository.ORMRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.User;
import ro.mpp.repository.IUserRepository;
import ro.mpp.utils.JPAUtils;

import java.util.List;
import java.util.Optional;

public class UserRepoORM implements IUserRepository {
    private static final Logger logger = LogManager.getLogger(UserRepoORM.class);
    private final EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        logger.info("Finding user by username: {} and password", username);
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.createQuery("select u from User u where u.username = :username and u.password = :password", User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            em.getTransaction().commit();
            if (user != null) return Optional.of(user);
            else return Optional.empty();
        }
    }

    @Override
    public Optional<User> save(User entity) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        return Optional.empty();
    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public Optional<User> find(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }
}
