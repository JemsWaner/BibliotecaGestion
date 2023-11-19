package CodigoDetras;
import Interfaz.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Main extends Libreria implements metodos{
    JRadioButton radio;
    public Main(){}
    public int idDeGeneros;
    public String TextoSeleccionadoGenero="Terror";

/////Este metodo actualiza mi tabla, perfecta cuando quiero mostrar lo cambios y de una muestro la tabla denuevo pero con los datos hechos    
    public void mostarTabla(JTable tabla){
        conexion();
        DefaultTableModel model;
        model = (DefaultTableModel) tabla.getModel();
        
        try {
            java.sql.Statement statement= con.createStatement();
//            "select L.id, L.titulo, L.autor, L.descripcion, G.genero from Libros L left join LibrosGeneros LG on L.id= LG.libro_id left join Generos G on G.id= LG.genero_id"
            ResultSet resultado= statement.executeQuery("call tablaCompleta()");
            
            model.setRowCount(0);
            
            while (resultado.next()) {
                Object [] filas= {resultado.getString("L.titulo"),resultado.getString("L.autor"),resultado.getString("L.descripcion"),resultado.getString("G.genero")};
                model.addRow(filas);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
/////Al igual que el metodo MostrarTabla este me filtra/muestra los valores de mi DB que coincidan con un texto deseado    
    public void filtrarTabla(JTable tabla,String text){
        conexion();
        DefaultTableModel model;
        model = (DefaultTableModel) tabla.getModel();
        
        try {
            java.sql.Statement statement= con.createStatement();
            ResultSet resultado= statement.executeQuery("select L.id, L.titulo, L.autor, L.descripcion, G.genero from Libros L left join LibrosGeneros LG on L.id= LG.libro_id left join Generos G on G.id= LG.genero_id where G.genero= '"+text+"' ");
            
            model.setRowCount(0);
            
            while (resultado.next()) {
                Object [] filas= {resultado.getString("L.titulo"),resultado.getString("L.autor"),resultado.getString("L.descripcion"),resultado.getString("G.genero")};
                model.addRow(filas);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
     public void edicion(int identi,String nombre,String autor, String desc,JTable tabla){
         
        try {
            conexion();
            java.sql.Statement statement= con.createStatement();
            statement.executeUpdate("update Libros set titulo='"+nombre+"',autor='"+autor+"',descripcion='"+desc+"' where id="+identi);
            
        } catch (SQLException ex) {
            Logger.getLogger(Libreria.class.getName()).log(Level.SEVERE, null, ex);
        }
        mostarTabla(tabla);
    }
     
    @Override
    public void eliminacion(int identi,JTable tabla){
        try {
            conexion();
            java.sql.Statement statement= con.createStatement();
            statement.executeUpdate("delete from LibrosGeneros where libro_id="+identi);
            statement.executeUpdate("delete from Libros where id="+identi);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Libreria.class.getName()).log(Level.SEVERE, null, ex);
        }
        mostarTabla(tabla);
    }
    
    
/////****////////////Desde aqui abajo implementare los metodos de la interfaz////////*******////////////
    
    /////Este metodo me sirve para agregar los checkbutton de mi DB al panel deseado y agregarle listener a todos
    @Override
    public String checkButtonResult(JPanel panel,JTable tabla) {
        conexion();
        ActionListener radioListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton selectedRadioButton = (JRadioButton) e.getSource();
            String selectedText = selectedRadioButton.getText();
            System.out.println("Radio button seleccionado: " + selectedText);
    ////Preferi llamar al metodo FiltrarTabla aqui porque cuando haya un listener del checkbutton de una vez me imprimira el valor en la tabla de generos
            filtrarTabla(tabla, selectedText);
            TextoSeleccionadoGenero=selectedText;
        }
    };

    try {
        java.sql.Statement statement = con.createStatement();
        ResultSet resultado = statement.executeQuery("SELECT id, genero FROM Generos");

        while (resultado.next()) {
            radio = new JRadioButton(resultado.getString("genero"));
            radio.addActionListener(radioListener);
            panel.add(radio);
        }

    } catch (SQLException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
    return TextoSeleccionadoGenero;
    }

///////////Con este metodo imprimo los generos de mi DB con un check button a mi panel de la tabla principal, solo a esa    
    @Override
    public int generosResult(JPanel panel) {
    conexion();
    ActionListener radioListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton selectedRadioButton = (JRadioButton) e.getSource();
            String selectedText = selectedRadioButton.getText();
            System.out.println("Radio button seleccionado: " + selectedText);
            idDeGeneros = buscarIdGeneros(selectedText);
        }
    };

    try {
        java.sql.Statement statement = con.createStatement();
        ResultSet resultado = statement.executeQuery("SELECT id, genero FROM Generos");

        while (resultado.next()) {
            radio = new JRadioButton(resultado.getString("genero"));
            radio.addActionListener(radioListener);
            panel.add(radio);
        }

    } catch (SQLException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
    return idDeGeneros;
}

/////////Esta busca el id en mi DB de la primera columna clickeado en la fila de mi tabla de usuarios en la interfaz principal EdicionLibros
    @Override
    public int buscarId(String value) {
        int id = 0;
        try {
            conexion();
            java.sql.Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery("SELECT id FROM Libros WHERE titulo='" + value + "'");

            if (result.next()) {
                id = result.getInt("id");
            }

            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Libreria.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    
    @Override
    public int buscarIdGeneros(String value) {
        int id = 0;
        try {
            conexion();
            java.sql.Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery("SELECT id FROM Generos WHERE genero='" + value + "'");

            if (result.next()) {
                id = result.getInt("id");
                System.out.println(id);
            }

            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Libreria.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

//////////////Aqui le agrego un genero obteniendo el id de la fila que he clickeado y el checkbutton de genero
    @Override
    public void editarGenero(int idLista, int ideGenero,JTable tabla) {
        try {
            conexion();
            java.sql.Statement statement = con.createStatement();
            statement.executeUpdate("INSERT INTO LibrosGeneros(libro_id, genero_id) VALUES (" + idLista + "," + ideGenero + ")");

        } 
        catch (SQLException ex) {
            Logger.getLogger(Libreria.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al editar el género: " + ex.getMessage());
        }
        mostarTabla(tabla);
    }


}