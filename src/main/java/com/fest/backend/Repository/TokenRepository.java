package com.fest.backend.Repository;

import com.fest.backend.Entity.FestUser;
import com.fest.backend.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token , Integer> {

    Optional<Token> findByToken(String token);

    @Query("""
    SELECT t FROM Token t
    WHERE t.user.email = :email AND (t.expired = false OR t.revoked = false)
    """)
    List<Token> findAllValidTokensByUser(@Param("email") String email);

}
