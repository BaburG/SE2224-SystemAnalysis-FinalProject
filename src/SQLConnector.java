import java.sql.*;

public class SQLConnector {

    private final String username = "root";
    private final String password = "password";
    private final String url = "jdbc:mysql://@localhost:3306/se2224project";
    private Connection conn;

    public SQLConnector(){
        try {
            conn = DriverManager.getConnection(url, username, password);

            System.out.println("Connected Succesfully");
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public int auth(String user, String pass) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT userid FROM userinfo WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("userid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public ResultSet loadVisits(int userid, int year){
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT * FROM visits WHERE visitorID = ? AND year = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userid);
            pstmt.setInt(2, year);

            rs = pstmt.executeQuery();

            return rs; // Returning the ResultSet for further processing
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null in case of an exception
    }

    public ResultSet loadVisits(int userid){
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT * FROM visits WHERE visitorID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userid);

            rs = pstmt.executeQuery();

            return rs; // Returning the ResultSet for further processing
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null in case of an exception
    }

    public ResultSet loadVisitsWithBestFood(int userid){
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT * FROM visits WHERE visitorID = ? and best = 'food' Order By rating DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userid);

            rs = pstmt.executeQuery();

            return rs; // Returning the ResultSet for further processing
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null in case of an exception
    }

    public ResultSet mostVisit(int userid){
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT country FROM visits WHERE visitorID = ? GROUP BY country HAVING COUNT(*) = ( SELECT COUNT(*) FROM visits WHERE visitorID = ? GROUP BY country ORDER BY COUNT(*) DESC LIMIT 1)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userid);
            pstmt.setInt(2, userid);

            rs = pstmt.executeQuery();

            return rs; // Returning the ResultSet for further processing
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null in case of an exception
    }

    public ResultSet springVisit(int userid){
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT country FROM visits WHERE season = 'Spring' AND visitorID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userid);

            rs = pstmt.executeQuery();

            return rs; // Returning the ResultSet for further processing
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null in case of an exception
    }

    public void removeVisit(int id){
        PreparedStatement pstmt = null;
        try {
            String sql = "Delete from visits where locationID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVisit(String country,String city,int year,String season,String best,String user_comment,int rating,int visitorID){
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE visits SET country = ?, city = ?, year = ?, season = ?, best = ?, user_comment = ?, rating = ? WHERE locationID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(3, year);
            pstmt.setInt(7, rating);
            pstmt.setInt(8, visitorID);
            pstmt.setString(1, country);
            pstmt.setString(2, city);
            pstmt.setString(4, season);
            pstmt.setString(5, best);
            pstmt.setString(6, user_comment);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addVisit(String country, String city, int year, String season, String best, String user_comment, int rating, int visitorID){
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO visits (country, city, year, season, best, user_comment, rating, visitorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, country);
            pstmt.setString(2, city);
            pstmt.setInt(3, year);
            pstmt.setString(4, season);
            pstmt.setString(5, best);
            pstmt.setString(6, user_comment);
            pstmt.setInt(7, rating);
            pstmt.setInt(8, visitorID);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close PreparedStatement here if necessary
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet fetchUsers(int userid){
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT userID, username FROM userinfo WHERE userID != ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userid);

            rs = pstmt.executeQuery();

            return rs; // Returning the ResultSet for further processing
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null in case of an exception
    }

    public void addSharedVisit(int ownerID, int visitID, int sharedPersonID) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO sharedvisits (sharingPersonID, sharedLocationID, sharedPersonID) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ownerID);
            pstmt.setInt(2, visitID);
            pstmt.setInt(3, sharedPersonID);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close PreparedStatement here if necessary
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet loadSharedVisits(int userid){
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "SELECT su.username AS sharingPersonUsername,v.locationID as locationID, v.country as country, v.city as city, v.year as year, v.season as season, v.best as best, v.user_comment as comment, v.rating as rating FROM sharedvisits sv JOIN visits v ON sv.sharedLocationID = v.locationID JOIN userinfo su ON sv.sharingPersonID = su.userid WHERE sv.sharedPersonID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userid);

            rs = pstmt.executeQuery();

            return rs; // Returning the ResultSet for further processing
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null in case of an exception
    }
}
