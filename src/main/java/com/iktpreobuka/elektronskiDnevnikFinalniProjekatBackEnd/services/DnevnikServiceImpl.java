package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.DnevnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.OcenaEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredavanjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UcenikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.PredmetiOceneUcenikaDTO;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.models.EmailObject;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.DnevnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.OcenaRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.PredavanjeRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.UcenikRepository;

@Service
public class DnevnikServiceImpl implements DnevnikService {

	@Autowired
	private DnevnikRepository dnevnikRepository;

	@Autowired
	private UcenikRepository ucenikRepository;
	

	@Autowired
	private PredavanjeRepository predavanjeRepository;

	@Autowired
	private OcenaRepository ocenaRepository;

	@Autowired
	private EmailService emailService;
	
	private final Logger LOGGER = LoggerFactory.getLogger(DnevnikServiceImpl.class);

	@Override
	public List<PredmetiOceneUcenikaDTO> vratiOceneUcenikaUceniku(Integer ucenikId) {

		UcenikEntity ucenik = ucenikRepository.findById(ucenikId).get();

		List<DnevnikEntity> dnevnik = dnevnikRepository.findAllByUcenik(ucenik);

		List<PredmetiOceneUcenikaDTO> pregled = new ArrayList<PredmetiOceneUcenikaDTO>();

		for (DnevnikEntity dnevnikEntity : dnevnik) {
			PredmetiOceneUcenikaDTO pou = new PredmetiOceneUcenikaDTO();
			pou.setImePrezimeUcenika(dnevnikEntity.getUcenik().getIme() + " " + dnevnikEntity.getUcenik().getPrezime());
			pou.setJmbg(dnevnikEntity.getUcenik().getJMBG());
			pou.setPredmet(dnevnikEntity.getPredavanje().getPredmet().getIme());
			pou.setVrednostOcene(dnevnikEntity.getOcena().getVrednost());
			pou.setPolugodiste(dnevnikEntity.getOcena().getPolugodiste().toString());
			pou.setZakljucenaOcena(dnevnikEntity.getOcena().getZakljucenaOcena());
			pou.setTipOcene(dnevnikEntity.getOcena().getTip().toString());
			pou.setDatumOcenjivanja(dnevnikEntity.getOcena().getDatumOcenjivanja());
			pou.setRazredOdeljenje(dnevnikEntity.getUcenik().getPohadjaRazred().getRazred() + "-"
					+ dnevnikEntity.getUcenik().getPohadjaRazred().getBroj());
			pregled.add(pou);
		}

		LOGGER.info("Vracene sve ocene za ucenika sa ID brojem: " + ucenikId);
		return pregled;
	}

	@Override
	public List<PredmetiOceneUcenikaDTO> vratiOceneUcenikaRoditelju(Integer ucenikId, Integer roditeljId) {
		UcenikEntity ucenik = ucenikRepository.findById(ucenikId).get();
		List<DnevnikEntity> dnevnik = dnevnikRepository.findAllByUcenik(ucenik);
		List<PredmetiOceneUcenikaDTO> pregled = new ArrayList<PredmetiOceneUcenikaDTO>();

		for (DnevnikEntity dnevnikEntity : dnevnik) {
			if (dnevnikEntity.getUcenik().getRoditelj().getId().equals(roditeljId)) {
				PredmetiOceneUcenikaDTO pou = new PredmetiOceneUcenikaDTO();
				pou.setImePrezimeUcenika(dnevnikEntity.getUcenik().getIme() + " " + dnevnikEntity.getUcenik().getPrezime());
				pou.setJmbg(dnevnikEntity.getUcenik().getJMBG());
				pou.setPredmet(dnevnikEntity.getPredavanje().getPredmet().getIme());
				pou.setVrednostOcene(dnevnikEntity.getOcena().getVrednost());
				pou.setPolugodiste(dnevnikEntity.getOcena().getPolugodiste().toString());
				pou.setZakljucenaOcena(dnevnikEntity.getOcena().getZakljucenaOcena());
				pou.setTipOcene(dnevnikEntity.getOcena().getTip().toString());
				pou.setDatumOcenjivanja(dnevnikEntity.getOcena().getDatumOcenjivanja());
				pou.setRazredOdeljenje(dnevnikEntity.getUcenik().getPohadjaRazred().getRazred() + "-"
						+ dnevnikEntity.getUcenik().getPohadjaRazred().getBroj());
				pregled.add(pou);
			}
		}

		LOGGER.info("Vracene sve ocene za ucenika sa ID brojem: " + ucenikId);
		return pregled;
	}

