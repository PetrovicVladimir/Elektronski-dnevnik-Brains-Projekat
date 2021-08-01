package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers.util.RESTError;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.KorisnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UcenikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UlogaEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.UlogaRepository;

@RestController
@RequestMapping(path = "/uloga")
public class UlogaController {

	@Autowired
	private UlogaRepository ulogaRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(UlogaController.class);

	/***** GET - Vrati sve uloge *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		LOGGER.info("Vracene sve usluge.");
		return new ResponseEntity<>(ulogaRepository.findAll(), HttpStatus.OK);
	}

	/***** GET - Vrati vrati ulogu na osnovu ID *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getUlogaById(@PathVariable Integer id) {
		try {
			if (!ulogaRepository.findById(id).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(1, "Uloga nije nadjena!"), HttpStatus.BAD_REQUEST);
			}
			UlogaEntity uloga = ulogaRepository.findById(id).get();

			LOGGER.info("Vracena uloga sa ID brojem: " + id);
			return new ResponseEntity<>(uloga, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/***** POST - Dodaj novu ulogu *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST, path = "/dodaj")
	public ResponseEntity<?> dodajUlogu(@Valid @RequestBody UlogaEntity novaUloga) {

		LOGGER.info("Dodata je nova uloga!");
		return new ResponseEntity<>(ulogaRepository.save(novaUloga), HttpStatus.OK);
	}

	/***** PUT - Izmeni ulogu *****/
	@RequestMapping(method = RequestMethod.PUT, path = "/izmeni/{ulogaId}")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<?> izmeniUlogu(@PathVariable Integer ulogaId, @Valid @RequestBody UlogaEntity novaUloga) {
		try {
			if (!ulogaRepository.findById(ulogaId).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(1, "Uloga nije nadjena!"), HttpStatus.BAD_REQUEST);
			}
			UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
			uloga.setIme(novaUloga.getIme());
			ulogaRepository.save(uloga);
			LOGGER.info("Izmenjena je uloga sa ID brojem: " + ulogaId);
			return new ResponseEntity<>(uloga, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/***** DELETE - Obrisi ulogu *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{ulogaId}")
	public ResponseEntity<?> obrisiUlogu(@PathVariable Integer ulogaId) {
		try {
			if (!ulogaRepository.findById(ulogaId).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(1, "Uloga nije nadjena!"), HttpStatus.BAD_REQUEST);
			}
			UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
			ulogaRepository.delete(uloga);

			LOGGER.info("Obrisana je uloga: " + ulogaId);

			return new ResponseEntity<>(uloga, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Validacija
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}
