import java.sql.*;
import java.util.ArrayList;

import CIM.*;


public class SQLdatabase {

	// JDBC driver name and database URL
	 private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	 private static final String DB_URL = "jdbc:mysql://localhost/";
	 private static final String DISABLE_SSL = "?useSSL=false";
	 // Database credentials
	 private static final String USER = "root";
	 private static final String PASS = "root"; // insert the password to SQL server
	 private String sql = null;
	 
	 private ArrayList<CircuitBreaker> breakerList;
	 private ArrayList<BaseVoltage> bVoltList;
	 private ArrayList<Substation> subList;
	 private ArrayList<VoltageLevel> voltLvlList;
	 private ArrayList<SynchronousMachine> synMach;
	 private ArrayList<GeneratingUnit> genUnit;
	 private ArrayList<RegulatingControl> regControl;
	 private ArrayList<PowerTransformer> powtrafo;
	 private ArrayList<PowerTransformerEnd> powtrafoEnd;
	 private ArrayList<EnergyConsumer> energCons;
	 private ArrayList<RatioTapChanger> ratiotap;
	 
	 
	 public SQLdatabase(ArrayList<CircuitBreaker> breakerList, ArrayList<BaseVoltage> bVoltList,ArrayList<Substation> subList,
			 ArrayList<VoltageLevel> voltLvlList,ArrayList<SynchronousMachine> synMach,ArrayList<GeneratingUnit> genUnit,
			 ArrayList<RegulatingControl> regControl, ArrayList<PowerTransformer> powtrafo, ArrayList<PowerTransformerEnd> powtrafoEnd,
			 ArrayList<EnergyConsumer> energCons,ArrayList<RatioTapChanger> ratiotap ){
		 this.breakerList=breakerList;
		 this.bVoltList=bVoltList;
		 this.subList=subList;
		 this.voltLvlList=voltLvlList;
		 this.synMach=synMach;
		 this.genUnit=genUnit;
		 this.regControl=regControl;
		 this.powtrafo=powtrafo;
		 this.powtrafoEnd=powtrafoEnd;
		 this.energCons=energCons;
		 this.ratiotap=ratiotap;
		 create();
	 }

