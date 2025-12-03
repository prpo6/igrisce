package com.golfapp.igrisce.repository;

import com.golfapp.igrisce.model.Igra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IgraRepository extends JpaRepository<Igra, UUID> {

   List<Igra> findByRezervacijaId(UUID rezervacijaId);

   List<Igra> findByClanId(UUID clanId);
}