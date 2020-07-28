package tk.deriwotua.service;

import tk.deriwotua.domain.Account;

import java.util.List;

public interface AccountService {

    public void save(Account account);

    public List<Account> findAll();

}
