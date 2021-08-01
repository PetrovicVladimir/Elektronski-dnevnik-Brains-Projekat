package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "NASTAVNIK")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class NastavnikEntity extends KorisnikEntity {
	
	private String email;
	
	@JsonIgnore
	@OneToMany(mappedBy = "nastavnik", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	protected List<PredavanjeEntity> predavanja = new ArrayList<>();

	public NastavnikEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<PredavanjeEntity> getPredavanja() {
		return predavanja;
	}

	public void setPredavanja(List<PredavanjeEntity> predavanja) {
		this.predavanja = predavanja;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
