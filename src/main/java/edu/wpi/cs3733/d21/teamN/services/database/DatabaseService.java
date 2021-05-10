package edu.wpi.cs3733.d21.teamN.services.database;

import com.google.gson.Gson;
import com.google.inject.Inject;
import edu.wpi.cs3733.d21.teamN.form.Form;
import edu.wpi.cs3733.d21.teamN.services.algo.Edge;
import edu.wpi.cs3733.d21.teamN.services.algo.Node;
import edu.wpi.cs3733.d21.teamN.services.database.requests.Request;
import edu.wpi.cs3733.d21.teamN.services.database.requests.RequestType;
import edu.wpi.cs3733.d21.teamN.services.database.users.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseService {
  private final Connection connection;
  /*
   database service class. This class will be loaded as a Singleton by Guice.
  */
  private static User currentUser;
  /*
   database service class. This class will be loaded as a Singleton by Guice.
  */
  @Inject UsersTable usersTable;
  @Inject NodesTable nodesTable;
  @Inject EdgesTable edgesTable;
  @Inject CovidFormsTable covidTable;
  @Inject RequestsTable requestsTable;
  @Inject AppointmentsTable appointmentsTable;
  @Inject AppointmentTypesTable appointmentTypesTable;
  @Inject FormsTable formsTable;
  private Statement stmt;

  @Inject
  DatabaseService(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /// NODES

  /**
   * retrieves all nodes from the database
   *
   * @return all nodes in the database as a HashSet
   */
  public HashSet<Node> getAllNodes() {
    return nodesTable.getAllNodes();
  }

  /**
   * retrieves all nodes from the database
   *
   * @return all nodes in the database as a HashMap
   */
  public HashMap<String, Node> getAllNodesMap() {
    return nodesTable.getAllNodesMap();
  }

  /**
   * retrieves single node from database
   *
   * @param nodeID the ID of the node that you want to retrieve
   * @return a Node of type Node from the database
   */
  public Node getNode(String nodeID) {
    return nodesTable.getNode(nodeID);
  }

  /**
   * adds node to the database
   *
   * @param node a Node of type Node to add to the database
   * @return whether the operation was carried out successfully
   */
  public boolean addNode(Node node) {
    return nodesTable.addNode(node);
  }

  /**
   * updates node in database based on given ID (you cannot change the ID of a node once set)
   *
   * @param id the ID of the desired node to be changed
   * @param x X position of the node
   * @param y Y position of the node
   * @param floor Floor the node is on
   * @param building The building where is in
   * @param nodeType Type of the node
   * @param longName Long name of the node
   * @param shortName Short name of the node
   * @return whether the operation was carried out successfully
   */
  public boolean updateNode(
      String id,
      double x,
      double y,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    return nodesTable.updateNode(id, x, y, floor, building, nodeType, longName, shortName);
  }

  /**
   * deletes a single node from the database
   *
   * @param nodeID the ID of the node you want to delete
   * @return whether the operation was carried out successfully
   */
  public boolean deleteNode(String nodeID) {
    return nodesTable.deleteNode(nodeID);
  }

  /** DELETES ALL DATA IN NODES TABLE */
  public void deleteNodeRows() {
    nodesTable.deleteNodeRows();
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------------------

  /**
   * retrieves all edges from the database
   *
   * @return all edges in the database as a HashSet
   */
  public HashSet<Edge> getAllEdges() {
    return edgesTable.getAllEdges();
  }

  /**
   * retrieves all edges from the database
   *
   * @return all edges in the database as a HashMap
   */
  public HashMap<String, Edge> getAllEdgesMap() {
    return edgesTable.getAllEdgesMap();
  }

  /**
   * adds an edge to the database
   *
   * @param edge an Edge of type Edge to add to the database
   * @return whether the operation was carried out successfully
   */
  public boolean addEdge(Edge edge) {
    return edgesTable.addEdge(edge);
  }

  /**
   * retrieves a single edge from the database
   *
   * @param edgeID the ID of the desired edge
   * @return an Edge of type Edge from the database
   */
  public Edge getEdge(String edgeID) {
    return edgesTable.getEdge(edgeID);
  }

  /**
   * updates an existing edge in the database (cannot change the ID of an existing edge)
   *
   * @param edgeID the ID of the edge you want to change
   * @param startNodeID the valid ID of a node
   * @param endNodeID the valid ID of a node
   * @return whether the operation was carried out successfully
   */
  public boolean updateEdge(String edgeID, String startNodeID, String endNodeID) {
    return edgesTable.updateEdge(edgeID, startNodeID, endNodeID);
  }

  /**
   * deletes specified edge from the database
   *
   * @param edgeID the ID of the edge you want to delete
   * @return whether the operation was carried out successfully
   */
  public boolean deleteEdge(String edgeID) {
    return edgesTable.deleteEdge(edgeID);
  }

  /**
   * retrieves all edges with the specified start node
   *
   * @param startNodeID the ID of a valid node
   * @return all of the edges with a start node matching input or null if invalid ID
   */
  public HashSet<Edge> getEdgesFromStartNode(String startNodeID) {
    return edgesTable.getEdgesFromStartNode(startNodeID);
  }

  /**
   * retrieves all edges with the specified end node
   *
   * @param endNodeID the ID of a valid node
   * @return all of the edges with an end node matching input or null if invalid ID
   */
  public HashSet<Edge> getEdgesFromEndNode(String endNodeID) {
    return edgesTable.getEdgesFromEndNode(endNodeID);
  }

  /** DELETES ALL DATA IN EDGES TABLE */
  public void deleteEdgeRows() {
    edgesTable.deleteEdgeRows();
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------------------

  /**
   * adds a request to the database
   *
   * @param request a Request of type Request to add to the database
   * @return whether the operation was carried out successfully
   */
  public boolean addRequest(Request request) {
    return requestsTable.addRequest(request, currentUser);
  }

  /**
   * deletes specified request from database
   *
   * @param requestID the ID of the request you want to delete
   * @return whether the operation was carried out successfully
   */
  public boolean deleteRequest(int requestID) {
    return requestsTable.deleteRequest(requestID);
  }

  /**
   * retrieves all requests from the database
   *
   * @return all requests in the database as a HashSet
   */
  public HashSet<Request> getAllRequests() {
    return requestsTable.getAllRequests();
  }

  /**
   * retrieves a single request from the database
   *
   * @param requestID the ID of the desired request
   * @return a Request of type Request from the database
   */
  public Request getRequest(int requestID) {
    return requestsTable.getRequest(requestID);
  }

  /**
   * retrieves all requests with the specified senderID
   *
   * @param senderID the ID of a valid user
   * @return all of the requests with a senderID matching input or null if invalid ID
   */
  public HashSet<Request> getRequestBySender(int senderID) {
    return requestsTable.getRequestBySender(senderID);
  }

  /**
   * retrieves all requests with the specified receiverID
   *
   * @param receiverID the ID of a valid user
   * @return all of the requests with a receiverID matching input or null if invalid ID
   */
  public HashSet<Request> getRequestByReceiver(int receiverID) {
    return requestsTable.getRequestByReceiver(receiverID);
  }

  /**
   * updates an existing request in the database (cannot change the ID of an existing request)
   *
   * @param requestID ID of the request from the db
   * @param type Type of request
   * @param senderID ID of the request sender
   * @param receiverID ID of the one assigned to the request
   * @param content Content of the request
   * @param notes Extra notes about the request
   * @return whether the operation was carried out successfully
   */
  public boolean updateRequest(
      int requestID, RequestType type, int senderID, int receiverID, String content, String notes) {
    return requestsTable.updateRequest(requestID, type, senderID, receiverID, content, notes);
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------------------

  /**
   * retrieves all users from the database
   *
   * @return all users in the database as a HashSet
   */
  public HashSet<User> getAllUsers() {
    return usersTable.getAllUsers();
  }

  /**
   * @param username username of the user to be added
   * @param password the password to be associated with given user
   * @param type the type of the user to be added
   * @return whether or not the operation was carried out successfully
   */
  public boolean addUser(String username, String password, UserType type) {
    return usersTable.addUser(username, password, type);
  }

  /**
   * updates an existing user in the database
   *
   * @param id ID of the user to update (Can not update ID)
   * @param username New username for the user
   * @param password New password for the user
   * @param type New type for the user
   * @return whether or not the operation was carried out successfully
   */
  public boolean updateUser(
      int id, String username, String password, UserType type, UserPrefs userPrefs) {
    return usersTable.updateUser(id, username, password, type, userPrefs);
  }

  public boolean updateUserPrefs(int id, UserPrefs userPrefs) {
    return usersTable.updateUserPrefs(id, userPrefs);
  }

  public boolean updateUserUsernameType(int id, String username, UserType type) {
    return usersTable.updateUserUsernameType(id, username, type);
  }

  /**
   * retrieves a single user from the database with matching ID
   *
   * @param id the ID of desired user
   * @return a User of type User from the database with matching ID
   */
  public User getUserById(int id) {
    return usersTable.getUserById(id);
  }

  /**
   * retrieves a single user from the database with matching username
   *
   * @param username the username of desired user
   * @return a User of type User from the database with matching username
   */
  public User getUserByUsername(String username) {
    return usersTable.getUserByUsername(username);
  }

  /**
   * retrieves all users with matching type from database
   *
   * @param userType a UserType of which you want to get users from
   * @return a HashMap of users with matching type
   */
  public HashSet<User> getUsersByType(UserType userType) {
    return usersTable.getUsersByType(userType);
  }

  /**
   * deletes a single user from the database
   *
   * @param username the username of the desired user to be deleted
   * @return whether the operation was carried out successfully
   */
  public boolean deleteUser(String username) {
    return usersTable.deleteUser(username);
  }

  /**
   * Gives a Hashset of all images associated with all Users
   *
   * @return Hashset of JavaFX Images
   */
  public HashMap<BufferedImage, Integer> getAllFaces() {
    return usersTable.getAllFaces();
  }

  public boolean updateUserImage(int id, BufferedImage image) {
    return usersTable.updateUserImage(id, image);
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------------------

  /**
   * retrieves all CovidForms from the database
   *
   * @return all covid forms in the database as a HashMap
   */
  public HashSet<CovidForm> getAllCovidForms() {
    return covidTable.getAllCovidForms();
  }

  /**
   * retrieves single CovidForm from database
   *
   * @param formId the ID of the form that you want to retrieve
   * @return a covid from of type CovidForm from the database
   */
  public CovidForm getCovidForm(int formId) {
    return covidTable.getCovidForm(formId);
  }

  /**
   * adds a covid form to the database. if a form already exists for that user, deletes it, then
   * adds inputted form.
   *
   * @param form a covid form of type CovidForm to add to the database
   * @return whether the operation was carried out successfully
   */
  public boolean addCovidForm(CovidForm form) {
    HashSet<User> empl = getUsersByType(UserType.EMPLOYEE);
    User[] u = new User[empl.size()];
    empl.toArray(u);
    int userId = u[((int) Math.random() * u.length)].getId();
    return covidTable.addCovidForm(form, userId);
  }

  /**
   * updates covid form isOk section
   *
   * @param id the Id of the form you want to update
   * @param isOk what you want to set isOk to
   * @return whether the operation was carried out successfully
   */
  public boolean updateCovidForm(int id, boolean isOk) {
    return covidTable.updateCovidForm(id, isOk);
  }

  /**
   * retrieves a single CovidForm with the specified UserId
   *
   * @param userId the ID of a valid user with a covid form
   * @return the covid form with matching userId, or null if invalid ID
   */
  public CovidForm getCovidFormByUserId(int userId) {
    return covidTable.getCovidFormByUserId(userId);
  }

  public HashSet<CovidForm> getCovidFormsByEmployeeId(int employeeId) {
    return covidTable.getCovidFormsByEmployeeId(employeeId);
  }

  /**
   * deletes a single form from the database
   *
   * @param formId the ID of the form you want to delete
   * @return whether the operation was carried out successfully
   */
  public boolean deleteCovidForm(int formId) {
    return covidTable.deleteCovidForm(formId);
  }

  /**
   * deletes form with matching UserId from the database
   *
   * @param userId the ID of the user whose form you want to delete
   * @return whether the operation was completed successfully
   */
  public boolean deleteCovidFormByUserId(int userId) {
    return covidTable.deleteCovidFormByUserId(userId);
  }

  /**
   * sets IsProcessed for the Covid Form with matching Id
   *
   * @param id the id of the covid form you want to modify
   * @param isProcessed what you want to set IsProcessed to
   * @return whether the operation was completed successfully
   */
  public boolean setCovidFormIsProcessed(int id, boolean isProcessed) {
    return covidTable.setCovidFormIsProcessed(id, isProcessed);
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------------------

  public boolean addAppointment(Appointment appointment) {
    return appointmentsTable.addAppointment(appointment);
  }

  public HashSet<Appointment> getAllAppointments() {
    return appointmentsTable.getAllAppointments();
  }

  public Appointment getAppointment(int appointmentId) {
    return appointmentsTable.getAppointment(appointmentId);
  }

  public HashSet<Appointment> getAppointmentsByPatientId(int patientId) {
    return appointmentsTable.getAppointmentsByPatientId(patientId);
  }

  public HashSet<Appointment> getAppointmentsByAssignedStaffId(int assignedStaffId) {
    return appointmentsTable.getAppointmentsByAssignedStaffId(assignedStaffId);
  }

  public boolean updateAppointment(Appointment appointment) {
    return appointmentsTable.updateAppointment(appointment);
  }

  public boolean updateAppointment(int appointmentId, Form form) {
    return appointmentsTable.updateAppointment(appointmentId, form);
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------------------

  public boolean addAppointmentType(String type, int formId) {
    return appointmentTypesTable.addAppointmentType(type, formId);
  }

  public HashSet<AppointmentType> getAllAppointmentTypes() {
    return appointmentTypesTable.getAllAppointmentTypes();
  }

  public AppointmentType getAppointmentType(int id) {
    return appointmentTypesTable.getAppointmentType(id);
  }

  public AppointmentType getAppointmentTypeByType(String type) {
    return appointmentTypesTable.getAppointmentTypeByType(type);
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------------------

  public boolean addForm(NamedForm nForm) {
    return formsTable.addForm(nForm);
  }

  public HashSet<NamedForm> getAllForms() {
    return formsTable.getAllForms();
  }

  public NamedForm getForm(int id) {
    return formsTable.getForm(id);
  }

  public NamedForm getFormByName(String name) {
    return formsTable.getFormByName(name);
  }

  public boolean deleteForm(int id) {
    return formsTable.deleteForm(id);
  }

  public boolean updateForm(NamedForm form) {
    return formsTable.updateForm(form);
  }
  /**
   * loads CSV files into database.
   *
   * @param csvPath full path to CSV File. (needs .csv)
   * @param tableName table name in which to import CSV data --Note: table name needs to be in all
   *     caps.--
   * @return whether the operation was carried out successfully
   */
  public boolean loadCSVtoTable(String csvPath, String tableName) {
    String path = Objects.requireNonNull(getClass().getResource(csvPath)).toExternalForm();
    String str =
        "CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
            + tableName
            + "', '"
            + path
            + "', ',', '\"', 'UTF-8',0)";
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      //      e.printStackTrace();
      return false;
    }
  }

  /** initializes all tables in the database */
  public boolean initTables() {
    try {
      String str =
          "CREATE TABLE Nodes( "
              + "id varchar(60), "
              + "xcoord DOUBLE NOT NULL, "
              + "ycoord DOUBLE NOT NULL, "
              + "floor varchar(25), "
              + "building varchar(60), "
              + "nodeType varchar(60), "
              + "longName varchar(60), "
              + "shortName varchar(60), "
              + "PRIMARY KEY (id))";
      stmt.execute(str);
      str =
          "CREATE TABLE Edges( "
              + "id varchar(60), "
              + "startNodeID varchar(60) REFERENCES Nodes (id) ON DELETE CASCADE, "
              + "endNodeID varchar(60) REFERENCES Nodes (id) ON DELETE CASCADE, "
              + "PRIMARY KEY (id))";

      stmt.execute(str);
      str =
          "CREATE TABLE Users("
              + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
              + "firstName varchar(25),"
              + "lastName varchar(30),"
              + "Username varchar(40) NOT NULL UNIQUE, "
              + "Password varchar(40) NOT NULL,"
              + "UserType varchar(15),"
              + "Occupation varchar (35), "
              + "Preferences varchar(300),"
              + "FaceImages blob(16M),"
              + "CONSTRAINT chk_UserType CHECK (UserType IN ('Patient', 'Employee', 'Administrator')),"
              + "CONSTRAINT chk_Occupation CHECK (Occupation IN "
              + "('AUDIO_VISUAL', 'COMPUTER_SERVICE', 'EXTERNAL_PATIENT_TRANSPORTATION', 'FLORAL', 'FOOD_DELIVERY', 'GIFT_DELIVERY', 'INTERNAL_PATIENT_TRANSPORTATION', 'LANGUAGE_INTERPRETER', "
              + "'LAUNDRY', 'MAINTENANCE', 'MEDICINE_DELIVERY', 'RELIGIOUS', "
              + "'SANITATION', 'SECURITY')),"
              + "PRIMARY KEY (id))";
      stmt.execute(str);
      str =
          "CREATE TABLE Requests("
              + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
              + "Type varchar(35), "
              + "SenderID INT NOT NULL REFERENCES Users (id), "
              + "ReceiverID INT REFERENCES Users (id), "
              + "Room varchar(60) NOT NULL REFERENCES Nodes (id),"
              + "Content varchar(700), "
              + "Notes varchar(200), "
              + "CONSTRAINT chk_Type CHECK (Type IN "
              + "('AUDIO_VISUAL', 'COMPUTER_SERVICE', 'EXTERNAL_PATIENT_TRANSPORTATION', 'FLORAL', 'FOOD_DELIVERY', 'GIFT_DELIVERY', 'INTERNAL_PATIENT_TRANSPORTATION', 'LANGUAGE_INTERPRETER', "
              + "'LAUNDRY', 'MAINTENANCE', 'MEDICINE_DELIVERY', 'RELIGIOUS', "
              + "'SANITATION', 'SECURITY')),"
              + "PRIMARY KEY (id))";
      stmt.execute(str);
      str =
          "CREATE TABLE CovidForms("
              + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
              + "UserId INT UNIQUE REFERENCES Users (id) ON DELETE CASCADE, "
              + "AssignedEmployee INT REFERENCES Users (id) ON DELETE CASCADE, "
              + "Q1 BOOLEAN NOT NULL, "
              + "Q2 BOOLEAN NOT NULL, "
              + "Q3 BOOLEAN NOT NULL, "
              + "Q4 BOOLEAN NOT NULL, "
              + "Q5 BOOLEAN NOT NULL, "
              + "Q6 BOOLEAN NOT NULL, "
              + "ExtraInfo varchar(250), "
              + "IsOk BOOLEAN, "
              + "IsProcessed BOOLEAN, "
              + "PRIMARY KEY (id))";
      stmt.execute(str);
      str =
          "CREATE TABLE Forms("
              + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
              + "Name varchar(30) UNIQUE, "
              + "Form BLOB(16M),"
              + "PRIMARY KEY (id))";
      stmt.execute(str);
      str =
          "CREATE TABLE AppointmentTypes("
              + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
              + "Type varchar(70) UNIQUE, "
              + "FormId INT NOT NULL REFERENCES FORMS (id), "
              + "PRIMARY KEY (id))";
      stmt.execute(str);
      str =
          "CREATE TABLE Appointments("
              + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
              + "AppointmentTypeId INT NOT NULL REFERENCES AppointmentTypes (id), "
              + "PatientId INT NOT NULL REFERENCES Users (id), "
              + "AssignedStaffId INT REFERENCES Users (id), "
              + "Form BLOB(16M), "
              + "TimeOfAppointment TIMESTAMP,"
              + "CheckInStatus BOOLEAN, "
              + "AssociatedRoomId varchar(60) REFERENCES NODES (id), "
              + "PRIMARY KEY (id))";
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      //      e.printStackTrace();
      return false;
    }
  }

  /**
   * logs in to the database (used to keep track of sending of service requests)
   *
   * @param username the username of a valid user
   * @param password the password of a the same valid user
   * @return
   */
  public boolean login(String username, String password) {
    String str =
        "SELECT * FROM USERS WHERE username = '" + username + "' AND password = '" + password + "'";
    try {
      ResultSet rs = stmt.executeQuery(str);
      Gson gson = new Gson();
      rs.next();
      UserPrefs userPrefs = gson.fromJson(rs.getString("PREFERENCES"), UserPrefs.class);
      switch (rs.getString("USERTYPE")) {
        case "Patient":
          currentUser = (new Patient(rs.getInt("ID"), rs.getString("USERNAME"), userPrefs));
          break;
        case "Employee":
          currentUser = (new Employee(rs.getInt("ID"), rs.getString("USERNAME"), userPrefs));
          break;
        case "Administrator":
          currentUser = (new Administrator(rs.getInt("ID"), rs.getString("USERNAME"), userPrefs));
          break;
      }
      return true;
    } catch (SQLException e) {
      //      e.printStackTrace();
    }
    return false;
  }

  public void setLoggedInUser(User user) {
    currentUser = user;
  }

  public void logout() {
    currentUser = null;
  }

  public User getCurrentUser() {
    return currentUser;
  }
}
