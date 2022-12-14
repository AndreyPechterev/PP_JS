package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.entity.Role;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @PersistenceContext
    private EntityManager entityManager;
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Role getRoleByName(String name) {
        return roleDao.findByName(name);
    }

    @Override
    public List<Role> listRoles() {
//        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
        return roleDao.findAll();
    }

    public Role findByName(String name) {
        return entityManager.createQuery("select u FROM Role u WHERe u.name = :id", Role.class)
                .setParameter("id", name)
                .getResultList().stream().findAny().orElse(null);
    }

}
