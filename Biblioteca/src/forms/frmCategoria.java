package forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import clases.Categoria;
import clases.Libro;
import colecciones.DatosCategoria;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Cursor;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class frmCategoria extends JDialog implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private JLabel lblCodigo;
	private JLabel lblNombre;
	private JButton btnActualizar;
	private JButton btnLimpiar;
	private JButton btnAgregar;
	private JButton btnEliminar;
	private JLabel lblcabecera;
	private JTextField txtBusqueda;
	private JButton btnBuscar;
	private JScrollPane scrollPane;
	private JTextField txtCodigo;
	private JTextField txtCategoria;
	private JTable tbCategoria;
	private DefaultTableModel modelo;

	public DatosCategoria dataCategoria = frmPrincipal.dataCategoria;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			frmCategoria dialog = new frmCategoria();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public frmCategoria() {
		setTitle("Mantenimiento categoria");
		setBounds(100, 100, 573, 330);
		getContentPane().setLayout(null);

		lblCodigo = new JLabel("Codigo");
		lblCodigo.setBounds(72, 80, 46, 14);
		getContentPane().add(lblCodigo);

		lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(72, 110, 46, 14);
		getContentPane().add(lblNombre);

		btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(this);
		btnActualizar.setForeground(new Color(255, 153, 51));
		btnActualizar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnActualizar.setBounds(443, 219, 89, 23);
		getContentPane().add(btnActualizar);

		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(this);
		btnLimpiar.setBounds(443, 159, 89, 23);
		getContentPane().add(btnLimpiar);

		btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(this);
		btnAgregar.setForeground(new Color(51, 102, 255));
		btnAgregar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		btnAgregar.setBackground(SystemColor.menu);
		btnAgregar.setBounds(443, 77, 89, 23);
		getContentPane().add(btnAgregar);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(this);
		btnEliminar.setForeground(new Color(255, 0, 51));
		btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnEliminar.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnEliminar.setBounds(443, 253, 89, 23);
		getContentPane().add(btnEliminar);

		txtBusqueda = new JTextField();
		txtBusqueda.addActionListener(this);
		txtBusqueda.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtBusqueda.setColumns(10);
		txtBusqueda.setBounds(84, 12, 258, 31);
		getContentPane().add(txtBusqueda);

		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(this);
		btnBuscar.setBorder(null);
		btnBuscar.setBounds(364, 17, 89, 23);
		getContentPane().add(btnBuscar);

		lblcabecera = new JLabel("");
		lblcabecera.setOpaque(true);
		lblcabecera.setBackground(new Color(0, 128, 192));
		lblcabecera.setBounds(0, 4, 557, 55);
		getContentPane().add(lblcabecera);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(72, 138, 363, 138);
		getContentPane().add(scrollPane);

		tbCategoria = new JTable();
		tbCategoria.addMouseListener(this);
		tbCategoria.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Codigo", "Nombre" }));
		scrollPane.setViewportView(tbCategoria);

		txtCodigo = new JTextField();
		txtCodigo.setEnabled(false);
		txtCodigo.setBounds(128, 77, 86, 20);
		getContentPane().add(txtCodigo);
		txtCodigo.setColumns(10);

		txtCategoria = new JTextField();
		txtCategoria.setBounds(128, 107, 86, 20);
		getContentPane().add(txtCategoria);
		txtCategoria.setColumns(10);

		cargarData();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == txtBusqueda) {
			actionPerformedTxtBusqueda(e);
		}
		if (e.getSource() == btnBuscar) {
			actionPerformedBtnBuscar(e);
		}
		if (e.getSource() == btnEliminar) {
			actionPerformedBtnEliminar(e);
		}
		if (e.getSource() == btnLimpiar) {
			actionPerformedBtnLimpiar(e);
		}
		if (e.getSource() == btnAgregar) {
			actionPerformedBtnAgregar(e);
		}
		if (e.getSource() == btnActualizar) {
			actionPerformedBtnActualizar(e);
		}
	}

	public void cargarData() {
		modelo = (DefaultTableModel) tbCategoria.getModel();
		modelo.setRowCount(0);

		cargarDataTabla(dataCategoria.categorias);
	}

	protected void actionPerformedBtnAgregar(ActionEvent e) {

		try {
			String nombre = txtCategoria.getText();

			String mensaje = dataCategoria.agregar(new Categoria(nombre));

			JOptionPane.showMessageDialog(null, mensaje);

			dataCategoria.guardar();

			cargarData();

			limpiar();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	protected void actionPerformedBtnActualizar(ActionEvent e) {

		Categoria c = new Categoria();

		try {
			c.setCodCategoria(txtCodigo.getText());
			c.setNombre(txtCategoria.getText());

			String mensaje = dataCategoria.actualizar(c);
			JOptionPane.showMessageDialog(null, mensaje);

			dataCategoria.guardar();

			cargarData();

			limpiar();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	protected void actionPerformedBtnLimpiar(ActionEvent e) {
		limpiar();
	}

	protected void actionPerformedBtnEliminar(ActionEvent e) {

		try {
			String mensaje = dataCategoria.remover(txtCodigo.getText());

			JOptionPane.showMessageDialog(null, mensaje);

			dataCategoria.guardar();

			cargarData();

			limpiar();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	public void limpiar() {
		txtCodigo.setText("");
		txtCategoria.setText("");
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == tbCategoria) {
			mouseClickedTbCategoria(e);
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	protected void mouseClickedTbCategoria(MouseEvent e) {
		int i = tbCategoria.getSelectedRow();
		txtCodigo.setText(tbCategoria.getValueAt(i, 0).toString());
		txtCategoria.setText(tbCategoria.getValueAt(i, 1).toString());
	}

	protected void actionPerformedBtnBuscar(ActionEvent e) {
		modelo.setRowCount(0);
		var a = dataCategoria.buscar(txtBusqueda.getText());
		cargarDataTabla(a);
	}

	public void cargarDataTabla(ArrayList<Categoria> data) {

		for (int i = 0; i < data.size(); i++) {
			Categoria l = data.get(i);
			String c = dataCategoria.obtenerNombreCategoria(l.getCodCategoria());

			Object[] fila = { l.getCodCategoria(), l.getNombre() };
			modelo.addRow(fila);
		}
	}
	protected void actionPerformedTxtBusqueda(ActionEvent e) {
		modelo.setRowCount(0);
		var a = dataCategoria.buscar(txtBusqueda.getText());
		cargarDataTabla(a);
	}
}
