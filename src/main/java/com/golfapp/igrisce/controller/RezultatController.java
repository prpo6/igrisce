package com.golfapp.igrisce.controller;

import com.golfapp.igrisce.dto.EnterScoreRequest;
import com.golfapp.igrisce.dto.RezultatDTO;
import com.golfapp.igrisce.dto.ScorecardDTO;
import com.golfapp.igrisce.service.RezultatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/api/rezultati") @RequiredArgsConstructor @CrossOrigin(origins = "*")
public class RezultatController {

   private final RezultatService rezultatService;

   @PostMapping
   public ResponseEntity<RezultatDTO> enterScore(@Valid @RequestBody EnterScoreRequest request) {
      RezultatDTO rezultat = rezultatService.enterScore(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(rezultat);
   }

   @GetMapping("/igre/{igraId}/scorecard")
   public ResponseEntity<ScorecardDTO> getScorecard(@PathVariable UUID igraId) {
      ScorecardDTO scorecard = rezultatService.getScorecard(igraId);
      return ResponseEntity.ok(scorecard);
   }

   @GetMapping("/igre/{igraId}")
   public ResponseEntity<List<RezultatDTO>> getScoresForGame(@PathVariable UUID igraId) {
      List<RezultatDTO> scores = rezultatService.getScoresForGame(igraId);
      return ResponseEntity.ok(scores);
   }

   @GetMapping("/igre/{igraId}/holes/{luknja}")
   public ResponseEntity<RezultatDTO> getScoreForHole(@PathVariable UUID igraId, @PathVariable Integer luknja) {
      RezultatDTO score = rezultatService.getScoreForHole(igraId, luknja);
      return ResponseEntity.ok(score);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteScore(@PathVariable UUID id) {
      rezultatService.deleteScore(id);
      return ResponseEntity.noContent().build();
   }

   @DeleteMapping("/igre/{igraId}")
   public ResponseEntity<Void> deleteAllScoresForGame(@PathVariable UUID igraId) {
      rezultatService.deleteAllScoresForGame(igraId);
      return ResponseEntity.noContent().build();
   }
}