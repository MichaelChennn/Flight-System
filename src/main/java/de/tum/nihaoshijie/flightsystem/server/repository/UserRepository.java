package de.tum.nihaoshijie.flightsystem.server.repository;

import de.tum.nihaoshijie.flightsystem.server.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = :email")
    List<User> findUsersByEmail(@Param("email") String email);

    @Query("select u from User u where u.userName = :userName")
    Optional<User> findUserByUserName(@Param("userName") String userName);

    @Modifying
    @Transactional
    @Query("update User u set u.userName = :userName, u.email = :email, u.birthDate = :birthDate, u.password = :password where u.id = :id")
    void update(@Param("id") long id, @Param("userName") String userName, @Param("birthDate") LocalDate birthDate,
                @Param("email") String email, @Param("password") String password);
}