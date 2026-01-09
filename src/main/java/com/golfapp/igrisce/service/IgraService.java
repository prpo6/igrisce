package com.golfapp.igrisce.service;

import com.golfapp.igrisce.dto.IgraDTO;
import com.golfapp.igrisce.dto.RezultatDTO;
import com.golfapp.igrisce.exception.ResourceNotFoundException;
import com.golfapp.igrisce.model.Igra;
import com.golfapp.igrisce.repository.IgraRepository;
import com.golfapp.igrisce.repository.RezervacijaRepository;
import com.golfapp.igrisce.repository.RezultatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
public class IgraService {

   private final IgraRepository igraRepository;
   private final RezervacijaRepository rezervacijaRepository;
   private final RezultatRepository rezultatRepository;

   @Transactional
   public IgraDTO createIgra(UUID rezervacijaId, UUID clanId) {
      log.info("Creating new game for rezervacija: {} and clan: {}", rezervacijaId, clanId);

      // Verify reservation exists (only if rezervacijaId is provided - tournament games don't need it)
      if (rezervacijaId != null && !rezervacijaRepository.existsById(rezervacijaId)) {
         throw new ResourceNotFoundException("Rezervacija not found with ID: " + rezervacijaId);
      }

      Igra igra = new Igra();
      igra.setRezervacijaId(rezervacijaId);
      igra.setClanId(clanId);

      Igra saved = igraRepository.save(igra);
      log.info("Game created with ID: {}", saved.getId());

      return mapToDTO(saved);
   }

   @Transactional(readOnly = true)
   public IgraDTO getIgra(UUID id) {
      log.info("Fetching game with ID: {}", id);
      Igra igra = igraRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Igra not found with ID: " + id));
      return mapToDTO(igra);
   }

   @Transactional(readOnly = true)
   public List<IgraDTO> getAllIgre() {
      log.info("Fetching all games");
      return igraRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
   }

   @Transactional(readOnly = true)
   public List<IgraDTO> getIgreByRezervacija(UUID rezervacijaId) {
      log.info("Fetching games for rezervacija: {}", rezervacijaId);
      return igraRepository.findByRezervacijaId(rezervacijaId).stream().map(this::mapToDTO)
               .collect(Collectors.toList());
   }

   @Transactional(readOnly = true)
   public List<IgraDTO> getIgreByClan(UUID clanId) {
      log.info("Fetching games for clan: {}", clanId);
      return igraRepository.findByClanId(clanId).stream().map(this::mapToDTO).collect(Collectors.toList());
   }

   @Transactional
   public void deleteIgra(UUID id) {
      log.info("Deleting game with ID: {}", id);

      if (!igraRepository.existsById(id)) {
         throw new ResourceNotFoundException("Igra not found with ID: " + id);
      }

      igraRepository.deleteById(id);
      log.info("Game deleted with ID: {}", id);
   }

   private IgraDTO mapToDTO(Igra igra) {
      IgraDTO dto = new IgraDTO();
      dto.setId(igra.getId());
      dto.setRezervacijaId(igra.getRezervacijaId());
      dto.setClanId(igra.getClanId());

      // Get total score
      Integer totalScore = rezultatRepository.getTotalScoreForGame(igra.getId());
      dto.setTotalScore(totalScore != null ? totalScore : 0);

      // Get all results
      List<RezultatDTO> rezultati = rezultatRepository.findByIgraIdOrderByLuknja(igra.getId()).stream()
               .map(r -> new RezultatDTO(r.getId(), r.getLuknja(), r.getRezultat(), r.getIgraId()))
               .collect(Collectors.toList());
      dto.setRezultati(rezultati);

      return dto;
   }
}