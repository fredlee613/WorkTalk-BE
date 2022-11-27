package com.golfzonTech4.worktalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication()
public class WorktalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorktalkApplication.class, args);
	}

}
