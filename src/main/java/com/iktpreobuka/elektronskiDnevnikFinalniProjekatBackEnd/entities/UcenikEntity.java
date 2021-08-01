package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "UCENIK")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UcenikEntity extends KorisnikEntity {

	private String JMBG;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "odeljenje")
	private OdeljenjeEntity odeljenje;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "roditelj")
	private RoditeljEntity roditelj;
	
	@OneToMany(mappedBy = "ucenik", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<DnevnikEntity> dnevnik = new ArrayList<>();

	public UcenikEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getJMBG() {
		return JMBG;
	}

	public void setJMBG(String JMBG) {
		this.JMBG = JMBG;
	}

	public OdeljenjeEntity getPohadjaRazred() {
		return odeljenje;
	}

	public void setPohadjaRazred(OdeljenjeEntity odeljenje) {
		this.odeljenje = odeljenje;
	}

	public RoditeljEntity getRoditelj() {
		return roditelj;
	}

	public void setRoditelj(RoditeljEntity roditelj) {
		this.roditelj = roditelj;
	}

	public List<DnevnikEntity> getDnevnik() {
		return dnevnik;
	}

	public void setDnevnik(List<DnevnikEntity> dnevnik) {
		this.dnevnik = dnevnik;
	}
}