	@Override
	public List<PredmetiOceneUcenikaDTO> vratiOceneUcenikaIzPredmeta(Integer ucenikId, String predmet) {
		UcenikEntity ucenik = ucenikRepository.findById(ucenikId).get();

		List<DnevnikEntity> dnevnik = dnevnikRepository.findAllByUcenik(ucenik);

		List<PredmetiOceneUcenikaDTO> pregled = new ArrayList<PredmetiOceneUcenikaDTO>();

		for (DnevnikEntity dnevnikEntity : dnevnik) {
			if (dnevnikEntity.getPredavanje().getPredmet().getIme().equalsIgnoreCase(predmet)) {
				PredmetiOceneUcenikaDTO pou = new PredmetiOceneUcenikaDTO();
				pou.setImePrezimeUcenika(dnevnikEntity.getUcenik().getIme() + " " + dnevnikEntity.getUcenik().getPrezime());
				pou.setJmbg(dnevnikEntity.getUcenik().getJMBG());
				pou.setPredmet(dnevnikEntity.getPredavanje().getPredmet().getIme());
				pou.setVrednostOcene(dnevnikEntity.getOcena().getVrednost());
				pou.setPolugodiste(dnevnikEntity.getOcena().getPolugodiste().toString());
				pou.setZakljucenaOcena(dnevnikEntity.getOcena().getZakljucenaOcena());
				pou.setTipOcene(dnevnikEntity.getOcena().getTip().toString());
				pou.setDatumOcenjivanja(dnevnikEntity.getOcena().getDatumOcenjivanja());
				pou.setRazredOdeljenje(dnevnikEntity.getUcenik().getPohadjaRazred().getRazred() + "-"
						+ dnevnikEntity.getUcenik().getPohadjaRazred().getBroj());
				pregled.add(pou);
			}
		}

		LOGGER.info("Vracene sve ocene za ucenika sa ID brojem: " + ucenikId);
		return pregled;
	}

	@Override
	public DnevnikEntity obavestavanjeRoditelja(Integer ucenikId, Integer predavanjeId, Integer ocenaId) {
		UcenikEntity ucenik = ucenikRepository.findById(ucenikId).get();
		PredavanjeEntity predavanje = predavanjeRepository.findById(predavanjeId).get();
		OcenaEntity ocena = ocenaRepository.findById(ocenaId).get();

		EmailObject emailObject = new EmailObject();

		emailObject.setTo(ucenik.getRoditelj().getEmail());
		emailObject.setSubject("Nova ocena dodata: " + predavanje.getPredmet().getIme());

		emailObject.setText("Poštovani roditelju," + System.lineSeparator() +  "Vaše dete " + ucenik.getIme() + " " + ucenik.getPrezime() + " je datuma: "
				+ ocena.getDatumOcenjivanja().toString() + " iz predmeta " + predavanje.getNaziv() + " dobilo ocenu "
				+ ocena.getVrednost() + " za " + ocena.getTip() + " od strane nastavnika " + predavanje.getNastavnik().getIme()
				+ " " + predavanje.getNastavnik().getPrezime() + System.lineSeparator() + "Srdačan pozdrav");

		emailService.sendSimpleMessage(emailObject);

		DnevnikEntity dnevnik = new DnevnikEntity();

		dnevnik.setUcenik(ucenik);
		dnevnik.setPredavanje(predavanje);
		dnevnik.setOcena(ocena);

		dnevnikRepository.save(dnevnik);

		LOGGER.info("U dnevnik uneti podaci o uceniku, predavanju i oceni pod ID brojem: " + dnevnik.getId() + ""
				+ " Obavestenje poslato roditelju ucenika na email: " + ucenik.getRoditelj().getEmail());

		return dnevnik;
	}

