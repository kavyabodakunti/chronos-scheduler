package com.chronos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chronos.Entity.JobRun;



@Repository
public interface JobRunRepository extends JpaRepository<JobRun,Long> {

}
