package jjapra.app.controller;

import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.project.AddProjectRequest;
import jjapra.app.model.member.Member;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.model.member.Role;
import jjapra.app.service.ProjectMemberService;
import jjapra.app.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;

    @GetMapping("")
    public ResponseEntity<List<ProjectMember>> getProjects(HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        List<ProjectMember> projects = projectMemberService.findByMemberId(((Member) loggedInUser).getId());
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @PostMapping("")
    public ResponseEntity<Project> addProject(@RequestBody AddProjectRequest request, HttpSession session) {
        // admin인 경우에만 프로젝트 생성할 수 있도록 코드 추가하기
        if (request.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Project project = projectService.findByTitle(request.getTitle());
        if (project != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Project savedProject = projectService.save(request);
        ProjectMember projectMember = ProjectMember.builder()
                .project(savedProject)
                .member((Member) session.getAttribute("loggedInUser"))
                .role(Role.ADMIN)
                .build();
        projectMemberService.save(projectMember);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") Integer id, HttpSession session) {
        List<ProjectMember> projectMembers = projectMemberService.findByProjectId(id);
        if (projectMembers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if (projectMembers.stream().noneMatch(pm -> pm.getMember().getId().equals((
                (Member) session.getAttribute("loggedInUser")).getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(projectMembers.get(0).getProject());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Integer id, @RequestBody AddProjectRequest request, HttpSession session) {
        List<ProjectMember> projectMembers = projectMemberService.findByProjectId(id);
        if (projectMembers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if (projectMembers.stream().noneMatch(pm -> pm.getMember().getId().equals((
                (Member) session.getAttribute("loggedInUser")).getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Project updatedProject = projectService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteProject(@PathVariable("id") Integer id) {
        List<ProjectMember> projectMemberList = projectMemberService.findByProjectId(id);
        if (projectMemberList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        projectMemberList.forEach(projectMemberService::delete);
        return projectService.deleteProject(id);
    }
}

