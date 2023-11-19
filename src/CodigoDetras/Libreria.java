package CodigoDetras;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

interface metodos{
    int buscarId(String value);
    int buscarIdGeneros(String value);
    String checkButtonResult(JPanel panel,JTable tabla);
    int generosResult(JPanel panel);
    void editarGenero(int idLista,int ideGenero,JTable tabla);
}

public abstract class Libreria{
    String Nombre,Genero,Autor,Descripcion;
    Connection con;
      public Libreria(){}
    
    public Connection conexion(){
        
        try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con= DriverManager.getConnection("jdbc:mysql://localhost:3306/libreria","root","12345678");
                System.out.println("Se ha conectado a mi base de datos");
            } 
            catch(ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }          
            
        return con;
    }
    
  
    public void creacion(String nombre, String autor, String desc,String genero){
        try {
            conexion();
            java.sql.Statement statement= con.createStatement();
            statement.executeQuery("call agregarLibro('"+nombre+"','"+autor+"','"+desc+"')");
            
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Libreria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void edicion(int identi,String nombre,String autor, String desc,JTable tabla){
        
    }
    
    public void eliminacion(int identi,JTable tabla){}
    
}
