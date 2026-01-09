package com.golfapp.igrisce.controller;

import com.golfapp.igrisce.dto.CreateRezervacijaRequest;
import com.golfapp.igrisce.dto.RezervacijaDTO;
import com.golfapp.igrisce.service.RezervacijaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/api/rezervacije") @RequiredArgsConstructor @CrossOrigin(origins = "*")
public class RezervacijaController {

   private final RezervacijaService rezervacijaService;

   @PostMapping
   public ResponseEntity<RezervacijaDTO> createRezervacija(@Valid @RequestBody CreateRezervacijaRequest request) {
      RezervacijaDTO created = rezervacijaService.createRezervacija(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
   }

   @GetMapping("/{id}")
   public ResponseEntity<RezervacijaDTO> getRezervacija(@PathVariable UUID id) {
      RezervacijaDTO rezervacija = rezervacijaService.getRezervacija(id);
      return ResponseEntity.ok(rezervacija);
   }

   @GetMapping
   public ResponseEntity<List<RezervacijaDTO>> getRezervacije(@RequestParam(required = false) UUID clanId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datum,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datumOd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datumDo,
            @RequestParam(required = false) Integer skupina) {

      List<RezervacijaDTO> rezervacije;

      if (clanId == null && datum == null && datumOd == null && datumDo == null && skupina == null) {
         rezervacije = rezervacijaService.getAllRezervacije();
      } else {
         rezervacije = rezervacijaService.getRezervacijeByFilters(clanId, datum, datumOd, datumDo, skupina);
      }

      return ResponseEntity.ok(rezervacije);
   }

   @PutMapping("/{id}")
   public ResponseEntity<RezervacijaDTO> updateRezervacija(@PathVariable UUID id,
            @Valid @RequestBody CreateRezervacijaRequest request) {
      RezervacijaDTO updated = rezervacijaService.updateRezervacija(id, request);
      return ResponseEntity.ok(updated);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteRezervacija(@PathVariable UUID id) {
      rezervacijaService.deleteRezervacija(id);
      return ResponseEntity.noContent().build();
   }

   @GetMapping("/past")
   public ResponseEntity<List<RezervacijaDTO>> getPastRezervacije(
            @RequestParam(required = false, defaultValue = "50") Integer limit) {
      List<RezervacijaDTO> pastReservations = rezervacijaService.getPastRezervacije(limit);
      return ResponseEntity.ok(pastReservations);
   }
}