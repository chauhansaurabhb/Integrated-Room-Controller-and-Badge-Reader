package deviceImpl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import framework.*;

public class JavaSEProfileDB implements IProfileDB {

	
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	static PreparedStatement ps ;

		private TempStruct tempPreference;
		
	private String queryCreateTable = null;

	
	
	// Configuration Parameter
	// TODO: Device Developers will write USERNAME and PASSWORD to access the database
	static final String USERNAME = "";
	static final String PASSWORD = "";	
	
	//TODO: Should be generated from StringTemplate file
	static final String DBNAME = "ProfileDB";
	

	
		
  public JavaSEProfileDB() {
	  
	  
	 
	  try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/", 
							 USERNAME , PASSWORD );
			
			statement = (Statement) connect.createStatement();
			ps = connect.prepareStatement("CREATE DATABASE IF NOT EXISTS iotsuiteuser ");
			ps.execute();
			
			connect = DriverManager.getConnection("jdbc:mysql://localhost/iotsuiteuser", 
					 USERNAME , PASSWORD );
			
			if(checkTableExists()){ // If table exists				
				 
				 System.out.println("Table exisits in the database.....");	

				 						//setprofile("-400", new TempStruct(-400.0,"C"));
				 						
				}
				else{				
				System.out.println("Table does not exist in the database.....");	
				
				
				ps = connect.prepareStatement("CREATE TABLE iotsuiteuser.ProfileDB " +
				
              "(badgeID VARCHAR(255),tempValue DOUBLE,unitOfMeasurement VARCHAR(255))");
              ps.execute();
				
	  
				//ProfileDB   
	           //generate  profile: TempStruct accessed-by badgeID: String;
				
				

									
								//setprofile("-300", new TempStruct(-100.0,"C"));
								
				}
				
						
			
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
  
  }

     
    
    @Override
    public void setprofile 
        (String newIndex, TempStruct  newprofileValue ) {
      // Use this function, when storage is considered  
  	//  as an actuator - It stores data from various sources. 

  	  try {  
  			ps = 
  				connect.prepareStatement("INSERT INTO ProfileDB " +
  						"(badgeID,tempValue,unitOfMeasurement) VALUES" +
  						" ("+newIndex+","+newprofileValue.gettempValue()+","+newprofileValue.getunitOfMeasurement()+")");
  			/*pstmt.setString(1, newIndex);
    			pstmt.setDouble(2, newprofileValue.gettempValue());
    			pstmt.setString(3, newprofileValue.getunitOfMeasurement());*/
  			ps.executeUpdate();
  			
  			
  		} catch (SQLException e) {
  			e.printStackTrace();
  		}	
  	  
      } 
   
    @Override
    public TempStruct getprofile(String index){
      // Use this function, when  storage is considered as 
  	  // sensor - It allows to reterive stored data.
  	  
  	  try {
  			String query  = "SELECT * FROM  iotsuiteuser.ProfileDB WHERE badgeID=" + index;
  			resultSet = statement.executeQuery(query);
  			resultSet.next();
  			//double tempValue =resultSet.getDouble("tempValue");
  			//String unitOfMeasurement = resultSet.getString("unitOfMeasurement");
  			//tempPreference = new TempStruct(tempValue, unitOfMeasurement);
  			tempPreference = new TempStruct(resultSet.getDouble("tempValue") ,resultSet.getString("unitOfMeasurement")    );
  		} catch (Exception e) {
  			System.err.println("Got an exception! ");
  			System.err.println(e.getMessage());
  		}	
  		return tempPreference;
    
    } 


private boolean checkTableExists() {
	System.out.println("Checking Table exisits or not in the database...");
try {
		DatabaseMetaData md = connect.getMetaData();
		ResultSet rs = md.getTables(null, null, DBNAME, null);

		if (rs.next()){ 
			return true;
		} else {
			return false;
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return false;
}

}