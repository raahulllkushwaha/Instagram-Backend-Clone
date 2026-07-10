package com.rahul.instagram.follow;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface UserNodeRepository extends Neo4jRepository<UserNode, Long> {
    Optional<UserNode> findByUsername(String username);
}
