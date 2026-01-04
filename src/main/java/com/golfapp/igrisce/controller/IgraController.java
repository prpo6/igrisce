package com.golfapp.igrisce.controller;

import com.golfapp.igrisce.dto.CreateIgraRequest;
import com.golfapp.igrisce.dto.IgraDTO;
import com.golfapp.igrisce.service.IgraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/api/igre") @RequiredArgsConstructor @CrossOrigin(origins = "*")
public class IgraController {

   private final IgraService igraService;

   @PostMapping
   public ResponseEntity<IgraDTO> createIgra(@Valid @RequestBody CreateIgraRequest request) {
      IgraDTO created = igraService.createIgra(request.getRezervacijaId(), request.getClanId());
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
   }

   @GetMapping
   public ResponseEntity<List<IgraDTO>> getAllIgre() {
      List<IgraDTO> igre = igraService.getAllIgre();
      return ResponseEntity.ok(igre);
   }

   @GetMapping("/{id}")
   public ResponseEntity<IgraDTO> getIgra(@PathVariable UUID id) {
      IgraDTO igra = igraService.getIgra(id);
      return ResponseEntity.ok(igra);
   }

   @GetMapping("/rezervacije/{rezervacijaId}")
   public ResponseEntity<List<IgraDTO>> getIgreByRezervacija(@PathVariable UUID rezervacijaId) {
      List<IgraDTO> igre = igraService.getIgreByRezervacija(rezervacijaId);
      return ResponseEntity.ok(igre);
   }

   @GetMapping("/clani/{clanId}")
   public ResponseEntity<List<IgraDTO>> getIgreByClan(@PathVariable UUID clanId) {
      List<IgraDTO> igre = igraService.getIgreByClan(clanId);
      return ResponseEntity.ok(igre);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteIgra(@PathVariable UUID id) {
      igraService.deleteIgra(id);
      return ResponseEntity.noContent().build();
   }
}
