package org.example.dto.request;

import java.util.List;
import lombok.Data;
import org.example.entity.Member;

@Data
public class CreateGroupRequest {
    private String groupName;
    private List<Integer> memberIds;
}
