package com.golfapp.igrisce.repository;

import com.golfapp.igrisce.model.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RezervacijaRepository extends JpaRepository<Rezervacija, UUID>, JpaSpecificationExecutor<Rezervacija> {

   List<Rezervacija> findByDatum(LocalDate datum);

   List<Rezervacija> findByClanId(UUID clanId);

   List<Rezervacija> findByDatumBetween(LocalDate startDate, LocalDate endDate);

   List<Rezervacija> findBySkupina(Integer skupina);
}