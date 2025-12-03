package com.golfapp.igrisce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class IgraDTO {
   private UUID id;
   private UUID rezervacijaId;
   private UUID clanId;
   private Integer totalScore;
   private List<RezultatDTO> rezultati;
}