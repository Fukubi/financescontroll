package com.learning.financescontroll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learning.financescontroll.entity.EntryEntity;

public interface IEntryRepository extends JpaRepository<EntryEntity, Long>{

	@Query("SELECT e FROM EntryEntity e WHERE e.user.id = :userId")
	List<EntryEntity> findAllByUserId(@Param("userId")Long userId);
	
}
