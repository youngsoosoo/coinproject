package com.example.coinproject.controller;

import com.example.coinproject.DTO.RoomForm;
import com.example.coinproject.entity.coin_room;
import com.example.coinproject.entity.coin_user;
import com.example.coinproject.repository.RegisterRepository;
import com.example.coinproject.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j  //로깅을 위한 어노테이션
public class RoomController {

    @Autowired // 객체 자동 연결
    private RoomRepository roomRepository;
    @Autowired
    private RegisterRepository registerRepository;

    @GetMapping("/mainpage")
    public String Mainpage(Model model){
        List<coin_room> all = roomRepository.findAll(); //리스트에 DB정보 넣기
        model.addAttribute("list",all); //반복문을 위한 반복문 사용과 리스트 값 넘기기 위한 model
        return "mainpage";
    }

    @PostMapping("/use")
    public String Use(RoomForm form, HttpSession session){
        coin_user user = new coin_user();
        coin_room room = form.toEntity();
        Optional<coin_user> result_user = registerRepository.findByUserid((String) session.getAttribute("userid"));
        Optional<coin_room> result_room = roomRepository.findByNumroom(form.toEntity().getNumroom());
        room.setNumroom(form.toEntity().getNumroom());
        int a = result_room.get().getCoin() + form.toEntity().getCoin(); // 원래 방에 존재하는 코인 개수와의 합
        int b = a * 3;  //etime 시간 계산
        int c = result_user.get().getUsercoin() - form.toEntity().getCoin();    //아이디가 보유하고 있는 코인을 방에 넣어줌
        if(form.toEntity().getCoin() > 0){
            room.setCoin(a);
            room.setEtime(b);
            room.setIuse("사용중");
            user.setUserid(result_user.get().getUserid());
            if(result_user.get().getUsercoin() < 1){
                return "coinfail";
            }
            user.setUsercoin(c);
            user.setUsername(result_user.get().getUsername());
            user.setUserpw(result_user.get().getUserpw());
            roomRepository.save(room);
            registerRepository.save(user);
            return "redirect:/mainpage";
        }else if(form.toEntity().getCoin() <= 0){
            return "mainpage";
        }
        return "mainpage";
    }

}
