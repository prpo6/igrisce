package com.golfapp.igrisce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity @Table(name = "rezultati") @Data @NoArgsConstructor @AllArgsConstructor
public class Rezultat {

   @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "id", updatable = false, nullable = false)
   private UUID id;

   @Column(name = "luknja")
   private Integer luknja;

   @Column(name = "rezultat")
   private Integer rezultat;

   @Column(name = "igra_id")
   private UUID igraId;

   @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "igra_id", insertable = false, updatable = false) @JsonIgnore
   private Igra igra;
}