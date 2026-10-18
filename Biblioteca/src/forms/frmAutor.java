package forms;

import app.BibliotecaContexto;
import clases.Autor;
import service.AutorService;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public final class frmAutor extends JDialog {
    private static final long serialVersionUID = 1L;
    private final AutorService autores;
    private final Runnable actualizarLibros;
    private final JTextField codigo = new JTextField();
    private final JTextField nombre = new JTextField();
    private final JTextField busqueda = new JTextField();
    private final DefaultTableModel modelo = new DefaultTableModel(new Object[][] {}, new String[] {"Código", "Nombre"}) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tabla = new JTable(modelo);

    public frmAutor(BibliotecaContexto contexto, Runnable actualizarLibros) {
        autores = contexto.autores();
        this.actualizarLibros = actualizarLibros;
        setTitle("Mantenimiento autor");
        setBounds(100, 100, 573, 330);
        getContentPane().setLayout(null);

        JLabel cabecera = new JLabel();
        cabecera.setOpaque(true);
        cabecera.setBackground(new Color(0, 128, 192));
        cabecera.setBounds(0, 4, 557, 55);
        getContentPane().add(cabecera);

        busqueda.setBounds(84, 12, 258, 31);
        busqueda.addActionListener(e -> cargar(autores.buscar(busqueda.getText())));
        getContentPane().add(busqueda);
        JButton buscar = boton("Buscar", 364, 17, e -> cargar(autores.buscar(busqueda.getText())));
        buscar.setBorder(null);

        JLabel lblCodigo = new JLabel("Código"); lblCodigo.setBounds(72, 80, 50, 14); getContentPane().add(lblCodigo);
        JLabel lblNombre = new JLabel("Nombre"); lblNombre.setBounds(72, 110, 50, 14); getContentPane().add(lblNombre);
        codigo.setBounds(128, 77, 86, 20); codigo.setEditable(false); getContentPane().add(codigo);
        nombre.setBounds(128, 107, 210, 20); getContentPane().add(nombre);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(72, 138, 363, 138);
        getContentPane().add(scroll);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.convertRowIndexToModel(tabla.getSelectedRow());
                codigo.setText(modelo.getValueAt(fila, 0).toString());
                nombre.setText(modelo.getValueAt(fila, 1).toString());
            }
        });

        JButton agregar = boton("Agregar", 443, 77, e -> ejecutar(() -> autores.registrar(nombre.getText())));
        agregar.setForeground(new Color(51, 102, 255));
        boton("Limpiar", 443, 159, e -> limpiar());
        JButton actualizar = boton("Actualizar", 443, 219,
                e -> ejecutar(() -> autores.modificar(codigo.getText(), nombre.getText())));
        actualizar.setForeground(new Color(255, 153, 51));
        JButton eliminar = boton("Eliminar", 443, 253,
                e -> ejecutar(() -> autores.eliminar(codigo.getText())));
        eliminar.setForeground(new Color(255, 0, 51));
        cargar(autores.listar());
    }

    private JButton boton(String texto, int x, int y, java.awt.event.ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, 89, 23);
        boton.setFont(new Font("Tahoma", Font.PLAIN, 13));
        boton.addActionListener(accion);
        getContentPane().add(boton);
        return boton;
    }
    private void cargar(ArrayList<Autor> lista) {
        modelo.setRowCount(0);
        for (Autor autor : lista) modelo.addRow(new Object[] {autor.getCodigo(), autor.getNombre()});
    }
    private void limpiar() { codigo.setText(""); nombre.setText(""); tabla.clearSelection(); }
    private void ejecutar(Runnable operacion) {
        try {
            operacion.run();
            cargar(autores.listar()); limpiar(); actualizarLibros.run();
        } catch (RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "Autor", JOptionPane.ERROR_MESSAGE);
        }
    }
}
