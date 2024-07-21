package com.agencia.aeropuerto.infrastructure.out;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.agencia.aeropuerto.domain.entity.Aeropuerto;
import com.agencia.aeropuerto.domain.service.AeropuertoService;

public class AeropuertoRepository implements AeropuertoService {

  private Connection connection;

  public AeropuertoRepository() {
    try {
      Properties props = new Properties();
      props.load(getClass().getClassLoader().getResourceAsStream("configdb.properties"));
      String url = props.getProperty("url");
      String user = props.getProperty("user");
      String password = props.getProperty("password");

      System.out.println("URL: " + url); // Verificar la URL cargada
      System.out.println("User: " + user); // Verificar el usuario cargado
      // N
      connection = DriverManager.getConnection(url, user, password);
      System.out.println("Conexión exitosa!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void createAeropuerto(Aeropuerto aeropuerto) {
      try {
          String query = "INSERT INTO aeropuertos (id, nombre, idciudad) VALUES (?, ?, ?)";
          PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
          ps.setString(1, aeropuerto.getId()); // Asegúrate de que getId() devuelve un valor que no exceda 5 caracteres
          ps.setString(2, aeropuerto.getNombreAero()); // Este campo es VARCHAR(50), verifica que no exceda 50 caracteres
          ps.setString(3, aeropuerto.getIdCiudad()); // Este campo es VARCHAR(5), verifica que no exceda 5 caracteres
  
          ps.executeUpdate();
          System.out.println("Aeropuerto creado con éxito!");
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }


  @Override
  public void updateAeropuerto(Aeropuerto aeropuerto) {
      try {
          String sql = "UPDATE aeropuertos SET nombre = ?, idciudad = ? WHERE id = ?";
  
          PreparedStatement statement = connection.prepareStatement(sql);
          statement.setString(1, aeropuerto.getNombreAero());
          statement.setString(2, aeropuerto.getIdCiudad());
          statement.setString(3, aeropuerto.getId()); // Este es el cuarto parámetro
  
          int rowsUpdate = statement.executeUpdate();
          System.out.println("Aeropuerto actualizado  " + rowsUpdate);
  
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }
  

  @Override
  public Aeropuerto finAeropuerto(String id) {
   Aeropuerto aeropuerto = null;

   try {
    String sql = "SELECT id, nombre, idciudad FROM aeropuertos WHERE id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setString(1, id);
    try (ResultSet resultSet = statement.executeQuery()) {
      if (resultSet.next()){
        aeropuerto = new Aeropuerto();
        aeropuerto.setId(resultSet.getString("id"));
        aeropuerto.setNombreAero(resultSet.getString("nombre"));
        aeropuerto.setIdCiudad(resultSet.getString("idciudad"));
        }
      } 
    }catch (SQLException e){
      e.printStackTrace();
    }
    return aeropuerto;
   }

  @Override
  public Aeropuerto deleteAeropuerto(String id) {
    String query = "Delete FROM aeropuertos WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);
            ps.executeUpdate();
        }

        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
  
      }}