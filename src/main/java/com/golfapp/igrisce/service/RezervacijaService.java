package com.golfapp.igrisce.service;

import com.golfapp.igrisce.client.TurnirjiClient;
import com.golfapp.igrisce.dto.CreateRezervacijaRequest;
import com.golfapp.igrisce.dto.RezervacijaDTO;
import com.golfapp.igrisce.exception.ResourceNotFoundException;
import com.golfapp.igrisce.model.Rezervacija;
import com.golfapp.igrisce.repository.RezervacijaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
public class RezervacijaService {

   private final RezervacijaRepository rezervacijaRepository;
   private final TurnirjiClient turnirjiClient;

   @Transactional
   public RezervacijaDTO createRezervacija(CreateRezervacijaRequest request) {
      log.info("Creating new reservation for clan: {}", request.getClanId());

      // Check if there's a tournament on this date
      if (turnirjiClient.isTournamentOnDate(request.getDatum())) {
         log.warn("Cannot create reservation - tournament is scheduled on date: {}", request.getDatum());
         throw new IllegalStateException("Ne morete ustvariti rezervacije - ta dan je rezerviran za turnir");
      }

      // Check if user already has a reservation on this date
      List<Rezervacija> existingRezervacije = rezervacijaRepository.findByClanIdAndDatum(
         request.getClanId(), request.getDatum());
      
      if (!existingRezervacije.isEmpty()) {
         log.warn("User {} already has a reservation on date: {}", request.getClanId(), request.getDatum());
         throw new IllegalStateException("Na ta dan že imate rezervacijo. Lahko imate samo eno rezervacijo na dan.");
      }

      Rezervacija rezervacija = new Rezervacija();
      rezervacija.setClanId(request.getClanId());
      rezervacija.setSkupina(request.getSkupina());
      rezervacija.setDatum(request.getDatum());
      rezervacija.setUra(request.getUra());

      Rezervacija saved = rezervacijaRepository.save(rezervacija);
      log.info("Reservation created with ID: {}", saved.getId());

      return mapToDTO(saved);
   }

   @Transactional(readOnly = true)
   public RezervacijaDTO getRezervacija(UUID id) {
      log.info("Fetching reservation with ID: {}", id);
      Rezervacija rezervacija = rezervacijaRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Rezervacija not found with ID: " + id));
      return mapToDTO(rezervacija);
   }

   @Transactional(readOnly = true)
   public List<RezervacijaDTO> getAllRezervacije() {
      log.info("Fetching all reservations");
      return rezervacijaRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
   }

   @Transactional(readOnly = true)
   public List<RezervacijaDTO> getRezervacijeByFilters(UUID clanId, LocalDate datum, LocalDate datumOd,
            LocalDate datumDo, Integer skupina) {
      log.info("Fetching reservations with filters - clanId: {}, datum: {}, skupina: {}", clanId, datum, skupina);

      Specification<Rezervacija> spec = Specification.where(null);

      if (clanId != null) {
         spec = spec.and((root, query, cb) -> cb.equal(root.get("clanId"), clanId));
      }

      if (datum != null) {
         spec = spec.and((root, query, cb) -> cb.equal(root.get("datum"), datum));
      }

      if (datumOd != null && datumDo != null) {
         spec = spec.and((root, query, cb) -> cb.between(root.get("datum"), datumOd, datumDo));
      }

      if (skupina != null) {
         spec = spec.and((root, query, cb) -> cb.equal(root.get("skupina"), skupina));
      }

      return rezervacijaRepository.findAll(spec).stream().map(this::mapToDTO).collect(Collectors.toList());
   }

   @Transactional
   public RezervacijaDTO updateRezervacija(UUID id, CreateRezervacijaRequest request) {
      log.info("Updating reservation with ID: {}", id);

      Rezervacija rezervacija = rezervacijaRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Rezervacija not found with ID: " + id));

      rezervacija.setClanId(request.getClanId());
      rezervacija.setSkupina(request.getSkupina());
      rezervacija.setDatum(request.getDatum());
      rezervacija.setUra(request.getUra());

      Rezervacija updated = rezervacijaRepository.save(rezervacija);
      log.info("Reservation updated with ID: {}", updated.getId());

      return mapToDTO(updated);
   }

   @Transactional
   public void deleteRezervacija(UUID id) {
      log.info("Deleting reservation with ID: {}", id);

      if (!rezervacijaRepository.existsById(id)) {
         throw new ResourceNotFoundException("Rezervacija not found with ID: " + id);
      }

      rezervacijaRepository.deleteById(id);
      log.info("Reservation deleted with ID: {}", id);
   }

   @Transactional(readOnly = true)
   public List<RezervacijaDTO> getPastRezervacije(Integer limit) {
      log.info("Fetching past reservations with limit: {}", limit);
      LocalDate today = LocalDate.now();
      List<Rezervacija> pastReservations = rezervacijaRepository.findByDatumBeforeOrderByDatumDescUraDesc(today);
      
      if (limit != null && limit > 0) {
         pastReservations = pastReservations.stream().limit(limit).collect(Collectors.toList());
      }
      
      return pastReservations.stream().map(this::mapToDTO).collect(Collectors.toList());
   }

   private RezervacijaDTO mapToDTO(Rezervacija rezervacija) {
      return new RezervacijaDTO(rezervacija.getId(), rezervacija.getClanId(), rezervacija.getSkupina(),
               rezervacija.getDatum(), rezervacija.getUra());
   }
}