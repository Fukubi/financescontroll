package com.learning.financescontroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.financescontroll.entity.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

}
