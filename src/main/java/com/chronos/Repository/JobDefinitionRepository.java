package com.chronos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chronos.Entity.JobDefinition;



@Repository
public interface JobDefinitionRepository extends JpaRepository<JobDefinition, Long>  {

}
