package org.example.controller;

import org.apache.log4j.Logger;
import org.example.entities.Account;
import org.example.entities.RequestData;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/")
public class AccountController {
    @Autowired
    AccountService accountService;
    public int countTransactions = 0;
    private static final Logger log = Logger.getLogger(AccountController.class);
    public class MyThread extends Thread {
        @Override
        public void run() {
            List<Account> accounts = accountService.findAll();
            int deposit = ThreadLocalRandom.current().nextInt(1,1000);
            int firstAccountId = ThreadLocalRandom.current().nextInt(0,accounts.size()-1);
            int secondAccountId = ThreadLocalRandom.current().nextInt(0,accounts.size()-1);
            if (firstAccountId == secondAccountId)
                secondAccountId++;
            Account firstAccount = accounts.get(firstAccountId);
            Account secondAccount = accounts.get(secondAccountId);
            try{
                if(firstAccount.getMoney()>=deposit){
                    firstAccount.setMoney(firstAccount.getMoney()-deposit);
                    secondAccount.setMoney(secondAccount.getMoney()+deposit);
                    accountService.changeAccount(firstAccount);
                    accountService.changeAccount(secondAccount);
                    countTransactions++;
                    log.info("Success deposit("+deposit+") from account="+
                            firstAccount.getId()+" to account="+secondAccount.getId());
                    sleep(ThreadLocalRandom.current().nextInt(1000,2000));
                    if (countTransactions<30)
                        run();
                }
                else{
                    log.info("In this account="+firstAccount.getId()+
                            ", not enough money. money="+firstAccount.getMoney()+" deposit("+deposit+")");

                    sleep(ThreadLocalRandom.current().nextInt(1000,2000));
                    if (countTransactions<30)
                        run();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    @PostMapping(value = "/transactional_simulation")
    public String transactionalSimulation(@RequestBody RequestData data) throws InterruptedException {
        if (data!=null){
            List<MyThread> threadList = new ArrayList<>();
            for (int i = data.getAccounts();i!= 0;i--){
                accountService.save(new Account());
            }
            for (int i = data.getThreads();i!=0;i--){
                threadList.add(new MyThread());
            }
            for(MyThread th:threadList){
                th.run();
                th.join();
            }
            log.info("30 transactions well done!");
            countTransactions = 0;
            return "30 transactions well done!";

        }
        log.error("Wrong request data!");
        return "Wrong request data!";
    }
    @GetMapping(value = "/getAccounts")
    public List<Account> getAccounts() {
        return accountService.findAll();
    }

    @GetMapping(value = "/deleteAllAccounts")
    public boolean deleteAll() {
        accountService.deleteAll();
        return true;
    }
}
