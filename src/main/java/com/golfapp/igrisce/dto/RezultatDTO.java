package com.golfapp.igrisce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class RezultatDTO {
   private UUID id;
   private Integer luknja;
   private Integer rezultat;
   private UUID igraId;
}