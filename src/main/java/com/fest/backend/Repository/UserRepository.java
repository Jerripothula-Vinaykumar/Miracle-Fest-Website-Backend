package com.fest.backend.Repository;

import com.fest.backend.Entity.FestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<FestUser, Integer > {

    FestUser findByEmail(String email);

}
