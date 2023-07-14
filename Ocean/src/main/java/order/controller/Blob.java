package order.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Blob {

	public static void main(String[] args) {
		Blob blob = new Blob().Test();
	}

	public DataSource ds;

	public Blob() {
//		讓這個ＤＡＯ透過DataSource
		try {
			ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/javaFramework");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public Blob Test() {

		try (Connection conn = ds.getConnection()) {
			PreparedStatement ptst = conn.prepareStatement("SELECT COUNT(*) FROM User");
			System.out.println("aaaa");
			// 取得 User 表的記錄數量
			int recordCount = 0;

			try (ResultSet rs = ptst.executeQuery()) {
				if (rs.next()) {
					recordCount = rs.getInt(1);
					System.out.println("Number of records in table User: " + recordCount);
				}
			}

			for (int i = 1; i <= recordCount; i++) {

				String sql1 = "UPDATE User SET profilePicture = ? WHERE userid = " + i;
//				System.out.println(sql1);
				String path = "/Users/J_s_Kai/Desktop/image/" + i + ".jpg";
//				System.out.println(path);
				try (PreparedStatement ps = conn.prepareStatement(sql1);) {

					File file = new File(path);
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(file);
					} catch (FileNotFoundException e) {

						e.printStackTrace();
					}
					ps.setBlob(1, fis);
					int rowCount = ps.executeUpdate();
					System.out.println(rowCount + " row(s) updated!!" + i);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
