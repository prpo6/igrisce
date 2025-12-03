package com.golfapp.igrisce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class ScorecardDTO {
   private UUID igraId;
   private UUID clanId;
   private List<HoleScoreDTO> holes;
   private Integer totalScore;
   private Integer completedHoles;
}
