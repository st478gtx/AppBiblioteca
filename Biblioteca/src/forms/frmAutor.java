package forms;

import clases.Autor;
import colecciones.DatosAutor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class frmAutor extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final DatosAutor dataAutor = frmPrincipal.dataAutor;
	private final JTextField txtBusqueda = new JTextField();
	private final JTextField txtCodigo = new JTextField();
	private final JTextField txtNombre = new JTextField();
	private final JButton btnBuscar = new JButton("Buscar");
	private final JButton btnAgregar = new JButton("Agregar");
	private final JButton btnActualizar = new JButton("Actualizar");
	private final JButton btnEliminar = new JButton("Eliminar");
	private final JButton btnLimpiar = new JButton("Limpiar");
	private final DefaultTableModel modelo = new DefaultTableModel(new Object[] { "Código", "Nombre" }, 0) {
		private static final long serialVersionUID = 1L;
		@Override
		public boolean isCellEditable(int fila, int columna) {
			return false;
		}
	};
	private final JTable tbAutor = new JTable(modelo);

	public frmAutor() {
		setTitle("Mantenimiento autor");
		setBounds(100, 100, 573, 330);
		getContentPane().setLayout(null);

		JLabel lblCodigo = new JLabel("Código");
		lblCodigo.setBounds(72, 80, 50, 14);
		getContentPane().add(lblCodigo);

		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(72, 110, 50, 14);
		getContentPane().add(lblNombre);

		txtCodigo.setBounds(128, 77, 86, 20);
		txtCodigo.setEditable(false);
		getContentPane().add(txtCodigo);

		txtNombre.setBounds(128, 107, 210, 20);
		getContentPane().add(txtNombre);

		txtBusqueda.setBounds(84, 12, 258, 31);
		txtBusqueda.addActionListener(this);
		getContentPane().add(txtBusqueda);

		configurarBoton(btnBuscar, 364, 17);
		btnBuscar.setBorder(null);
		configurarBoton(btnAgregar, 443, 77);
		btnAgregar.setForeground(new Color(51, 102, 255));
		configurarBoton(btnLimpiar, 443, 159);
		configurarBoton(btnActualizar, 443, 219);
		btnActualizar.setForeground(new Color(255, 153, 51));
		configurarBoton(btnEliminar, 443, 253);
		btnEliminar.setForeground(new Color(255, 0, 51));

		JLabel cabecera = new JLabel();
		cabecera.setOpaque(true);
		cabecera.setBackground(new Color(0, 128, 192));
		cabecera.setBounds(0, 4, 557, 55);
		getContentPane().add(cabecera);

		JScrollPane scrollPane = new JScrollPane(tbAutor);
		scrollPane.setBounds(72, 138, 363, 138);
		getContentPane().add(scrollPane);
		tbAutor.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tbAutor.getSelectedRow() >= 0) {
				int fila = tbAutor.convertRowIndexToModel(tbAutor.getSelectedRow());
				txtCodigo.setText(modelo.getValueAt(fila, 0).toString());
				txtNombre.setText(modelo.getValueAt(fila, 1).toString());
			}
		});

		cargarData(dataAutor.autores);
	}

	private void configurarBoton(JButton boton, int x, int y) {
		boton.setBounds(x, y, 89, 23);
		boton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		boton.addActionListener(this);
		getContentPane().add(boton);
	}

	private void cargarData(ArrayList<Autor> autores) {
		modelo.setRowCount(0);
		for (Autor autor : autores) {
			modelo.addRow(new Object[] { autor.getCodAutor(), autor.getNombre() });
		}
	}

	private void limpiar() {
		tbAutor.clearSelection();
		txtCodigo.setText("");
		txtNombre.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origen = e.getSource();
		if (origen == btnBuscar || origen == txtBusqueda) {
			cargarData(dataAutor.buscar(txtBusqueda.getText()));
			return;
		}
		if (origen == btnLimpiar) {
			limpiar();
			return;
		}
		try {
			String mensaje;
			if (origen == btnAgregar) {
				mensaje = dataAutor.agregar(txtNombre.getText());
			} else if (origen == btnActualizar) {
				frmPrincipal.dataLibro.vincularAutores(dataAutor);
				mensaje = dataAutor.actualizar(new Autor(txtCodigo.getText(), txtNombre.getText()));
				frmPrincipal.dataLibro.actualizarNombreAutor(dataAutor.obtenerPorCodigo(txtCodigo.getText()));
			} else if (origen == btnEliminar) {
				if (txtCodigo.getText().isBlank()) {
					throw new IllegalArgumentException("Seleccione un autor para eliminar");
				}
				int respuesta = JOptionPane.showConfirmDialog(this,
						"¿Desea eliminar al autor " + txtNombre.getText() + "?",
						"Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (respuesta != JOptionPane.YES_OPTION) return;
				mensaje = dataAutor.remover(txtCodigo.getText(), frmPrincipal.dataLibro);
			} else {
				return;
			}
			dataAutor.guardar();
			if (origen == btnActualizar) frmPrincipal.dataLibro.guardar();
			cargarData(dataAutor.autores);
			limpiar();
			JOptionPane.showMessageDialog(this, mensaje);
		} catch (RuntimeException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Autor", JOptionPane.ERROR_MESSAGE);
		}
	}
}
