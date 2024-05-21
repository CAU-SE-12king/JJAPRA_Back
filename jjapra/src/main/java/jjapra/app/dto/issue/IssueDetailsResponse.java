package jjapra.app.dto.issue;

import jjapra.app.model.issue.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IssueDetailsResponse {
    private String title;
    private String description;
    private List<Comment> comments;
}