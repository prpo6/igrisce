package com.golfapp.igrisce.service;

import com.golfapp.igrisce.dto.EnterScoreRequest;
import com.golfapp.igrisce.dto.HoleScoreDTO;
import com.golfapp.igrisce.dto.RezultatDTO;
import com.golfapp.igrisce.dto.ScorecardDTO;
import com.golfapp.igrisce.exception.ResourceNotFoundException;
import com.golfapp.igrisce.model.Igra;
import com.golfapp.igrisce.model.Rezultat;
import com.golfapp.igrisce.repository.IgraRepository;
import com.golfapp.igrisce.repository.RezultatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
public class RezultatService {

   private final RezultatRepository rezultatRepository;
   private final IgraRepository igraRepository;

   @Transactional
   public RezultatDTO enterScore(EnterScoreRequest request) {
      log.info("Entering score for game: {}, hole: {}, score: {}", request.getIgraId(), request.getLuknja(),
               request.getRezultat());

      // Verify game exists
      if (!igraRepository.existsById(request.getIgraId())) {
         throw new ResourceNotFoundException("Igra not found with ID: " + request.getIgraId());
      }

      // Check if score already exists for this hole
      Optional<Rezultat> existing = rezultatRepository.findByIgraIdAndLuknja(request.getIgraId(), request.getLuknja());

      Rezultat rezultat;
      if (existing.isPresent()) {
         // Update existing score
         log.info("Updating existing score for hole: {}", request.getLuknja());
         rezultat = existing.get();
         rezultat.setRezultat(request.getRezultat());
      } else {
         // Create new score
         rezultat = new Rezultat();
         rezultat.setIgraId(request.getIgraId());
         rezultat.setLuknja(request.getLuknja());
         rezultat.setRezultat(request.getRezultat());
      }

      Rezultat saved = rezultatRepository.save(rezultat);
      log.info("Score saved with ID: {}", saved.getId());

      return mapToDTO(saved);
   }

   @Transactional(readOnly = true)
   public ScorecardDTO getScorecard(UUID igraId) {
      log.info("Fetching scorecard for game: {}", igraId);

      Igra igra = igraRepository.findById(igraId)
               .orElseThrow(() -> new ResourceNotFoundException("Igra not found with ID: " + igraId));

      List<Rezultat> rezultati = rezultatRepository.findByIgraIdOrderByLuknja(igraId);

      List<HoleScoreDTO> holes = rezultati.stream().map(r -> new HoleScoreDTO(r.getLuknja(), r.getRezultat()))
               .collect(Collectors.toList());

      Integer totalScore = rezultatRepository.getTotalScoreForGame(igraId);

      ScorecardDTO scorecard = new ScorecardDTO();
      scorecard.setIgraId(igraId);
      scorecard.setClanId(igra.getClanId());
      scorecard.setHoles(holes);
      scorecard.setTotalScore(totalScore != null ? totalScore : 0);
      scorecard.setCompletedHoles(holes.size());

      return scorecard;
   }

   @Transactional(readOnly = true)
   public List<RezultatDTO> getScoresForGame(UUID igraId) {
      log.info("Fetching all scores for game: {}", igraId);

      if (!igraRepository.existsById(igraId)) {
         throw new ResourceNotFoundException("Igra not found with ID: " + igraId);
      }

      return rezultatRepository.findByIgraIdOrderByLuknja(igraId).stream().map(this::mapToDTO)
               .collect(Collectors.toList());
   }

   @Transactional(readOnly = true)
   public RezultatDTO getScoreForHole(UUID igraId, Integer luknja) {
      log.info("Fetching score for game: {}, hole: {}", igraId, luknja);

      Rezultat rezultat = rezultatRepository.findByIgraIdAndLuknja(igraId, luknja)
               .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Score not found for game %s, hole %d", igraId, luknja)));

      return mapToDTO(rezultat);
   }

   @Transactional
   public void deleteScore(UUID rezultatId) {
      log.info("Deleting score with ID: {}", rezultatId);

      if (!rezultatRepository.existsById(rezultatId)) {
         throw new ResourceNotFoundException("Rezultat not found with ID: " + rezultatId);
      }

      rezultatRepository.deleteById(rezultatId);
      log.info("Score deleted with ID: {}", rezultatId);
   }

   @Transactional
   public void deleteAllScoresForGame(UUID igraId) {
      log.info("Deleting all scores for game: {}", igraId);

      List<Rezultat> rezultati = rezultatRepository.findByIgraId(igraId);
      rezultatRepository.deleteAll(rezultati);

      log.info("Deleted {} scores for game: {}", rezultati.size(), igraId);
   }

   private RezultatDTO mapToDTO(Rezultat rezultat) {
      return new RezultatDTO(rezultat.getId(), rezultat.getLuknja(), rezultat.getRezultat(), rezultat.getIgraId());
   }
}