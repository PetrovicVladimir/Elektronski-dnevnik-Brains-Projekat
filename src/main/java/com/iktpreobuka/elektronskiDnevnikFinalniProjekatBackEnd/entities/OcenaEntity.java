package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "OCENA")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OcenaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull(message = "Vrednost ocene mora imati vrednost.")
	@Min(value = 1, message = "Minimalna ocena je: {value}.")
	@Max(value = 5, message = "Maksimalna ocena je: {value}.")
	private Integer vrednost;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Grade type must not be null.")
	private ETipOcene tip;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Grade type must not be null.")
	private EPolugodiste polugodiste;
	
	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate datumOcenjivanja;

	@Column
	private Boolean zakljucenaOcena;
	
	@OneToMany(mappedBy = "ocena", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<DnevnikEntity> dnevnik = new ArrayList<DnevnikEntity>();

	@Version
	private Integer version;

	public OcenaEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVrednost() {
		return vrednost;
	}

	public void setVrednost(Integer vrednost) {
		this.vrednost = vrednost;
	}

	public ETipOcene getTip() {
		return tip;
	}

	public void setTip(ETipOcene tip) {
		this.tip = tip;
	}

	public EPolugodiste getPolugodiste() {
		return polugodiste;
	}

	public void setPolugodiste(EPolugodiste polugodiste) {
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

	public List<DnevnikEntity> getDnevnik() {
		return dnevnik;
	}

	public void setDnevnik(List<DnevnikEntity> dnevnik) {
		this.dnevnik = dnevnik;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
