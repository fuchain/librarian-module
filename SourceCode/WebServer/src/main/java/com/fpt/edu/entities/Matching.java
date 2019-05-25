package com.fpt.edu.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Matching {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)	
	private Long id;
	
	
}
