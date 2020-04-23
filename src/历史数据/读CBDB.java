package 历史数据;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.nobodxbodon.zhconverter.简繁转换类;
import com.github.nobodxbodon.zhconverter.简繁转换类.目标;

// 参考: https://www.sqlitetutorial.net/sqlite-java/
public class 读CBDB {
  private Connection 连接() {
    Connection 联结 = null;
    try {
      String url = "jdbc:sqlite:/Users/xuanwu/Downloads/CBDB历史人物数据库2018/CBDB_20190424.db";
      联结 = DriverManager.getConnection(url);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return 联结;
  }

  public void 统计姓() {
    String sql = "SELECT c_name_chn, c_surname_chn FROM biog_main";
    Map<String, Integer> 未排序 = new HashMap<>();

    try {
      Connection 联结 = this.连接();
      Statement stmt = 联结.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      List<String> 无姓 = new ArrayList<>();
      while (rs.next()) {
        String 全名 = rs.getString("c_name_chn");
        String 姓 = rs.getString("c_surname_chn");
        if (姓 != null) {
          姓 = 简繁转换类.转换(姓, 目标.简体);
          
          if (未排序.containsKey(姓)) {
            未排序.put(姓, 未排序.get(姓) + 1);
          } else {
            未排序.put(姓, 1);
          }
        } else {
          无姓.add(全名);
        }
      }
      for (String 名 : 无姓) {
        // System.out.println(名);
        String 姓 = 名.substring(0, 1);
        if (未排序.containsKey(姓)) {
          未排序.put(姓, 未排序.get(姓) + 1);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    // 排序参考: https://howtodoinjava.com/sort/java-sort-map-by-values/
    LinkedHashMap<String, Integer> 排序 = new LinkedHashMap<>();

    未排序.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .forEachOrdered(x -> 排序.put(x.getKey(), x.getValue()));

    System.out.println("大姓: " + 排序);
  }

  public void 所有人() {
    String sql = "SELECT c_name_chn, c_surname_chn FROM biog_main";

    try {
      Connection 联结 = this.连接();
      Statement stmt = 联结.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      int 人数 = 0;
      int 问题 = 0;
      while (rs.next()) {
        String 全名 = rs.getString("c_name_chn");
        String 姓 = rs.getString("c_surname_chn");

        if (人数 < 10) {
          System.out.println(全名 + "\t" + 姓);
        } else {
          if (姓 != null && (全名 == null || 全名.isEmpty() || 姓.isEmpty() || 全名.indexOf(姓) != 0)) {
            System.out.println("? " + 全名 + "\t" + 姓);
            问题++;
          }
        }
        人数++;
      }
      System.out.println("人数: " + 人数 + " ?: " + 问题);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    读CBDB 读 = new 读CBDB();
    读.统计姓();
  }
}
