package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentDemoExpenseManager extends ExpenseManager {
    private DatabaseHandler databaseHandler;

    public PersistentDemoExpenseManager(Context context){
        this.databaseHandler = new DatabaseHandler(context);
        setup();
    }
    @Override
    public void setup(){
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(databaseHandler);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(databaseHandler);
        setAccountsDAO(persistentAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945G", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        try {
            getAccountsDAO().addAccount(dummyAcct1);
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
        try {
            getAccountsDAO().addAccount(dummyAcct2);
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
    }
}