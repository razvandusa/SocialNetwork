package org.example.repository;

import org.example.domain.User;

public class FriendshipDataBaseRepository implements Repository<Long, User>{
    @Override
    public void add(User entity) {

    }

    @Override
    public void remove(User entity) {

    }

    @Override
    public User findById(Long aLong) {
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }
}
