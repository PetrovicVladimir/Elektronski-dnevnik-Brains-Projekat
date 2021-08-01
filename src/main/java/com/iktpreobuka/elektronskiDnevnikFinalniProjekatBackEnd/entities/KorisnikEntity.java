package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "korisnik")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class KorisnikEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "korisnik_id")
	private Integer id;

	@NotBlank(message = "Ne moze biti prazno. Ime je obavezno polje")
	@Size(min = 2, max = 15, message = "Ime mora biti duzine izmedju {min} i {max} karaktera.")
	private String ime;

	@NotBlank(message = "Ne moze biti prazno. Prezime je obavezno polje")
	@Size(min = 2, max = 15, message = "Prezime mora biti duzine izmedju {min} i {max}.")
	private String prezime;

	@NotBlank(message = "Ne moze biti prazno. Korisnicko ime je obavezno polje")
	@Size(min = 2, max = 15, message = "Korisnicko ime mora biti duzine izmedju {min} i {max}.")
	private String korisnickoIme;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Size(min = 4, max = 15, message = "Sifra mora biti duzine izmedju {min} i {max}.")
	private String sifra;


	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "uloga")
	private UlogaEntity uloga;

	@Version
	private Integer version;

	public KorisnikEntity(String ime, String prezime, String korisnickoIme, String sifra, UlogaEntity uloga) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.korisnickoIme = korisnickoIme;
		this.sifra = sifra;
		this.uloga = uloga;
	}

	public KorisnikEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	

	public UlogaEntity getUloga() {
		return uloga;
	}

	public void setUloga(UlogaEntity uloga) {
		this.uloga = uloga;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
