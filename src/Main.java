import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import classloader.CustomClassLoader;
import db.DB;

public class Main {

    private static void testClassloader() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CustomClassLoader customClassLoader = new CustomClassLoader();
        Class<?> cl = customClassLoader.findClass("classloader.DemoClass");
        Object ob = cl.newInstance();
        Method method = cl.getMethod("test1");
        method.invoke(ob);
        method = cl.getMethod("helloWorld", String.class);
        method.invoke(ob, "Classloader");
    }

    private static void dbPrintData(DB db, ResultSet rs) {
        if (rs != null) {
            try {
                System.out.println("------------------------------------");
                while (rs.next()) {
                    System.out.println("ID="+rs.getInt(1)+" NAME="+rs.getString(2) + " COUNTER=" + rs.getInt(3));
                }
            } catch (SQLException e) {
                db.close();
                e.printStackTrace();
            }
        }
    }

    private static void testDB() {
        DB db = new DB();
        db.connect();

        db.createTable("test", "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, counter INTEGER DEFAULT 0)");
        db.update("INSERT INTO test (name, counter) VALUES ('test', 100)");
        db.update("INSERT INTO test (name, counter) VALUES ('test1', 150)");
        db.commit();

        ResultSet rs = db.select("SELECT * from test");
        dbPrintData(db, rs);

        db.update("UPDATE test SET counter = 300 WHERE name = 'test1'");
        rs = db.select("SELECT * from test");
        dbPrintData(db, rs);

        db.update("DELETE FROM test WHERE name = 'test1'");
        rs = db.select("SELECT * from test");
        dbPrintData(db, rs);
        db.commit();

        //Откат транзакции
        db.update("DELETE FROM test WHERE name = 'test'");
        db.rollback();
        rs = db.select("SELECT * from test");
        dbPrintData(db, rs);

        //Некорректный запрос
        db.update("DELETE FROM test WHERE name = 'test1");

        db.close();
    }

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        testClassloader();
        testDB();
    }
}