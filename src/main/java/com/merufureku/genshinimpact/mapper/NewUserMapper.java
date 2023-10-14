package com.merufureku.genshinimpact.mapper;

import com.merufureku.genshinimpact.dto.UsersDTO;
import com.merufureku.genshinimpact.dto.params.NewUserParam;
import com.merufureku.genshinimpact.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.logging.Logger;

@Component
public class NewUserMapper {
    Logger logger = Logger.getLogger(NewUserMapper.class.getName());

    private final UsersRepository usersRepository;

    @Autowired
    public NewUserMapper(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UsersDTO mapUser(NewUserParam userParam){
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setUid(getLatestUid());
        usersDTO.setEmail(userParam.getEmail());
        usersDTO.setPassword(userParam.getPassword());
        usersDTO.setUsername(userParam.getUsername());
        usersDTO.setLast_login_date(null);
        usersDTO.setCreated_date(LocalDate.now());

        logger.info("Mapped new user: " + usersDTO);
        return usersDTO;
    }

    private Integer getLatestUid(){
        Integer latestUid = usersRepository.findLatestUid();
        logger.info("Latest UID is: " + latestUid);
        if (latestUid == null){
            return 80000;
        }
        return latestUid + 1;
    }
}
