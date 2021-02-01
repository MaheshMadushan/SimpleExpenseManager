/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class PersistentAccountDAO implements AccountDAO {
    private final Map<String, Account> accounts;
    private final DatabaseHandler DatabaseHandler;
    private final  SQLiteDatabase databaseReadable;
    private final  SQLiteDatabase databaseWritable;

    public PersistentAccountDAO(DatabaseHandler DatabaseHandler) {
        this.databaseReadable = DatabaseHandler.getReadableDatabase();
        this.databaseWritable = DatabaseHandler.getWritableDatabase();
        this.DatabaseHandler = DatabaseHandler;
        this.accounts = new HashMap<>();
    }

    @Override
    public List<String> getAccountNumbersList() {

        Cursor c = databaseReadable.rawQuery(
                "SELECT "+ DatabaseHandler.getAccount_no() + " FROM " + DatabaseHandler.getAccount_table_name(),
                null
        );

        ArrayList<String> AccNumberList = new ArrayList<>();

        for(c.moveToFirst(); !c.isAfterLast();  c.moveToNext()){
            AccNumberList.add(c.getString(0));
        }

        return AccNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor c = databaseReadable.rawQuery(
                "SELECT * FROM " + DatabaseHandler.getAccount_table_name(),
                null
        );

        ArrayList<Account> AccList = new ArrayList<>();

        for(c.moveToFirst(); !c.isAfterLast();  c.moveToNext()){
            Account acc = new Account(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getDouble(3)
            );

            AccList.add(acc);
        }

        return AccList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor c = databaseReadable.rawQuery(
                "SELECT * FROM " + DatabaseHandler.getAccount_table_name() +
                " WHERE " + DatabaseHandler.getAccount_no() + " =?; ",
                new String[]{accountNo}
        );


        if (c != null && c.moveToFirst()) {
            return new Account(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getDouble(3)
            );


        }else{
            String msg = "Account " + accountNo + " is not in database.";
            throw new InvalidAccountException(msg);
        }


    }

    @Override
    public void addAccount(Account account) throws InvalidAccountException {

        try {
            Account isAccountExist = getAccount(account.getAccountNo());
        } catch (InvalidAccountException e) {
            e.printStackTrace();
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHandler.getAccount_no(), account.getAccountNo());
            cv.put(DatabaseHandler.getBank_name(), account.getBankName());
            cv.put(DatabaseHandler.getAccount_holder_name(), account.getAccountHolderName());
            cv.put(DatabaseHandler.getBalance(), account.getBalance());

            //insert new row to account table
            databaseWritable.insert(DatabaseHandler.getAccount_table_name(), null, cv);
        }


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {



        try {
            Account isAccountExist = getAccount(accountNo);
            String msg = "Account " + accountNo + " is not exist.";
            throw new InvalidAccountException(msg);
        } catch (InvalidAccountException e) {

            databaseWritable.delete(
                    DatabaseHandler.getAccount_table_name(),
                    DatabaseHandler.getAccount_no() + " = ?",
                    new String[]{accountNo}
            );
        }

    }
















    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        try {
            Account account = getAccount(accountNo);
            switch (expenseType) {
                case EXPENSE:
                    account.setBalance(account.getBalance() - amount);
                    break;
                case INCOME:
                    account.setBalance(account.getBalance() + amount);
                    break;
            }
            // if ok query to update the balance in the account table
            databaseWritable.execSQL(
                    "UPDATE " + DatabaseHandler.getAccount_table_name() +
                            " SET " + DatabaseHandler.getBalance() + " = ?" +
                            " WHERE " + DatabaseHandler.getAccount_no() + " = ?",
                    new String[]{String.valueOf(account.getBalance()), accountNo});
        } catch (InvalidAccountException e) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }
}
