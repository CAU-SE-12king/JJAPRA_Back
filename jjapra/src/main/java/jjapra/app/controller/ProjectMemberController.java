package jjapra.app.controller;

import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.project.AddProjectMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.model.member.Role;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.service.MemberService;
import jjapra.app.service.ProjectMemberService;
import jjapra.app.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;
    private final ProjectService projectService;

    @PostMapping("/projects/{id}")
    public ProjectMember save(@PathVariable("id") Integer id, @RequestBody AddProjectMemberRequest request, HttpSession session) {
        Project project = projectService.findById(id);
        if (project == null) {
            return null;
        }
        Member member = (Member) session.getAttribute("loggedInUser");

        return projectMemberService.save(request, project, member);
    }
}
