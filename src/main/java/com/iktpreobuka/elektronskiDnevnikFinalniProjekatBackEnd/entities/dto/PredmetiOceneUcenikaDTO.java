package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto;

import java.time.LocalDate;

public class PredmetiOceneUcenikaDTO {

	private String imePrezimeUcenika;

	private String jmbg;

	private String razredOdeljenje;

	private String predmet;

	private Integer vrednostOcene;

	private String tipOcene;

	private String polugodiste;

	private LocalDate datumOcenjivanja;

	private Boolean zakljucenaOcena;

	
	public PredmetiOceneUcenikaDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public PredmetiOceneUcenikaDTO(String imePrezimeUcenika, String jmbg, String razredOdeljenje, String predmet,
			Integer vrednostOcene, String tipOcene, String polugodiste, LocalDate datumOcenjivanja,
			Boolean zakljucenaOcena) {
		super();
		this.imePrezimeUcenika = imePrezimeUcenika;
		this.jmbg = jmbg;
		this.razredOdeljenje = razredOdeljenje;
		this.predmet = predmet;
		this.vrednostOcene = vrednostOcene;
		this.tipOcene = tipOcene;
		this.polugodiste = polugodiste;
		this.datumOcenjivanja = datumOcenjivanja;
		this.zakljucenaOcena = zakljucenaOcena;
	}



	public String getImePrezimeUcenika() {
		return imePrezimeUcenika;
	}

	public void setImePrezimeUcenika(String imePrezimeUcenika) {
		this.imePrezimeUcenika = imePrezimeUcenika;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public String getRazredOdeljenje() {
		return razredOdeljenje;
	}

	public void setRazredOdeljenje(String razredOdeljenje) {
		this.razredOdeljenje = razredOdeljenje;
	}

	public String getPredmet() {
		return predmet;
	}

	public void setPredmet(String predmet) {
		this.predmet = predmet;
	}

	public Integer getVrednostOcene() {
		return vrednostOcene;
	}

	public void setVrednostOcene(Integer vrednostOcene) {
		this.vrednostOcene = vrednostOcene;
	}

	public String getTipOcene() {
		return tipOcene;
	}

	public void setTipOcene(String tipOcene) {
		this.tipOcene = tipOcene;
	}

	public String getPolugodiste() {
		return polugodiste;
	}

	public void setPolugodiste(String polugodiste) {
		this.polugodiste = polugodiste;
	}

	public LocalDate getDatumOcenjivanja() {
		return datumOcenjivanja;
	}

	public void setDatumOcenjivanja(LocalDate datumOcenjivanja) {
		this.datumOcenjivanja = datumOcenjivanja;
	}

	public Boolean getZakljucenaOcena() {
		return zakljucenaOcena;
	}

	public void setZakljucenaOcena(Boolean zakljucenaOcena) {
		this.zakljucenaOcena = zakljucenaOcena;
	}

}
