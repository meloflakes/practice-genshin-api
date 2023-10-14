package com.merufureku.genshinimpact.controller.interfaces;

import com.merufureku.genshinimpact.dto.UsersDTO;
import com.merufureku.genshinimpact.dto.params.NewUserParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

public interface IGenshinController {

    ResponseEntity<List<UsersDTO>> fetchAllPlayers();

    ResponseEntity<UsersDTO> fetchPlayers(Integer uid);

    ResponseEntity<UsersDTO> registerPlayer(NewUserParam newUserParam);

    @PutMapping("players/{uid}")
    ResponseEntity<UsersDTO> updateLogin(@PathVariable Integer uid);

    @DeleteMapping("players/{uid}")
    void deletePlayer(@PathVariable Integer uid);
}
