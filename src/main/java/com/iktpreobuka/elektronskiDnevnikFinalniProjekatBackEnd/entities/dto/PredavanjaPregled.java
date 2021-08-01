package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto;

public class PredavanjaPregled {

	private Integer id;
	private String naziv;
	private String predmet;
	private String nastavnik;
	private Integer fondCasova;
	public PredavanjaPregled() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getPredmet() {
		return predmet;
	}
	public void setPredmet(String predmet) {
		this.predmet = predmet;
	}
	public String getNastavnik() {
		return nastavnik;
	}
	public void setNastavnik(String nastavnik) {
		this.nastavnik = nastavnik;
	}
	public Integer getFondCasova() {
		return fondCasova;
	}
	public void setFondCasova(Integer fondCasova) {
		this.fondCasova = fondCasova;
	}
	
	
}
