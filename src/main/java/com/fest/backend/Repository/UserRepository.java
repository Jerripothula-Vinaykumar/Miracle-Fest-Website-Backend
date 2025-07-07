package com.fest.backend.Repository;

import com.fest.backend.Entity.FestUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<FestUser, Integer > {

}
