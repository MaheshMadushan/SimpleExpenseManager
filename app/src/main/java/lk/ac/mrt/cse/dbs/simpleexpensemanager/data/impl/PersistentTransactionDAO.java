package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHandler;

public class PersistentTransactionDAO implements TransactionDAO{
    private final List<Transaction> transactions;
    private final DatabaseHandler DatabaseHandler;
    private final SQLiteDatabase databaseReadable;
    private final  SQLiteDatabase databaseWritable;
    private final DateFormat dateFormat;

    public PersistentTransactionDAO(DatabaseHandler DatabaseHandler) {
        this.DatabaseHandler = DatabaseHandler;
        this.databaseReadable = DatabaseHandler.getReadableDatabase();
        this.databaseWritable = DatabaseHandler.getWritableDatabase();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        transactions = new LinkedList<>();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHandler.getDate(), this.dateFormat.format(date));
        cv.put(DatabaseHandler.getAccount_no(), accountNo);
        cv.put(DatabaseHandler.getType(), expenseType.toString());
        cv.put(DatabaseHandler.getAmount(), amount);
        //insert new row to transaction table
        databaseWritable.insert(DatabaseHandler.getTransaction_table_name(), null, cv);
    }
    @Override
    public List<Transaction> getAllTransactionLogs() {

        Cursor c = databaseReadable.rawQuery(
                "SELECT * FROM " + DatabaseHandler.getTransaction_table_name() + " ORDER BY " + DatabaseHandler.getDate() + " DESC ",
                null
        );

        ArrayList<Transaction> listOfTransaction = new ArrayList<>();


        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            try {

                Transaction transaction = new Transaction(
                        this.dateFormat.parse(c.getString(1)),
                        c.getString(2),
                        ExpenseType.valueOf(c.getString(3).toUpperCase()),
                        c.getDouble(4)
                );

                listOfTransaction.add(transaction);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        c.close();
        return listOfTransaction;

    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        Cursor c = databaseReadable.rawQuery(
                "SELECT * FROM " + DatabaseHandler.getTransaction_table_name() + " ORDER BY " + DatabaseHandler.getDate() + " DESC " + " LIMIT ?;",
                new String[]{Integer.toString(limit)}
        );

        ArrayList<Transaction> listOfTransaction = new ArrayList<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            try {

                Transaction transaction = new Transaction(
                        this.dateFormat.parse(c.getString(1)),
                        c.getString(2),
                        ExpenseType.valueOf(c.getString(3).toUpperCase()),
                        c.getDouble(4)
                );

                listOfTransaction.add(transaction);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        c.close();
        return listOfTransaction;

    }

}
