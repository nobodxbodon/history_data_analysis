package 历史数据;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class 读CBDB {
  public static void 连接() {
    Connection 联结 = null;
    try {
      // db parameters
      String url = "jdbc:sqlite:/Users/xuanwu/Downloads/CBDB历史人物数据库2018/CBDB_20190424.db";
      联结 = DriverManager.getConnection(url);

      System.out.println("Connection to SQLite has been established.");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    } finally {
      try {
        if (联结 != null) {
          联结.close();
        }
      } catch (SQLException ex) {
        System.out.println(ex.getMessage());
      }
    }
  }

  public static void main(String[] args) {
    连接();
  }
}
