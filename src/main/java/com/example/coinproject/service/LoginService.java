package com.example.coinproject.service;

import com.example.coinproject.DTO.RegisterForm;
import com.example.coinproject.entity.coin_user;
import com.example.coinproject.repository.RegisterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class LoginService {
    private final RegisterRepository registerRepository;

    public int login(coin_user user) {      //로그인 서비스
        Optional<coin_user> result_id = registerRepository.findByUserid(user.getUserid());
        if (!result_id.get().getUserid().equals(user.getUserid())) {
            return 0;
        }
        if(!result_id.get().getUserpw().equals(user.getUserpw())) {
            return 0;
        }
        return 1;
    }

    public int managerlogin(coin_user user){        //관리자 로그인
        if(!user.getUserid().equals("root")){
            return 0;
        }
        if(!user.getUserpw().equals("1234")){
            return 0;
        }
        return 1;
    }
}
