package org.example.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "Accounts")
public class Account {
    @Id
    private final String id;
    @Column
    private int money;

    public Account() {
        id = UUID.randomUUID()+"";
        setMoney(10000);
    }
    public int getMoney() {
        return money;
    }
    public String getId() {
        return id;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
