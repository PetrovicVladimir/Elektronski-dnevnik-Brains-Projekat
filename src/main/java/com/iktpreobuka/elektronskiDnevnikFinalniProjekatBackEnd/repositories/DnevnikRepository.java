package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.DnevnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredavanjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UcenikEntity;

public interface DnevnikRepository extends CrudRepository<DnevnikEntity, Integer> {


	List<DnevnikEntity> findAllByUcenik(UcenikEntity ucenik);

	List<DnevnikEntity> findAllByPredavanje(PredavanjeEntity predavanje);

}
