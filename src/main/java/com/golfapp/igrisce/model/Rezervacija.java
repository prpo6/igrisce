package com.golfapp.igrisce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity @Table(name = "rezervacije") @Data @NoArgsConstructor @AllArgsConstructor
public class Rezervacija {

   @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "id", updatable = false, nullable = false)
   private UUID id;

   @Column(name = "clan_id")
   private UUID clanId;

   @Column(name = "skupina")
   private Integer skupina;

   @Column(name = "datum")
   private LocalDate datum;

   @Column(name = "ura", columnDefinition = "time(0)")
   private LocalTime ura;

   @OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Igra> igre;
}