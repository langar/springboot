package com.fhi.datex;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


/**
 * The Main Class.
 */
@SpringBootApplication
@EnableScheduling
public class Application {
	
	/** The job launcher. */
	@Autowired
    JobLauncher jobLauncher;
     
    /** The job. */
    @Autowired
    Job job;
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String args[]) {
        SpringApplication.run(BatchConfiguration.class, args);
    }
    
    /**
     * Perform.
     *
     * @throws Exception the exception
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void perform() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }
}
