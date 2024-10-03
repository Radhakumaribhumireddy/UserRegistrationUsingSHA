import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    private static final String url="jdbc:mysql://localhost:3306/registration";
    private static final String username="root";
    private static final String password="radha";

    public static String sha(String input) throws NoSuchAlgorithmException {
        MessageDigest md=MessageDigest.getInstance("MD5");
        byte[] messageDigest=md.digest(input.getBytes());
        BigInteger bigInt=new BigInteger(1,messageDigest);
        return bigInt.toString(16);
    }

    public static void main(String[] args)
        {
            try (Scanner sc = new Scanner(System.in);
                 Connection con = DriverManager.getConnection(url, username, password);
                 PreparedStatement pstmt = con.prepareStatement("select * from SHA where username=?");
                 PreparedStatement ps = con.prepareStatement("insert into SHA values(?, sha(?))")) {

                Class.forName("com.mysql.cj.jdbc.Driver");

                // Asking user for username and password
                System.out.print("Enter username: ");
                String user = sc.nextLine();
                System.out.print("Enter Password: ");
                String pw = sc.nextLine();

                // Setting the username for the SELECT query
                pstmt.setString(1, user);

                // Executing the query and processing the ResultSet
                ResultSet rs = pstmt.executeQuery();
                if (rs.next())
                {
                    System.out.println("Username found: " + rs.getString(1)); // Adjust column index if needed

                }
                else {
                    System.out.println("Username not found.");
                    // Now performing the INSERT query with the hashed password
                    ps.setString(1, user); // Setting username
                    ps.setString(2, pw);   // Setting password to be hashed

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Registered Successfully");
                    } else {
                        System.out.println("Failed to register");
                    }

                }



            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("Driver not found: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Exception: " + e.getMessage());
            }
        }

}