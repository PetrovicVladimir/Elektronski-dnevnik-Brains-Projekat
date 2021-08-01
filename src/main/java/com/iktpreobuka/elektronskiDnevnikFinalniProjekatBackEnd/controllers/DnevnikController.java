package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers.util.RESTError;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.DnevnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.NastavnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.OcenaEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredavanjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.RoditeljEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UcenikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.PredmetiOceneUcenikaDTO;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.models.EmailObject;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.DnevnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.NastavnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.OcenaRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.PredavanjeRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.RoditeljRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.UcenikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.services.DnevnikService;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.services.EmailService;

@RestController
@RequestMapping(path = "/dnevnik")
public class DnevnikController {

	@Autowired
	private DnevnikRepository dnevnikRepository;

	@Autowired
	private UcenikRepository ucenikRepository;

	@Autowired
	private PredavanjeRepository predavanjeRepository;

	@Autowired
	private OcenaRepository ocenaRepository;

	@Autowired
	private DnevnikService dnevnikService;

	@Autowired
	private EmailService emailService;

	private final Logger LOGGER = LoggerFactory.getLogger(DnevnikController.class);

	/***** GET Vrati sve *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		LOGGER.info("Vraceni su svi podaci iz dnevnika.");
		return new ResponseEntity<>(dnevnikRepository.findAll(), HttpStatus.OK);
	}

	/***** GET Vrati sve na osnovu ID broja *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/{dnevnikId}")
	public ResponseEntity<?> vratiDnevnikZaId(@PathVariable Integer dnevnikId) {
		try {
			if (!dnevnikRepository.findById(dnevnikId).isPresent()) {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Dnevnik nije nadjen!"), HttpStatus.NOT_FOUND);
			}
			DnevnikEntity dnevnik = dnevnikRepository.findById(dnevnikId).get();
			LOGGER.info("Vracen red dnevnik sa ID brojem: " + dnevnikId);
			return new ResponseEntity<>(dnevnik, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/***** GET Vrati sve ocene ucenika na osnovu ID broja ucenika *****/
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK", "ROLE_UCENIK"})
	@RequestMapping(method = RequestMethod.GET, value = "/oceneStudentu/{ucenikId}")
	public ResponseEntity<?> vratiOceneUcenikaUceniku(@PathVariable Integer ucenikId) {
		try {
			if (!ucenikRepository.findById(ucenikId).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ucenik nije nadjen!"),
						HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(dnevnikService.vratiOceneUcenikaUceniku(ucenikId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*****
	 * GET Vrati sve ocene ucenika roditelju. Roditelj dobija sve ocene za učenika
	 * vezanog za sebe
	 *****/
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK", "ROLE_RODITELJ"})
	@RequestMapping(method = RequestMethod.GET, value = "/oceneDeceOdRoditelja/{ucenikId}/roditelj/{roditeljId}")
	public ResponseEntity<?> vratiOceneUcenikaRoditelju(@PathVariable Integer ucenikId,
			@PathVariable Integer roditeljId) {
		try {
			if (!ucenikRepository.findById(ucenikId).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ucenik nije nadjen!"),
						HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(dnevnikService.vratiOceneUcenikaRoditelju(ucenikId, roditeljId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/***** GET Vrati sve ocene ucenika iz odredjenog predmeta *****/
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK", "ROLE_RODITELJ"})
	@RequestMapping(method = RequestMethod.GET, value = "/oceneUcenikaIzPredmeta/{ucenikId}/{predmet}")
	public ResponseEntity<?> vratiOceneUcenikaIzPredmeta(@PathVariable Integer ucenikId, @PathVariable String predmet) {
		try {
			if (!ucenikRepository.findById(ucenikId).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ucenik nije nadjen!"),
						HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(dnevnikService.vratiOceneUcenikaIzPredmeta(ucenikId, predmet), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*****
	 * POST Dodaj u dnevnik ocenu, predavanje i ocenu. Slanje obevestenja na email
	 * roditelju za ocenjenog ucenika
	 *****/
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK"})
	@RequestMapping(method = RequestMethod.POST, path = "/dodajOcenuEmail")
	public ResponseEntity<?> obavestavanjeRoditelja(@RequestParam Integer ucenikId, @RequestParam Integer predavanjeId,
			@RequestParam Integer ocenaId) {
		try {
			if (!predavanjeRepository.findById(predavanjeId).isPresent()) {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Predavanje nije nadjeno!"), HttpStatus.NOT_FOUND);
			}
			if (!ucenikRepository.findById(ucenikId).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ucenik nije nadjen!"),
						HttpStatus.NOT_FOUND);
			}
			if (!ocenaRepository.findById(ocenaId).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ocena nije nadjena!"),
						HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(dnevnikService.obavestavanjeRoditelja(ucenikId, predavanjeId, ocenaId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*****
	 * GET - Nastavnik vidi ocene svih svojih predmeta za ucenike i predmete kojima
	 * predaje
	 *****/
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK"})
	@RequestMapping(method = RequestMethod.GET, value = "/oceneUcenikaZaPredavanje/{predavanjeId}")
	public ResponseEntity<?> vratiOceneNastavnikaDatihUcenikuZaPredavanjeKojiPredaje(
			@PathVariable Integer predavanjeId) {
		try {
			if (!predavanjeRepository.findById(predavanjeId).isPresent()) {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Predavanje nije nadjeno!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(
					dnevnikService.vratiOceneNastavnikaDatihUcenikuZaPredavanjeKojiPredaje(predavanjeId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/***** GET - Ocene iz predavanja za odredjeno polugodiste *****/
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK"})
	@RequestMapping(method = RequestMethod.GET, value = "/oceneUcenikaZaPredavanjePolugodiste/{predavanjeId}/{polugodiste}")
	public ResponseEntity<?> OceneDatieUcenikucimaZaPredavanjeIPolugosite(@PathVariable Integer predavanjeId,
			@PathVariable String polugodiste) {
		try {
			if (!predavanjeRepository.findById(predavanjeId).isPresent()) {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Predavanje nije nadjeno!"), HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(
					dnevnikService.OceneDatieUcenikucimaZaPredavanjeIPolugosite(predavanjeId, polugodiste),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	/*****
//	 * POST Dodaj u dnevnik predavanje, ucenika i ocenu bez obaveštenja roditelja
//	 *****/
//	@RequestMapping(method = RequestMethod.POST, path = "/dodaj")
//	public ResponseEntity<?> dodajPredavanjeUcenika(@RequestParam Integer ucenikId, @RequestParam Integer predavanjeId,
//			@RequestParam Integer ocenaId) {
//
//		try {
//			UcenikEntity ucenik = ucenikRepository.findById(ucenikId).get();
//			PredavanjeEntity predavanje = predavanjeRepository.findById(predavanjeId).get();
//			OcenaEntity ocena = ocenaRepository.findById(ocenaId).get();
//
//			DnevnikEntity dnevnik = new DnevnikEntity();
//
//			dnevnik.setUcenik(ucenik);
//			dnevnik.setPredavanje(predavanje);
//			dnevnik.setOcena(ocena);
//
//			dnevnikRepository.save(dnevnik);
//
//			LOGGER.info("U dnevnik uneti podaci o uceniku, predavanju i oceni: " + dnevnik.getId()
//					+ " bez slanja email obavestenja");
//
//			return new ResponseEntity<>(dnevnik, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
//					HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//	}

	/***** PUT izmeni ocenu, predavanje i ucenika *****/
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.PUT, path = "/izmeni/{dnevnikid}")
	public ResponseEntity<?> izmeniPredmet(@PathVariable Integer dnevnikid, @RequestParam Integer predavanjeId,
			@RequestParam Integer ucenikId, @RequestParam Integer ocenaId) {
		try {
			if (!dnevnikRepository.findById(dnevnikid).isPresent()) {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Dnevnik nije nadjen!"), HttpStatus.NOT_FOUND);
			}
			if (!predavanjeRepository.findById(predavanjeId).isPresent()) {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Predavanje nije nadjeno!"), HttpStatus.NOT_FOUND);
			}
			if (!ucenikRepository.findById(ucenikId).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ucenik nije nadjen!"),
						HttpStatus.NOT_FOUND);
			}
			if (!ocenaRepository.findById(ocenaId).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ocena nije nadjena!"),
						HttpStatus.NOT_FOUND);
			}
			
			return new ResponseEntity<>(dnevnikService.izmeniPredmet(dnevnikid, predavanjeId, ucenikId, ocenaId), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/***** DELETE *****/
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteDnevnik(@PathVariable Integer id) {
		try {
			if (!dnevnikRepository.findById(id).isPresent()) {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Dnevnik nije nadjen!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(dnevnikService.deleteDnevnik(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Internal server error. Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
