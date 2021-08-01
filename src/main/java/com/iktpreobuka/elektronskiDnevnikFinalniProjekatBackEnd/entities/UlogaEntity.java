package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "uloga")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UlogaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "uloga_id")
	private Integer id;
	
	@NotBlank(message = "Naziv uloge ne moze biti prazan.")
	@Column(name = "uloga_ime")
	private String ime;
	
	@OneToMany(mappedBy = "uloga", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH })
	@JsonIgnore
	private List<KorisnikEntity> users = new ArrayList<>();

	public UlogaEntity() {
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

	public List<KorisnikEntity> getUsers() {
		return users;
	}

	public void setUsers(List<KorisnikEntity> users) {
		this.users = users;
	}
}
