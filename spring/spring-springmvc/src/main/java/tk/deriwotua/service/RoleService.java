package tk.deriwotua.service;

import tk.deriwotua.domain.Role;

import java.util.List;

public interface RoleService {
    public List<Role> list() ;

    void save(Role role);
}
