package com.golfapp.igrisce.repository;

import com.golfapp.igrisce.model.Rezultat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RezultatRepository extends JpaRepository<Rezultat, UUID> {

   List<Rezultat> findByIgraId(UUID igraId);

   Optional<Rezultat> findByIgraIdAndLuknja(UUID igraId, Integer luknja);

   @Query("SELECT SUM(r.rezultat) FROM Rezultat r WHERE r.igraId = :igraId")
   Integer getTotalScoreForGame(UUID igraId);

   @Query("SELECT r FROM Rezultat r WHERE r.igraId = :igraId ORDER BY r.luknja ASC")
   List<Rezultat> findByIgraIdOrderByLuknja(UUID igraId);
}