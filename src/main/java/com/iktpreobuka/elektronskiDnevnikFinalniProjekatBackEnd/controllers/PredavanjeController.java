package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers.util.RESTError;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.DnevnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.KorisnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.NastavnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredavanjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredmetEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.PredavanjaPregled;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.PredmetiOceneUcenikaDTO;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.NastavnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.PredavanjeRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.PredmetRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.services.PredavanjeService;

@RestController
@RequestMapping(path = "/predavanje")
public class PredavanjeController {

	@Autowired
	private PredmetRepository predmetRepository;

	@Autowired
	private NastavnikRepository nastavnikRepository;

	@Autowired
	private PredavanjeRepository predavanjeRepository;

	@Autowired
	private PredavanjeService predavanjeService;

	private final Logger LOGGER = LoggerFactory.getLogger(PredavanjeController.class);

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSve() {
		LOGGER.info("Vraceni sva predavanja");
		return new ResponseEntity<>(predavanjeRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK"})
	public ResponseEntity<?> vratiPredavanjeZaId(@PathVariable Integer id) {
		try {
			if (!predavanjeRepository.findById(id).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(1, "User not found"), HttpStatus.BAD_REQUEST);
			}
			PredavanjeEntity predavanje = predavanjeRepository.findById(id).get();
			LOGGER.info(predavanje.getNaziv() + " : je vraceno!.");
			return new ResponseEntity<>(predavanje, HttpStatus.OK);
		} catch (Exception e) { // u slucaju izuzetka vratiti 500
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/***** GET - Izlistaj sve predmete nastavnika *****/
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK"})
	@RequestMapping(method = RequestMethod.GET, value = "/predmetiNastavnika/{nastavnikId}")
	public ResponseEntity<?> izlistajPredmeteNastavnika(@PathVariable Integer nastavnikId) {

		if (!nastavnikRepository.findById(nastavnikId).isPresent()) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik nije nadjen!"),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(predavanjeService.izlistajPredmeteNastavnika(nastavnikId), HttpStatus.OK);
	}

	/***** GET - Izlistaj sve nastavnike predmeta *****/
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK"})
	@RequestMapping(method = RequestMethod.GET, value = "/nastavniciPredmeta/{predmetId}")
	public ResponseEntity<?> vratiNastavnikePredmeta(@PathVariable Integer predmetId) {
		if (!predmetRepository.findById(predmetId).isPresent()) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Predmet nije nadjen!"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(predavanjeService.vratiNastavnikePredmeta(predmetId), HttpStatus.OK);
	}

	/***** GET - Spisak predavanja nastavnika *****/
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK"})
	@RequestMapping(method = RequestMethod.GET, value = "/spisakPredavanjaNastavnika/{nastavnikId}")
	public ResponseEntity<?> spisakPredavanjaNastavnika(@PathVariable Integer nastavnikId) {
		if (!nastavnikRepository.findById(nastavnikId).isPresent()) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik nije nadjen!"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(predavanjeService.spisakPredavanjaNastavnika(nastavnikId), HttpStatus.OK);
	}

	// POST Dodaj novo predavanje(predmet + nastavnik)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST, path = "/dodaj")
	public ResponseEntity<?> dodajNovoPredavanje(@RequestParam Integer predmetId, @RequestParam Integer nastavnikId) {
		if (!nastavnikRepository.findById(nastavnikId).isPresent()) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik nije nadjen!"),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(predavanjeService.dodajNovoPredavanje(predmetId, nastavnikId), HttpStatus.OK);
	}

	// PUT Izmeni predmet ili nastavnika za predavanje
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/izmeni/{predavanjeId}")
	public ResponseEntity<?> izmeniPredavanje(@PathVariable Integer predavanjeId, @RequestParam Integer predmetId, @RequestParam Integer nastavnikId) {
		if (!predavanjeRepository.findById(predavanjeId).isPresent()) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Predavanje nije nadjeno!"),
					HttpStatus.NOT_FOUND);
		}
		if (!nastavnikRepository.findById(nastavnikId).isPresent()) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik nije nadjen!"),
					HttpStatus.NOT_FOUND);
		}
		if (!predmetRepository.findById(predmetId).isPresent()) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Predmet nije nadjen!"),
					HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(predavanjeService.izmeniPredavanje(predavanjeId, predmetId, nastavnikId), HttpStatus.OK);
	}

	/***** DELETE - Obrisi predavanje *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deletePredavanje(@PathVariable Integer id) {
		try {
			PredavanjeEntity predavanje = predavanjeRepository.findById(id).get();
			predavanjeRepository.delete(predavanje);
			LOGGER.info(predavanje.getId() + " : je obrisano!.");
			return new ResponseEntity<>(predavanje, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Internal server error. Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
