package com.golfapp.igrisce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateRezervacijaRequest {

   @NotNull(message = "Clan ID je obvezen")
   private UUID clanId;

   private Integer skupina;

   @NotNull(message = "Datum je obvezen")
   private LocalDate datum;

   @NotNull(message = "Ura je obvezna")
   private LocalTime ura;
}