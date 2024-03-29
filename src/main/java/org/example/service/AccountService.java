package org.example.service;

import org.example.entities.Account;
import org.example.repository.accountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    accountRepository accRep;
    private static final Logger log = Logger.getLogger(AccountService.class);
    public boolean save(Account account) {
        if (account!=null){
            try{
                accRep.save(account);
                log.info("Created new account with id="+account.getId()
                        +", money="+account.getMoney());
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
        return false;
    }
    public boolean isExist(String id) {
        if (id!=null){
            for (Account a : accRep.findAll())
                if (a.getId().equals(id))
                    return true;
        }
        return false;
    }
    public void deleteAll() {
        accRep.deleteAll();
    }

    public boolean changeAccount(Account account) {
        if (account!=null){
            for (Account a : accRep.findAll()){
                if (a.getId().equals(account.getId())) {
                    try{
                        accRep.save(account);
                        log.info("Changed account with id="+account.getId()+", money="+account.getMoney());
                        return true;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        log.error(e.getMessage());
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public Account getAccount(String id) {
        if (id!=null){
            if (isExist(id))
                return accRep.getOne(id);
        }
        return null;
    }

    public boolean deleteAccount(String id) {
        if (id!=null) {
            if (isExist(id))
            {
                try {
                    accRep.delete(getAccount(id));
                    log.info("Deleted account with id="+id);
                    return true;
                }catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        }
        return false;
    }

    public List<Account> findAll() {return accRep.findAll();}
}
