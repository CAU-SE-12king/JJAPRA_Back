package jjapra.app.repository;

import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    ProjectMember save(ProjectMember projectMember);
    void delete(ProjectMember projectMember);
    List<ProjectMember> findByMemberId(String memberId);
    List<ProjectMember> findByProjectId(Integer projectId);
}
