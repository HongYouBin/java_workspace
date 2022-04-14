package dbms;

import java.sql.*;
import java.util.Hashtable;

public class App {

	public static void main(String[] args) throws SQLException {
		//System.out.println("Hello DBMS");	
		Hashtable<Integer, Float> ht = new Hashtable<Integer, Float>();
		//create connection for a server installed in localhost, with a user "root" with no password
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/dbhw", "root", "watch1998")) {
            // create a Statement
            try (Statement stmt = conn.createStatement()) {
                //execute query
                try (ResultSet rs = stmt.executeQuery("SELECT S.rating, AVG(S.age) "
                		+ "AS average FROM Sailors S WHERE S.age>=18 "
                		+ "GROUP BY S.rating "
                		+ "HAVING 1<(SELECT COUNT(*) FROM Sailors S2 where s.RATING = S2.rating)")) {
                    //position result to first
                    rs.first();
                    while(rs.next()) {
                    System.out.println("등급 : "+rs.getString(1)+' '+"평균 나이 : "+rs.getString(2));
                    ht.put(Integer.parseInt(rs.getString(1)), Float.parseFloat(rs.getString(2)));	
                    }
                    stmt.executeUpdate("UPDATE sailors s "
                    		+ "SET s.age = 30 "
                    		+ "WHERE s.sname = 'Andy'");
                    ResultSet updated_rs = stmt.executeQuery("SELECT S.rating, AVG(S.age) "
                		+ "AS average FROM Sailors S WHERE S.age>=18 "
                		+ "GROUP BY S.rating "
                		+ "HAVING 1<(SELECT COUNT(*) FROM Sailors S2 where s.RATING = S2.rating)");
                    updated_rs.first();
                    while(updated_rs.next()) {
                    	if(ht.get(Integer.parseInt(updated_rs.getString(1)))!=Float.parseFloat(updated_rs.getString(2))) {
                    		System.out.println("Step 1 결과 대비 평균 나이가 변한 등급 : "+updated_rs.getString(1));
                    		
                    	}
                    }
                 }
            }
        }
	}

}
