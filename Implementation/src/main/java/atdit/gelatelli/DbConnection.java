package atdit.gelatelli;

import java.sql.*;

/**
 * Hello world!
 */
public class DbConnection {
    private static final String url = "jdbc:mariadb://localhost:3306/eisecafegelatelli";
    private static final String user = "root";
    private static final String password = "password";

    public void test() {
        Connection c = null;
        PreparedStatement s = null;
        ResultSet r = null;

        try {
            c = DriverManager.getConnection(url, user, password);
            System.out.println("works");
            /*r.close();
            s.close();
            c.close();*/

            Statement statement = c.createStatement();
            ResultSet ergebnis = statement.executeQuery("SELECT * FROM flavour WHERE flavour_name = 'chocolate'");

            ergebnis.next();

            System.out.println(Double.parseDouble(ergebnis.getString(2)));

        } catch (SQLException e) {
            System.out.println(
                    "Error"
            );
        }
    }
}
