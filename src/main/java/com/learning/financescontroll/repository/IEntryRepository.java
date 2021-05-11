package com.learning.financescontroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.financescontroll.entity.EntryEntity;

public interface IEntryRepository extends JpaRepository<EntryEntity, Long>{

}
