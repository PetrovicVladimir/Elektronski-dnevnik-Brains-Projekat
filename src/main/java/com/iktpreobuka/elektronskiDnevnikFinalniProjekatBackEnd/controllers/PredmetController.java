package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers;

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
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.KorisnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredmetEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UlogaEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.PredmetRepository;

@RestController
@RequestMapping(path = "/predmet")
public class PredmetController {

	@Autowired
	private PredmetRepository predmetRepository;
	
	private final Logger LOGGER = LoggerFactory.getLogger(PredmetController.class);

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSve() {
		LOGGER.info("Vraceni svi predmeti!");
		return new ResponseEntity<>(predmetRepository.findAll(), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiPredmeteZaId(@PathVariable Integer id) {
		try {
			if (predmetRepository.existsById(id)) {
				LOGGER.info("Vraceni predmet sa ID brojem " + id);
				return new ResponseEntity<>(predmetRepository.findById(id), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Predmet nije nadjen"), HttpStatus.NOT_FOUND);
		} catch (Exception e) { // u slucaju izuzetka vratiti 500
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// POST Dodaj novi predmet
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST, path = "/dodaj")
	public ResponseEntity<?> dodajNoviPredmet(@RequestBody PredmetEntity noviPredmet, BindingResult result) {

		

		PredmetEntity predmet = new PredmetEntity();

		predmet.setIme(noviPredmet.getIme());
		predmet.setFondCasova(noviPredmet.getFondCasova());

		predmetRepository.save(predmet);

		LOGGER.info(predmet.getIme() + " : je kreiran!.");

		return new ResponseEntity<>(predmet, HttpStatus.OK);
	}

	// PUT Izmeni predmet
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/izmeni/{predmetId}")
	public ResponseEntity<?> izmeniPredmet(@Valid @RequestBody PredmetEntity promenaPredmet, @PathVariable Integer predmetId) {

		if (!predmetRepository.findById(predmetId).isPresent()) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Predmet nije nadjen!"), HttpStatus.NOT_FOUND);
		}
			
		PredmetEntity predmet = predmetRepository.findById(predmetId).get();

		predmet.setIme(promenaPredmet.getIme());
		predmet.setFondCasova(promenaPredmet.getFondCasova());

		predmetRepository.save(predmet);

		LOGGER.info(predmet.getIme() + " : je izmenjen!.");

		return new ResponseEntity<>(predmet, HttpStatus.OK);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		try {
			PredmetEntity predmet = predmetRepository.findById(id).get();
			predmetRepository.delete(predmet);
			LOGGER.info(predmet.getIme() + " : je obrisan!.");
			return new ResponseEntity<>(predmet, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Internal server error. Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
