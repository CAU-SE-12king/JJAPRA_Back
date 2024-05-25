package jjapra.app.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.member.AddMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@RequiredArgsConstructor
@RestController
@EnableWebMvc
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<Object> displayJoinSuccessPage(@RequestBody AddMemberRequest request) {
        if (request.getUsername().isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }
        if (memberService.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.badRequest().body("already exists id");
        }

        memberService.save(request);
        return ResponseEntity.ok().body("success");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Member member = memberService.findByUsername(request.getUsername());
        if (member == null) {
            return ResponseEntity.badRequest().body("Invalid id");
        }
        if (member.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body(member);
        } else {
            return ResponseEntity.badRequest().body("Invalid password");
        }
    }

    // 회원 정보 조회. 회원 ID를 받아서 해당 회원의 정보를 반환
    @GetMapping("/members/{username}")
    public Member getMember(@PathVariable("username") String username) {
        return memberService.findByUsername(username);
    }

    @GetMapping("/members")
    public List<Member> getMembers() {
        return memberService.findAll();
    }
}

@Getter
@Setter
@EnableWebMvc
class LoginRequest {
    private String username;
    private String password;
}