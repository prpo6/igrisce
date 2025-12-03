package com.golfapp.igrisce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class EnterScoreRequest {

   @NotNull(message = "Igra ID je obvezna")
   private UUID igraId;

   @NotNull(message = "Luknja number je obvezna") @Min(value = 1, message = "Luknja mora biti med 1 in 18") @Max(value = 18, message = "Luknja mora biti med 1 in 18")
   private Integer luknja;

   @NotNull(message = "Rezultat je obvezna") @Min(value = 1, message = "Rezultat more biti vsaj 1")
   private Integer rezultat;
}