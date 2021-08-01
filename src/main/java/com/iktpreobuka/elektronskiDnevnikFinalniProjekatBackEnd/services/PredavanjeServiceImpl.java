package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers.PredavanjeController;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.NastavnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredavanjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredmetEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.PredavanjaPregled;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.NastavnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.PredavanjeRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.PredmetRepository;

@Service
public class PredavanjeServiceImpl implements PredavanjeService {

	@Autowired
	private PredmetRepository predmetRepository;
	
	@Autowired
	private NastavnikRepository nastavnikRepository;
	
	@Autowired
	private PredavanjeRepository predavanjeRepository;
	
	private final Logger LOGGER = LoggerFactory.getLogger(PredavanjeServiceImpl.class);
	
	@Override
	public List<PredmetEntity> izlistajPredmeteNastavnika(Integer nastavnikId) {
		
		NastavnikEntity nastavnik = nastavnikRepository.findById(nastavnikId).get();
		List<PredavanjeEntity> predavanje = predavanjeRepository.findAllByNastavnik(nastavnik);

		List<PredmetEntity> pregledPredmeta = new ArrayList<PredmetEntity>();

		for (PredavanjeEntity predavanjeEntity : predavanje) {
			PredmetEntity predmet = new PredmetEntity();
			predmet.setId(predavanjeEntity.getPredmet().getId());
			predmet.setIme(predavanjeEntity.getPredmet().getIme());
			predmet.setFondCasova(predavanjeEntity.getPredmet().getFondCasova());
			pregledPredmeta.add(predmet);
		}
		LOGGER.info("Vracene svi predmeti i fondovi casova predmeta za nastavnika sa ID brojem: " + nastavnikId);
		return pregledPredmeta;

	}

	@Override
	public List<NastavnikEntity> vratiNastavnikePredmeta(Integer predmetId) {
		PredmetEntity predmet = predmetRepository.findById(predmetId).get();
		List<PredavanjeEntity> predavanje = predavanjeRepository.findAllByPredmet(predmet);
		
		List<NastavnikEntity> pregledNastavnika = new ArrayList<NastavnikEntity>();
		
		for (PredavanjeEntity predavanjeEntity : predavanje) {
			NastavnikEntity nastavnik = new NastavnikEntity();
			nastavnik.setId(predavanjeEntity.getNastavnik().getId());
			nastavnik.setIme(predavanjeEntity.getNastavnik().getIme());
			nastavnik.setPrezime(predavanjeEntity.getNastavnik().getPrezime());
			nastavnik.setEmail(predavanjeEntity.getNastavnik().getEmail());
			nastavnik.setKorisnickoIme(predavanjeEntity.getNastavnik().getKorisnickoIme());
			
			pregledNastavnika.add(nastavnik);
		}
		LOGGER.info("Vracene svi nastavnici za ID broj predmeta: " + predmetId);
		return pregledNastavnika;
	}

	@Override
	public List<PredavanjaPregled> spisakPredavanjaNastavnika(Integer nastavnikId) {
		NastavnikEntity nastavnik = nastavnikRepository.findById(nastavnikId).get();
		List<PredavanjeEntity> predavanje = predavanjeRepository.findAllByNastavnik(nastavnik);
		List<PredavanjaPregled> pregledPredavanja = new ArrayList<>();

		for (PredavanjeEntity predavanjeEntity : predavanje) {
			PredavanjaPregled pe = new PredavanjaPregled();

			pe.setId(predavanjeEntity.getId());
			pe.setNaziv(predavanjeEntity.getNaziv());
			pe.setNastavnik(
					predavanjeEntity.getNastavnik().getIme() + " " + predavanjeEntity.getNastavnik().getPrezime());
			pe.setPredmet(predavanjeEntity.getPredmet().getIme());
			pe.setFondCasova(predavanjeEntity.getPredmet().getFondCasova());
			pregledPredavanja.add(pe);
		}
		return pregledPredavanja;
	}

	@Override
	public PredavanjeEntity dodajNovoPredavanje(Integer predmetId, Integer nastavnikId) {
		PredmetEntity predmet = predmetRepository.findById(predmetId).get();
		NastavnikEntity nastavnik = nastavnikRepository.findById(nastavnikId).get();

		PredavanjeEntity predavanje = new PredavanjeEntity();

		predavanje.setNastavnik(nastavnik);
		predavanje.setPredmet(predmet);
		predavanje.setNaziv(predmet.getIme().toUpperCase()); // Postavlejno da se naziv predavanja odnosi na ime
																// predmeta

		predavanjeRepository.save(predavanje);

		LOGGER.info(predavanje.getNaziv() + " : je kreirano.");

		return predavanje;

	}

	@Override
	public PredavanjeEntity izmeniPredavanje(Integer predavanjeId, Integer predmetId, Integer nastavnikId) {
		PredmetEntity predmet = predmetRepository.findById(predmetId).get();
		NastavnikEntity nastavnik = nastavnikRepository.findById(nastavnikId).get();
		PredavanjeEntity predavanje = predavanjeRepository.findById(predavanjeId).get();
		
		predavanje.setNastavnik(nastavnik);
		predavanje.setPredmet(predmet);
		predavanje.setNaziv(predmet.getIme().toUpperCase());
		
		predavanjeRepository.save(predavanje);

		LOGGER.info("Predavanje sa ID brojem : " + predavanje.getId() + " : je izmenjeno.");

		return predavanje;
	}

}
