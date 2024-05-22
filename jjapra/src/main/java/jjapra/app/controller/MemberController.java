package jjapra.app.controller;

import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.member.AddMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<Object> displayJoinSuccessPage(@RequestBody AddMemberRequest request) {
        if (request.getId().isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }
        if (memberService.findById(request.getId()) != null) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }

        memberService.save(request);
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        Member member = memberService.findById(request.getId());
        if (member == null) {
            return ResponseEntity.badRequest().body("아이디 오류");
        }
        if (member.getPassword().equals(request.getPassword())) {
            session.setAttribute("loggedInUser", member);
            return ResponseEntity.ok().body("로그인 성공");
        } else {
            return ResponseEntity.badRequest().body("비밀번호 오류");
        }
    }

    // admin 권한으로 로그인한 경우 특정 회원 정보를 조회
    @GetMapping("/members/{id}")
    public Object getMember(@PathVariable("id") String id, HttpSession session) {
        if (((Member) session.getAttribute("loggedInUser")).is_admin()) {
            return memberService.findById(id);
        }
        return null;
    }

    // admin 권한으로 로그인한 경우 모든 회원 정보를 조회
    // 일반 회원으로 로그인한 경우 자신의 정보만 조회
    @GetMapping("/members")
    public Object getMembers(HttpSession session) {
        if (((Member) session.getAttribute("loggedInUser")).is_admin()) {
            return memberService.findAll();
        }
        return memberService.findById(((Member) session.getAttribute("loggedInUser")).getId());
    }
}

@Getter
@Setter
class LoginRequest {
    private String id;
    private String password;
}