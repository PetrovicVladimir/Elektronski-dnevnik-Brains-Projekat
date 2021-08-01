package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers.util.RESTError;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.KorisnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UlogaEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.KorisnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.UlogaRepository;

@RestController
@RequestMapping(value = "/admin")
public class AdministratorController {

	@Autowired
	private KorisnikRepository korisnikRepository;

	@Autowired
	private UlogaRepository ulogaRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(AdministratorController.class);

	/***** PUT Izmeni ulogu korisnika *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/izmeni/{korisnikId}/{ulogaId}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmeniUloguKorisniku(@PathVariable Integer korisnikId, @PathVariable Integer ulogaId) {

		try {
			if (!ulogaRepository.findById(ulogaId).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(1, "Uloga nije nadjena!"), HttpStatus.NOT_FOUND);
			} else if (!korisnikRepository.findById(korisnikId).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(2, "Korisnik nije nadjen!"), HttpStatus.NOT_FOUND);
			}
			UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
			KorisnikEntity korisnik = korisnikRepository.findById(korisnikId).get();
			korisnik.setUloga(uloga);
			korisnikRepository.save(korisnik);

			LOGGER.info("Korisniku sa ID brojem: " + korisnik.getId() + " je promenjena uloga u: " + uloga.getIme());

			return new ResponseEntity<>(korisnik, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(3, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/***** GET - Ocitaj sve iz log fajla *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/logFile", method = RequestMethod.GET)
	public ResponseEntity<String> ocitajLogove() throws IOException{
		return new ResponseEntity<>(ocitajLogFilre(), HttpStatus.OK);
	}

	public String ocitajLogFilre() throws IOException {

		String file ="logs//spring-boot-logging.log";
	    String result = null;

	    try (DataInputStream reader = new DataInputStream(new FileInputStream(file))) {
			int nBytesToRead = reader.available();
			if(nBytesToRead > 0) {
			    byte[] bytes = new byte[nBytesToRead];
			    reader.read(bytes);
			    result = new String(bytes);
			}
		}
	    return result;
	}

}
