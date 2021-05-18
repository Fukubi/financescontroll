package com.learning.financescontroll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learning.financescontroll.entity.CategoryEntity;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {

	@Query("SELECT c FROM CategoryEntity c where c.user.id = :userId")
	List<CategoryEntity> findAllByUserId(@Param("userId") Long userId);
	
}
