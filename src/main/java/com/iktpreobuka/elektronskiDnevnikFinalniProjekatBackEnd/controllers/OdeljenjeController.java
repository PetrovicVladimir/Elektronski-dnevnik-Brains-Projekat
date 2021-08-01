package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers.util.RESTError;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredmetEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.OdeljenjeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(path = "/odeljenje")
public class OdeljenjeController {

	@Autowired
	private OdeljenjeRepository odeljenjeRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(OdeljenjeController.class);

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSve() {
		LOGGER.info("Vracena sva odeljenja");
		return new ResponseEntity<>(odeljenjeRepository.findAll(), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiZaId(@PathVariable Integer id) {
		if (odeljenjeRepository.existsById(id)) {
			LOGGER.info("Pronadji odeljnje na osnovu ID: " + id);
			return new ResponseEntity<>(odeljenjeRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(1, "Trazeno odeljenje nije nadjeno."), HttpStatus.NOT_FOUND);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST, value = "/dodaj")
	public ResponseEntity<?> kreirajNovoOdeljenje(@Valid @RequestBody OdeljenjeEntity novoOdeljenje) {
		
		odeljenjeRepository.save(novoOdeljenje);
		LOGGER.info("Dodato novo odeljenje: " + novoOdeljenje.toString());
		return new ResponseEntity<>(novoOdeljenje, HttpStatus.OK);
	}

	// PUT Izmeni odeljenje
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/izmeni/{id}")
	public ResponseEntity<?> izmeniPredmet(@Valid @RequestBody OdeljenjeEntity promenaOdeljenje,
			@PathVariable Integer id, BindingResult result) {

		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(id).get();

		odeljenje.setRazred(promenaOdeljenje.getRazred());
		odeljenje.setBroj(promenaOdeljenje.getBroj());

		odeljenjeRepository.save(odeljenje);

		// logger.info(odeljenje.getName() + " : je promenjen.");

		return new ResponseEntity<>(odeljenje, HttpStatus.OK);
	}

//
//
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		try {
			OdeljenjeEntity odeljenje = odeljenjeRepository.findById(id).get();
			odeljenjeRepository.delete(odeljenje);
			return new ResponseEntity<>(odeljenje, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Internal server error. Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
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
