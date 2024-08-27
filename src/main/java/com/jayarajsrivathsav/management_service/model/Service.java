package com.jayarajsrivathsav.management_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Document(collection = "service")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Service {
    @Id private String nodeId;
    private String nodeName;
    private String description;
    private String memo;
    private String nodeType;
    private String parentNodeGroupName;
    private String parentNodeGroupId;
    private String parentNodeGroupType;
}
