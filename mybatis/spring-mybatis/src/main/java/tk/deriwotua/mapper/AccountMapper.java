package tk.deriwotua.mapper;

import tk.deriwotua.domain.Account;

import java.util.List;

public interface AccountMapper {

    public void save(Account account);

    public List<Account> findAll();

}
