package com.JesacaLin.GrubGoblin_v13;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/*
	FRONT END TO DOS:
	*Need to handle deals that happen on multiple days (maybe check marks in the UI makes sense here).
	*Hook up Google api, at the point of creating a place, api is called.

	THIS TIME'S PROJECT SCOPE:
	*1. Let's do full auth now
	*2. Add documentation to all my code
	*3. Convert all old way of mapping to new style
	 */
}
