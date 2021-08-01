package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "PREDMET")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PredmetEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	

//	@NotNull(message = "Nayiv predmeta mora imati ime.")
//	@Size(min = 5, max = 20, message = "Naziv kursa mora biti od {min} do {max} karaktera.")
	private String ime;
	
//	@NotBlank(message = "Fond casova ne moze biti prazan.")
//	@Min(value = 0, message = "Nedeljni fond casova ne sme biti manji od 0.")
//	@Max(value = 40, message = "Nedeljni fond casova mora biti preko 25.")
	private Integer fondCasova;
	
	@JsonIgnore
	@OneToMany(mappedBy = "predmet", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	protected List<PredavanjeEntity> predavanja = new ArrayList<>();
	
	@JsonIgnore
	@Version
	private Integer version;

	public PredmetEntity() {
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

	public Integer getFondCasova() {
		return fondCasova;
	}

	public void setFondCasova(Integer fondCasova) {
		this.fondCasova = fondCasova;
	}

	public List<PredavanjeEntity> getPredavanja() {
		return predavanja;
	}

	public void setPredavanja(List<PredavanjeEntity> predavanja) {
		this.predavanja = predavanja;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
