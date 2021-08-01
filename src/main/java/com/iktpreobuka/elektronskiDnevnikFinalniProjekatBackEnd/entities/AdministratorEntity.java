package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ADMIN")
public class AdministratorEntity extends KorisnikEntity {

	public AdministratorEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
}
