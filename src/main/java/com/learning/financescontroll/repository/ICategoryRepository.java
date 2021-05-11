package com.learning.financescontroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.financescontroll.entity.CategoryEntity;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
