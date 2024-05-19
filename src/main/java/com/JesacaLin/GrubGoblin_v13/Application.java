package com.JesacaLin.GrubGoblin_v13;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/*
	LONG TERM TO DO:
	*Need to handle deals that happen on multiple days (maybe check marks in the UI makes sense here)

	THIS TIME'S PROJECT SCOPE:
	*Create more ways to filter info
		*See days based on the day of week
	*Fix the sql diagram
	*Add created_at timestamp... this way the deals can be checked for validness?
	*need to try branches in git!


	For demo purpses:
	*add and save a new place all ready to go so I don't have to type it.

	 */
}
