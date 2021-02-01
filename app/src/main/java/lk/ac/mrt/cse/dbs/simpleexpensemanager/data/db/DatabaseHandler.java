package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
    //    db info
    private final static String db_name = "180286E";
    private final static int db_version = 1;

    //    table names
    private final String account_table_name = "account";
    private final String transaction_table_name = "transactions";

    //    account table fields
    private final String account_no = "account_no";
    private final String bank_name = "bank_name";
    private final String account_holder_name = "account_holder_name";
    private final String balance = "balance";

    //    account table fields
    private final String date = "date";
    private final String amount = "amount";
    private final String transaction_id = "transaction_id";
    private final String type = "type";


    public String getAccount_table_name() {
        return account_table_name;
    }

    public String getTransaction_table_name() {
        return transaction_table_name;
    }

    public String getAccount_no() {
        return account_no;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getAccount_holder_name() {
        return account_holder_name;
    }

    public String getBalance() {
        return balance;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getType() {
        return type;
    }

    public DatabaseHandler(@Nullable Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + this.account_table_name + "(" +
                        this.account_no + " TEXT PRIMARY KEY," +
                        this.bank_name + " TEXT NOT NULL," +
                        this.account_holder_name + " TEXT NOT NULL," +
                        this.balance + " REAL" +
                        ");"
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + this.transaction_table_name + "(" +
                        this.transaction_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        this.date + " TEXT NOT NULL," +
                        this.account_no + " TEXT NOT NULL," +
                        this.type + " TEXT NOT NULL," +
                        this.amount + " REAL NOT NULL," +
                        "FOREIGN KEY (" + this.account_no + ") REFERENCES "
                        + this.account_table_name + "(" + this.account_no + ")," +
                        "CHECK ("+this.type+"==\""+"INCOME"+"\" OR "+this.type+"==\""+"EXPENSE"+"\")"+
                        ");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + this.transaction_table_name);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + this.account_table_name);
        onCreate(sqLiteDatabase);
    }
}
