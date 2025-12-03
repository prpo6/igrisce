package com.golfapp.igrisce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity @Table(name = "igre") @Data @NoArgsConstructor @AllArgsConstructor
public class Igra {

   @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "id", updatable = false, nullable = false)
   private UUID id;

   @Column(name = "rezervacija_id", nullable = false)
   private UUID rezervacijaId;

   @Column(name = "clan_id")
   private UUID clanId;

   @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "rezervacija_id", insertable = false, updatable = false) @JsonIgnore
   private Rezervacija rezervacija;

   @OneToMany(mappedBy = "igra", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Rezultat> rezultati;
}