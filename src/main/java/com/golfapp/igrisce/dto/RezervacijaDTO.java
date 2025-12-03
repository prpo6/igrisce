package com.golfapp.igrisce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class RezervacijaDTO {
   private UUID id;
   private UUID clanId;
   private Integer skupina;
   private LocalDate datum;
   private LocalDateTime ura;
}