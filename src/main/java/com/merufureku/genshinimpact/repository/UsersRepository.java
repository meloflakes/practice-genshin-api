package com.merufureku.genshinimpact.repository;

import com.merufureku.genshinimpact.dto.UsersDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UsersRepository extends JpaRepository<UsersDTO, Long> {

    List<UsersDTO> findAll();

    UsersDTO findByUid(Integer uid);

    @Query(value = "SELECT uid FROM users ORDER BY uid desc LIMIT 1", nativeQuery = true)
    Integer findLatestUid();

    UsersDTO findByEmailAndUsername(String email, String username);
}
