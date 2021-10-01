package ir.sahab.monitoringsystem.backend.domain.model;

import ir.sahab.monitoringsystem.backend.domain.model.Warning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarningRepository extends JpaRepository<Warning, Long> {

    @Query("select w from Warning w order by w.date, w.time")
    List<Warning> findAllOrderByDateAndTime();

}
