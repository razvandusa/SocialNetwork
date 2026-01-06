package org.example.repository;

import org.example.domain.FriendshipRequest;
import org.example.domain.Message;
import org.example.domain.Status;
import org.example.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipRequestDataBaseRepository implements Repository<Long, FriendshipRequest> {
    private final String url;
    private final String username;
    private final String password;

    public FriendshipRequestDataBaseRepository(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(FriendshipRequest entity) {

    }

    @Override
    public void remove(FriendshipRequest entity) {

    }

    @Override
    public FriendshipRequest findById(Long aLong) {
        return null;
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        return null;
    }
}
