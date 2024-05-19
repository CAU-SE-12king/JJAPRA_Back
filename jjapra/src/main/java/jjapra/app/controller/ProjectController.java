package jjapra.app.controller;

import jjapra.app.dto.AddProjectRequest;
import jjapra.app.model.Project;
import jjapra.app.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class ProjectController {

    private final ProjectService projectService;

    @RequestMapping(value = "/project")
    public String displayProjectCreatePage(){return "project.html";}

    @PostMapping(value = "/project/create")
    public ResponseEntity<Project> addProject(@RequestBody AddProjectRequest request) {
        if (request.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Project project = projectService.findByTitle(request.getTitle());
        if (project != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Project savedProject = projectService.save(request);
        System.out.println("Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @RequestMapping(value = "/project/:id")
    public ResponseEntity<Project> getProject(@PathVariable String id) {
        Project project = projectService.findById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }
}

