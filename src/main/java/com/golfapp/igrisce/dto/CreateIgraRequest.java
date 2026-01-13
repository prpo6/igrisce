package com.golfapp.igrisce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateIgraRequest {

   // Optional - can be null for tournament games without reservation
   private UUID rezervacijaId;

   @NotNull(message = "Clan ID je obvezen")
   private UUID clanId;
}
