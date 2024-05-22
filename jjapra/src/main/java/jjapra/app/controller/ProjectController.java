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
        return ResponseEntity.ok().body(projects);
    }

    @PostMapping("")
    public ResponseEntity<Project> addProject(@RequestBody AddProjectRequest request, HttpSession session) {
        // admin이 아닌 경우 401 Unauthorized
        if (!((Member) session.getAttribute("loggedInUser")).is_admin()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // title이 비어있는 경우 400 Bad Request
        if (request.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Project project = projectService.findByTitle(request.getTitle());
        // 이미 존재하는 프로젝트인 경우 400 Bad Request
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
        // 프로젝트가 존재하지 않는 경우 404 Not Found
        if (projectMembers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        // 프로젝트에 속하지 않은 경우 403 Forbidden
        if (projectMembers.stream().noneMatch(pm -> pm.getMember().getId().equals((
                (Member) session.getAttribute("loggedInUser")).getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(projectMembers.get(0).getProject());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Integer id, @RequestBody AddProjectRequest request, HttpSession session) {
        List<ProjectMember> projectMembers = projectMemberService.findByProjectId(id);
        // 프로젝트가 존재하지 않는 경우 404 Not Found
        if (projectMembers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        // 프로젝트에 속하지 않은 경우 403 Forbidden
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

