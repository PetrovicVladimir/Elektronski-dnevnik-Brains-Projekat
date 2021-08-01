package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RoditeljDTO {

	@NotBlank(message = "Ne moze biti prazno. Ime je obavezno polje")
	@Size(min = 2, max = 15, message = "Ime mora biti duzine izmedju {min} i {max} karaktera.")
	private String ime;

	@NotBlank(message = "Ne moze biti prazno. Prezime je obavezno polje")
	@Size(min = 2, max = 15, message = "Ime mora biti duzine izmedju {min} i {max} karaktera.")
	private String prezime;

	@NotBlank(message = "Ne moze biti prazno. Korisnicko ime je obavezno polje")
	@Size(min = 2, max = 15, message = "Ime mora biti duzine izmedju {min} i {max} karaktera.")
	private String korisnickoIme;

	@NotBlank(message = "Ne moze biti prazno. Sifra je obavezno polje")
	@Size(min = 4, max = 15, message = "Ime mora biti duzine izmedju {min} i {max} karaktera.")
	private String sifra;
	
	@Email(message = "Email adresa mora biti ispravno formatirana! Primer: abcd@efg.ih")
	private String email;
	
	
	public RoditeljDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
