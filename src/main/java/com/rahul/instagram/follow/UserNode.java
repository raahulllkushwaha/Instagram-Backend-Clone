package com.rahul.instagram.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Node("User")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNode {

    @Id
    private Long userId;

    private String username;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private Set<UserNode> following;
}
