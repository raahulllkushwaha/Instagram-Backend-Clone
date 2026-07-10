package com.rahul.instagram.follow;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface UserNodeRepository extends Neo4jRepository<UserNode, Long> {
    Optional<UserNode> findByUsername(String username);

    @Query("MATCH (a:User {userId: $followerId}), (b:User {userId: $followingId}) " +
            "MERGE (a)-[:FOLLOWS]->(b)")
    void followUser(Long followerId, Long followingId);

    @Query("MATCH (a:User {userId: $followerId})-[r:FOLLOWS]->(b:User {userId: $followingId}) " +
            "DELETE r")
    void unfollowUser(Long followerId, Long followingId);

    @Query("MATCH (a:User {userId: $userId})-[:FOLLOWS]->(b:User) RETURN b")
    List<UserNode> getFollowing(Long userId);

    @Query("MATCH (a:User)-[:FOLLOWS]->(b:User {userId: $userId}) RETURN a")
    List<UserNode> getFollowers(Long userId);

    @Query("MATCH (a:User {userId: $userId1})-[:FOLLOWS]->(mutual:User)<-[:FOLLOWS]-(b:User {userId: $userId2}) " +
            "RETURN mutual")
    List<UserNode> getMutualFollowing(Long userId1, Long userId2);
}
