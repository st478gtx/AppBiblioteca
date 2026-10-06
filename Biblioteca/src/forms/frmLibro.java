package forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import clases.Libro;
import colecciones.DatosLibro;

import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.awt.event.ActionEvent;

public class frmLibro extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JLabel lblTitulo;
	private JLabel lblAutor;
	private JLabel lblISBN;
	private JLabel lblCategoría;
	private JTable tbData;
	private JTextField txtTitulo;
	private JTextField txtAutor;
	private JTextField txtIsbn;
	private JComboBox cbxCategoria;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JTextField txtAnioPublicacion;
	private JTextField txtCantidadTotal;
	private JButton btnAgregar;
	private JButton btnActualizar;
	private JButton btnEliminar;
	private JTextField txtBusqueda;
	private JButton btnBuscar;

	private DefaultTableModel modelo;

	public static DatosLibro dataLibro = new DatosLibro();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			frmLibro dialog = new frmLibro();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public frmLibro() {
		setTitle("Mantenimiento de Libro");
		setBounds(100, 100, 798, 422);
		getContentPane().setLayout(null);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(60, 170, 668, 202);
		getContentPane().add(scrollPane);

		tbData = new JTable();
		tbData.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Imagen", "T\u00EDtulo", "Autor",
				"ISBN", "Categor\u00EDa", "Publicaci\u00F3n", "Cantidad total" }));
		scrollPane.setViewportView(tbData);

		lblTitulo = new JLabel("Título");
		lblTitulo.setBounds(112, 57, 46, 14);
		getContentPane().add(lblTitulo);

		lblAutor = new JLabel("Autor");
		lblAutor.setBounds(112, 82, 46, 14);
		getContentPane().add(lblAutor);

		lblISBN = new JLabel("ISBN");
		lblISBN.setBounds(112, 107, 46, 14);
		getContentPane().add(lblISBN);

		lblCategoría = new JLabel("Categoría");
		lblCategoría.setBounds(112, 132, 69, 14);
		getContentPane().add(lblCategoría);

		txtTitulo = new JTextField();
		txtTitulo.setBounds(186, 54, 86, 20);
		getContentPane().add(txtTitulo);
		txtTitulo.setColumns(10);

		txtAutor = new JTextField();
		txtAutor.setBounds(186, 79, 86, 20);
		getContentPane().add(txtAutor);
		txtAutor.setColumns(10);

		txtIsbn = new JTextField();
		txtIsbn.setBounds(186, 104, 86, 20);
		getContentPane().add(txtIsbn);
		txtIsbn.setColumns(10);

		cbxCategoria = new JComboBox();
		cbxCategoria.setBounds(186, 128, 86, 22);
		getContentPane().add(cbxCategoria);

		lblNewLabel_4 = new JLabel("Año publicación");
		lblNewLabel_4.setBounds(329, 54, 86, 14);
		getContentPane().add(lblNewLabel_4);

		lblNewLabel_5 = new JLabel("Cantidad total");
		lblNewLabel_5.setBounds(329, 79, 86, 14);
		getContentPane().add(lblNewLabel_5);

		txtAnioPublicacion = new JTextField();
		txtAnioPublicacion.setBounds(425, 51, 86, 20);
		getContentPane().add(txtAnioPublicacion);
		txtAnioPublicacion.setColumns(10);

		txtCantidadTotal = new JTextField();
		txtCantidadTotal.setBounds(425, 76, 86, 20);
		getContentPane().add(txtCantidadTotal);
		txtCantidadTotal.setColumns(10);

		btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(this);
		btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		btnAgregar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAgregar.setForeground(new Color(51, 102, 255));
		btnAgregar.setBackground(SystemColor.menu);
		btnAgregar.setBounds(617, 51, 89, 23);
		getContentPane().add(btnAgregar);

		btnActualizar = new JButton("Actualizar");
		btnActualizar.setForeground(new Color(255, 153, 51));
		btnActualizar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnActualizar.setBounds(617, 76, 89, 23);
		getContentPane().add(btnActualizar);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnEliminar.setForeground(new Color(255, 0, 51));
		btnEliminar.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnEliminar.setBounds(617, 101, 89, 23);
		getContentPane().add(btnEliminar);

		txtBusqueda = new JTextField();
		txtBusqueda.addActionListener(this);
		txtBusqueda.setBounds(253, 11, 258, 20);
		getContentPane().add(txtBusqueda);
		txtBusqueda.setColumns(10);

		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(this);
		btnBuscar.setBounds(518, 10, 89, 23);
		getContentPane().add(btnBuscar);

		cargarData();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == txtBusqueda) {
			actionPerformedTxtBusqueda(e);
		}
		if (e.getSource() == btnBuscar) {
			actionPerformedBtnBuscar(e);
		}
		if (e.getSource() == btnAgregar) {
			actionPerformedBtnAgregar(e);
		}
	}

	protected void actionPerformedBtnAgregar(ActionEvent e) {
	}

	public void cargarData() {
		modelo = (DefaultTableModel) tbData.getModel();
		modelo.setRowCount(0);
		for (int i = 0; i < dataLibro.longitud(); i++) {
			Libro l = dataLibro.obtener(i);

			Object[] fila = { l.getRutaPortada(), l.getTitulo(), l.getAutor(), l.getIsnb(), l.getCategoria(),
					l.getAnioPublicacion(), l.getCantidadDisponible(), l.getCantidadTotal()

			};
			modelo.addRow(fila);
		}
	}
	protected void actionPerformedBtnBuscar(ActionEvent e) {
		modelo.setRowCount(0);		
		var a = dataLibro.buscar(txtBusqueda.getText());
		cargarDataTabla(a);		
	}
	
	
	public void cargarDataTabla(ArrayList<Libro> data) {
		for (int i = 0; i < data.size(); i++) {
			Libro l = data.get(i);

			Object[] fila = { l.getRutaPortada(), l.getTitulo(), l.getAutor(), l.getIsnb(), l.getCategoria(),
					l.getAnioPublicacion(), l.getCantidadDisponible(), l.getCantidadTotal()

			};
			modelo.addRow(fila);
		}
	}
	protected void actionPerformedTxtBusqueda(ActionEvent e) {
		modelo.setRowCount(0);		
		var a = dataLibro.buscar(txtBusqueda.getText());
		cargarDataTabla(a);	
	}
}