	public void create() {
	 Connection conn = null;
	 Statement stmt = null;
	 try{
	 // Register JDBC driver
	 Class.forName(JDBC_DRIVER);
	 // Open a connection
	 System.out.println("Connecting to SQL server...");
	 conn = DriverManager.getConnection(DB_URL+DISABLE_SSL, USER, PASS);

	// execute a query to create database
	 System.out.println("Creating database...");
	 stmt = conn.createStatement(); 
     sql = "DROP DATABASE IF EXISTS SystemDatabase";
     stmt.executeUpdate(sql);
	 stmt = conn.createStatement();
	 sql = "CREATE DATABASE IF NOT EXISTS SystemDatabase"; 
	 stmt.executeUpdate(sql);
	 System.out.println("Database created successfully...");	
	 conn = DriverManager.getConnection(DB_URL + "SystemDatabase"+DISABLE_SSL, USER, PASS);
	 sql = "USE SystemDatabase";
	 stmt.executeUpdate(sql);
	 
	 
	 //Make table basevoltage
	 sql = "DROP TABLE IF EXISTS basevoltage";
	 stmt.executeUpdate(sql);
	 sql = "CREATE TABLE IF NOT EXISTS basevoltage(rdfID VARCHAR(40) NOT NULL, nominalvalue DOUBLE, PRIMARY KEY(rdfID))";
	 stmt.executeUpdate(sql) ; // execute query
	 // insert values into the table
	 for (int i = 0; i < bVoltList.size(); i++) {
		 String query = "INSERT INTO basevoltage VALUES(?,?)";
		 PreparedStatement preparedStmt = conn.prepareStatement(query);
		 preparedStmt.setString(1,bVoltList.get(i).getRdfID());
		 preparedStmt.setDouble(2,bVoltList.get(i).getNominalValue());
		 preparedStmt.executeUpdate();			 
		}

	 
	 //Make table substation
	 sql = "DROP TABLE IF EXISTS substation";
	 stmt.executeUpdate(sql);
	 sql = "CREATE TABLE IF NOT EXISTS substation(rdfID VARCHAR(40) NOT NULL, name VARCHAR(40), region_rdfid VARCHAR(40), PRIMARY KEY(rdfID))";
	 stmt.executeUpdate(sql) ; // execute query
	 // insert values into the table
	 for (int i = 0; i < subList.size(); i++) {
		 String query = "INSERT INTO substation VALUES(?,?,?)";
		 PreparedStatement preparedStmt = conn.prepareStatement(query);
		 preparedStmt.setString(1,subList.get(i).getRdfID());
		 preparedStmt.setString(2,subList.get(i).getName());
		 preparedStmt.setString(3,subList.get(i).getRegion());
		 preparedStmt.executeUpdate();			 
		}
		

	//Make table voltagelevel
		 sql = "DROP TABLE IF EXISTS voltagelevel";
		 stmt.executeUpdate(sql);		 
		 sql = "CREATE TABLE IF NOT EXISTS voltagelevel(rdfID VARCHAR(40) NOT NULL, name VARCHAR(40), substation_rdfID VARCHAR(40), "
		 		+ "baseVoltage_rdfID VARCHAR(40), PRIMARY KEY(rdfID), FOREIGN KEY (substation_rdfID) REFERENCES substation(rdfID), "
		 		+ "FOREIGN KEY (baseVoltage_rdfID) REFERENCES basevoltage(rdfID))";
		 stmt.executeUpdate(sql) ; // execute query
		 // insert values into the table
		 for (int i = 0; i < voltLvlList.size(); i++) {
			 String query = "INSERT INTO voltagelevel VALUES(?,?,?,?)";
			 PreparedStatement preparedStmt = conn.prepareStatement(query);
			 preparedStmt.setString(1,voltLvlList.get(i).getRdfID());
			 preparedStmt.setString(2,voltLvlList.get(i).getName());
			 preparedStmt.setString(3,voltLvlList.get(i).getSubstation());
			 preparedStmt.setString(4,voltLvlList.get(i).getBaseVoltage());
			 preparedStmt.executeUpdate();			 
			}


			//Make table powertransformer
		 sql = "DROP TABLE IF EXISTS powertransformer";
		 stmt.executeUpdate(sql);		 
		 sql = "CREATE TABLE IF NOT EXISTS powertransformer(rdfID VARCHAR(40) NOT NULL, name VARCHAR(40), "
		 		+ "equipmentContainer_rdfID VARCHAR(40), PRIMARY KEY(rdfID), FOREIGN KEY (equipmentContainer_rdfID) REFERENCES substation(rdfID))";		 		
		 stmt.executeUpdate(sql) ; // execute query
		 // insert values into the table
		 for (int i = 0; i < powtrafo.size(); i++) {
			 String query = "INSERT INTO powertransformer VALUES(?,?,?)";
			 PreparedStatement preparedStmt = conn.prepareStatement(query);
			 preparedStmt.setString(1,powtrafo.get(i).getRdfID());
			 preparedStmt.setString(2,powtrafo.get(i).getName());
			 preparedStmt.setString(3,powtrafo.get(i).getEquipmentContainer());
			 preparedStmt.executeUpdate();			 
			}
		 
		 
			//Make table powertransformerend
		 sql = "DROP TABLE IF EXISTS powertransformerend";
		 stmt.executeUpdate(sql);		 
		 sql = "CREATE TABLE IF NOT EXISTS powertransformerend(rdfID VARCHAR(40) NOT NULL, name VARCHAR(40), "
		 		+ "transformerR DOUBLE, transformerX DOUBLE, transformer_rdfID VARCHAR(40), basevoltage_rdfID VARCHAR(40),"
		 		+ " PRIMARY KEY(rdfID), FOREIGN KEY (transformer_rdfID) REFERENCES powertransformer(rdfID), "
		 		+ " FOREIGN KEY (basevoltage_rdfID) REFERENCES basevoltage(rdfID))";		 		
		 stmt.executeUpdate(sql) ; // execute query
		 // insert values into the table
		 for (int i = 0; i < powtrafoEnd.size(); i++) {
			 String query = "INSERT INTO powertransformerend VALUES(?,?,?,?,?,?)";
			 PreparedStatement preparedStmt = conn.prepareStatement(query);
			 preparedStmt.setString(1,powtrafoEnd.get(i).getRdfID());
			 preparedStmt.setString(2,powtrafoEnd.get(i).getName());
			 preparedStmt.setDouble(3,powtrafoEnd.get(i).getR());
			 preparedStmt.setDouble(4,powtrafoEnd.get(i).getX());
			 preparedStmt.setString(5,powtrafoEnd.get(i).getPowerTransformer());
			 preparedStmt.setString(6,powtrafoEnd.get(i).getBaseVoltage());
			 preparedStmt.executeUpdate();	
		 }
	 
		 
		 //Make table breakers
		 sql = "DROP TABLE IF EXISTS breakers";
		 stmt.executeUpdate(sql);
		 sql = "CREATE TABLE IF NOT EXISTS breakers(rdfID VARCHAR(40) NOT NULL, name VARCHAR(40), "
		 		+ "state BOOLEAN, equipmentcontainerrdfID VARCHAR(40), PRIMARY KEY(rdfID), "
		 		+ "FOREIGN KEY (equipmentcontainerrdfID) REFERENCES voltagelevel(rdfID))";
		 stmt.executeUpdate(sql) ; // execute query
		 // insert values into the table
		 for (int i = 0; i < breakerList.size(); i++) {
			 String query = "INSERT INTO breakers VALUES(?,?,?,?)";
			 PreparedStatement preparedStmt = conn.prepareStatement(query);
			 preparedStmt.setString(1,breakerList.get(i).getRdfID());
			 preparedStmt.setString(2,breakerList.get(i).getName());
			 preparedStmt.setBoolean(3,breakerList.get(i).isState());
			 preparedStmt.setString(4,breakerList.get(i).getEquipmentContainer());
			 preparedStmt.executeUpdate();			 
			}
	 
		 
		 //Make table energyconsumer
		 sql = "DROP TABLE IF EXISTS energyconsumer";
		 stmt.executeUpdate(sql);
		 sql = "CREATE TABLE IF NOT EXISTS energyconsumer(rdfID VARCHAR(40) NOT NULL, name VARCHAR(40), "
		 		+ "p DOUBLE,q DOUBLE, equipmentcontainerrdfID VARCHAR(40), PRIMARY KEY(rdfID), "
		 		+ "FOREIGN KEY (equipmentcontainerrdfID) REFERENCES voltagelevel(rdfID))";
		 stmt.executeUpdate(sql) ; // execute query
		 // insert values into the table
		 for (int i = 0; i < energCons.size(); i++) {
			 String query = "INSERT INTO energyconsumer VALUES(?,?,?,?,?)";
			 PreparedStatement preparedStmt = conn.prepareStatement(query);
			 preparedStmt.setString(1,energCons.get(i).getRdfID());
			 preparedStmt.setString(2,energCons.get(i).getName());
			 preparedStmt.setDouble(3,energCons.get(i).getP());
			 preparedStmt.setDouble(4,energCons.get(i).getQ());
			 preparedStmt.setString(5,energCons.get(i).getEquipmentContainer());
			 preparedStmt.executeUpdate();			 
			}
		 
		//Make table ratiotapchanger
		 sql = "DROP TABLE IF EXISTS ratiotapchanger";
		 stmt.executeUpdate(sql);
		 sql = "CREATE TABLE IF NOT EXISTS ratiotapchanger(rdfID VARCHAR(40) NOT NULL, name VARCHAR(40), "
		 		+ "step DOUBLE, PRIMARY KEY(rdfID))";		 		
		 stmt.executeUpdate(sql) ; // execute query
		 // insert values into the table
		 for (int i = 0; i < ratiotap.size(); i++) {
			 String query = "INSERT INTO ratiotapchanger VALUES(?,?,?)";
			 PreparedStatement preparedStmt = conn.prepareStatement(query);
			 preparedStmt.setString(1,ratiotap.get(i).getRdfID());
			 preparedStmt.setString(2,ratiotap.get(i).getName());
			 preparedStmt.setDouble(3,ratiotap.get(i).getStep());			
			 preparedStmt.executeUpdate();			 
			}
	 
		//Make table generatingunit
		 sql = "DROP TABLE IF EXISTS generatingunit";
		 stmt.executeUpdate(sql);
		 sql = "CREATE TABLE IF NOT EXISTS generatingunit(rdfID VARCHAR(40) NOT NULL, name VARCHAR(40), "
		 		+ "maxP DOUBLE,minP DOUBLE, equipmentcontainerrdfID VARCHAR(40), PRIMARY KEY(rdfID), "
		 		+ "FOREIGN KEY (equipmentcontainerrdfID) REFERENCES substation(rdfID))";
		 stmt.executeUpdate(sql) ; // execute query
		 // insert values into the table
		 for (int i = 0; i < genUnit.size(); i++) {
			 String query = "INSERT INTO generatingunit VALUES(?,?,?,?,?)";
			 PreparedStatement preparedStmt = conn.prepareStatement(query);
			 preparedStmt.setString(1,genUnit.get(i).getRdfID());
			 preparedStmt.setString(2,genUnit.get(i).getName());
			 preparedStmt.setDouble(3,genUnit.get(i).getPmax());
			 preparedStmt.setDouble(4,genUnit.get(i).getPmin());
			 preparedStmt.setString(5,genUnit.get(i).getEquipmentContainer());
			 preparedStmt.executeUpdate();			 
			}
	 
			//Make table regulatingcontrol
		 sql = "DROP TABLE IF EXISTS regulatingcontrol";
		 stmt.executeUpdate(sql);
		 sql = "CREATE TABLE IF NOT EXISTS regulatingcontrol(rdfID VARCHAR(40) NOT NULL, name VARCHAR(40), "
		 		+ "targetvalue DOUBLE, PRIMARY KEY(rdfID))"; 
		 stmt.executeUpdate(sql) ; // execute query
		 // insert values into the table
		 for (int i = 0; i < regControl.size(); i++) {
			 String query = "INSERT INTO regulatingcontrol VALUES(?,?,?)";
			 PreparedStatement preparedStmt = conn.prepareStatement(query);
			 preparedStmt.setString(1,regControl.get(i).getRdfID());
			 preparedStmt.setString(2,regControl.get(i).getName());
			 preparedStmt.setDouble(3,regControl.get(i).getTargetValue());			
			 preparedStmt.executeUpdate();			 
			}
	 
		//Make table synchronousmachine
		 sql = "DROP TABLE IF EXISTS synchronousmachine";
		 stmt.executeUpdate(sql);
		 sql = "CREATE TABLE IF NOT EXISTS synchronousmachine(rdfID VARCHAR(40) NOT NULL, name VARCHAR(40), "
		 		+ "p DOUBLE,q DOUBLE,ratedS DOUBLE,genUnit_rdfID VARCHAR(40),regControl_rdfID VARCHAR(40), "
		 		+ "equipmentcontainerrdfID VARCHAR(40), PRIMARY KEY(rdfID), "
		 		+ "FOREIGN KEY (genUnit_rdfID) REFERENCES generatingunit(rdfID), "
		 		+ "FOREIGN KEY (regControl_rdfID) REFERENCES regulatingcontrol(rdfID), "
		 		+ "FOREIGN KEY (equipmentcontainerrdfID) REFERENCES voltagelevel(rdfID))";
		 stmt.executeUpdate(sql) ; // execute query
		 // insert values into the table
		 for (int i = 0; i < synMach.size(); i++) {
			 String query = "INSERT INTO synchronousmachine VALUES(?,?,?,?,?,?,?,?)";
			 PreparedStatement preparedStmt = conn.prepareStatement(query);
			 preparedStmt.setString(1,synMach.get(i).getRdfID());
			 preparedStmt.setString(2,synMach.get(i).getName());
			 preparedStmt.setDouble(3,synMach.get(i).getP());
			 preparedStmt.setDouble(4,synMach.get(i).getQ());
			 preparedStmt.setDouble(5,synMach.get(i).getRatedS());
			 preparedStmt.setString(6,synMach.get(i).getGenUnit());
			 preparedStmt.setString(7,synMach.get(i).getRegControl());
			 preparedStmt.setString(8,synMach.get(i).getEquipmentContainer());
			 preparedStmt.executeUpdate();			 
			}	 
	

	 }catch(SQLException se){
	 //Handle errors for JDBC
	 se.printStackTrace();
	 }catch(Exception e){
	 //Handle errors for Class.forName
	 e.printStackTrace();}
	 System.out.println("Goodbye!");	 	 
	}


}


