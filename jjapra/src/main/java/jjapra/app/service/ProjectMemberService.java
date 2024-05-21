package jjapra.app.service;

import jjapra.app.dto.project.AddProjectMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.repository.MemberRepository;
import jjapra.app.repository.ProjectMemberRepository;
import jjapra.app.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public Project findProjectById(Integer id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Member findMemberById(String id) {
        return memberRepository.findById(id).orElse(null);
    }

    public ProjectMember save(AddProjectMemberRequest request, Project project, Member member) {
        return projectMemberRepository.save(request.toEntity(project, member));
    }

    public ProjectMember save(ProjectMember projectMember) {
        return projectMemberRepository.save(projectMember);
    }
}
