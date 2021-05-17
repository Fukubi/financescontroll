package com.learning.financescontroll.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.financescontroll.entity.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByCredenciaisUsername(String username);
	
}
