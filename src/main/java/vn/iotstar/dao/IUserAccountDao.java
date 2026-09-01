package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.entity.UserAccount;

public interface IUserAccountDao {
    void insert(UserAccount user);
    void update(UserAccount user);
    UserAccount findById(Long id);
    UserAccount findByEmail(String email);
    UserAccount findByUsername(String username);
    UserAccount findByUsernameOrEmail(String value);
    List<UserAccount> findAll();
}