	@Override
	public List<PredmetiOceneUcenikaDTO> vratiOceneNastavnikaDatihUcenikuZaPredavanjeKojiPredaje(Integer predavanjeId) {
		PredavanjeEntity predavanje = predavanjeRepository.findById(predavanjeId).get();
		List<DnevnikEntity> dnevnik = dnevnikRepository.findAllByPredavanje(predavanje);

		List<PredmetiOceneUcenikaDTO> pregled = new ArrayList<PredmetiOceneUcenikaDTO>();

		for (DnevnikEntity dnevnikEntity : dnevnik) {
			PredmetiOceneUcenikaDTO pou = new PredmetiOceneUcenikaDTO();
			pou.setImePrezimeUcenika(
					dnevnikEntity.getUcenik().getIme() + " " + dnevnikEntity.getUcenik().getPrezime());
			pou.setJmbg(dnevnikEntity.getUcenik().getJMBG());
			pou.setPredmet(dnevnikEntity.getPredavanje().getPredmet().getIme());
			pou.setVrednostOcene(dnevnikEntity.getOcena().getVrednost());
			pou.setPolugodiste(dnevnikEntity.getOcena().getPolugodiste().toString());
			pou.setZakljucenaOcena(dnevnikEntity.getOcena().getZakljucenaOcena());
			pou.setTipOcene(dnevnikEntity.getOcena().getTip().toString());
			pou.setDatumOcenjivanja(dnevnikEntity.getOcena().getDatumOcenjivanja());
			pou.setRazredOdeljenje(dnevnikEntity.getUcenik().getPohadjaRazred().getRazred() + "-"
					+ dnevnikEntity.getUcenik().getPohadjaRazred().getBroj());
			pregled.add(pou);
		}

		LOGGER.info("Vracene sve ocene nastavnika za njegovo predavanje sa ID brojem: " + predavanjeId);
		return pregled;
	}

	@Override
	public List<PredmetiOceneUcenikaDTO> OceneDatieUcenikucimaZaPredavanjeIPolugosite(Integer predavanjeId,
			String polugodiste) {
		PredavanjeEntity predavanje = predavanjeRepository.findById(predavanjeId).get();
		List<DnevnikEntity> dnevnik = dnevnikRepository.findAllByPredavanje(predavanje);
		
		List<PredmetiOceneUcenikaDTO> pregled = new ArrayList<PredmetiOceneUcenikaDTO>();

		for (DnevnikEntity dnevnikEntity : dnevnik) {
			PredmetiOceneUcenikaDTO pou = new PredmetiOceneUcenikaDTO();
			if (dnevnikEntity.getOcena().getPolugodiste().toString().equalsIgnoreCase(polugodiste)) {
				pou.setImePrezimeUcenika(dnevnikEntity.getUcenik().getIme() + " " + dnevnikEntity.getUcenik().getPrezime());
				pou.setJmbg(dnevnikEntity.getUcenik().getJMBG());
				pou.setPredmet(dnevnikEntity.getPredavanje().getPredmet().getIme());
				pou.setVrednostOcene(dnevnikEntity.getOcena().getVrednost());
				pou.setPolugodiste(dnevnikEntity.getOcena().getPolugodiste().toString());
				pou.setZakljucenaOcena(dnevnikEntity.getOcena().getZakljucenaOcena());
				pou.setTipOcene(dnevnikEntity.getOcena().getTip().toString());
				pou.setDatumOcenjivanja(dnevnikEntity.getOcena().getDatumOcenjivanja());
				pou.setRazredOdeljenje(dnevnikEntity.getUcenik().getPohadjaRazred().getRazred() + "-"
						+ dnevnikEntity.getUcenik().getPohadjaRazred().getBroj());
				pregled.add(pou);
			}

		}
		LOGGER.info("Vracene sve ocene nastavnika za " +  polugodiste  + " polugodiste za predavanje sa ID brojem: " + predavanjeId);
		return pregled;
	}

	@Override
	public DnevnikEntity izmeniPredmet(Integer dnevnikid, Integer predavanjeId, Integer ucenikId, Integer ocenaId) {
		DnevnikEntity dnevnik = dnevnikRepository.findById(dnevnikid).get();
		UcenikEntity ucenik = ucenikRepository.findById(ucenikId).get();
		PredavanjeEntity predavanje = predavanjeRepository.findById(predavanjeId).get();
		OcenaEntity ocena = ocenaRepository.findById(ocenaId).get();

		dnevnik.setUcenik(ucenik);
		dnevnik.setPredavanje(predavanje);
		dnevnik.setOcena(ocena);

		dnevnikRepository.save(dnevnik);

		LOGGER.info("U dnevniku izmenjeni podaci o uceniku i predavanju i oceni za ID: " + dnevnik.getId());

		return dnevnik;
	}

	@Override
	public DnevnikEntity deleteDnevnik(Integer id) {
		DnevnikEntity dnevnik = dnevnikRepository.findById(id).get();
		dnevnikRepository.delete(dnevnik);
		LOGGER.info("Obrisano za ID: " + dnevnik.getId());
		return dnevnik;
	}

}
