package com.merufureku.genshinimpact.controller.impl;

import com.merufureku.genshinimpact.controller.interfaces.IGenshinController;
import com.merufureku.genshinimpact.dto.UsersDTO;
import com.merufureku.genshinimpact.dto.params.NewUserParam;
import com.merufureku.genshinimpact.service.exception.ResourceNotFoundException;
import com.merufureku.genshinimpact.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.ws.rs.BeanParam;
import java.util.List;

@RestController
@RequestMapping(path = "api/genshin")
public class GenshinControllerImpl implements IGenshinController {

    private final IUserService userService;

    @Autowired
    public GenshinControllerImpl(IUserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("users")
    public ResponseEntity<List<UsersDTO>> fetchAllPlayers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    @GetMapping("user/{uid}")
    public ResponseEntity<UsersDTO> fetchPlayers(@PathVariable Integer uid) {
        return ResponseEntity.ok(userService.getUser(uid));
    }

    @Override
    @PostMapping("user")
    public ResponseEntity<UsersDTO> registerPlayer(@Valid @BeanParam NewUserParam newUserParam) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(newUserParam));
    }

    @Override
    @PutMapping("user/{uid}")
    public ResponseEntity<UsersDTO> updateLogin(@PathVariable Integer uid){
        return ResponseEntity.ok(userService.updateUserLoginDate(uid));
    }

    @Override
    @DeleteMapping("user/{uid}")
    public void deletePlayer(@PathVariable Integer uid){
        userService.deleteUser(uid);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleException(Exception ex) {
//        if (ex instanceof IllegalArgumentException || ex instanceof ValidationException) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request.");
//        } else if (ex instanceof ResourceNotFoundException) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred.");
//        }
//    }
}
