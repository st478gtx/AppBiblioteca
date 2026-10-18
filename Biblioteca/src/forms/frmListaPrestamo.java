package forms;


import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import clases.Prestamo;
import clases.Usuario;
import app.BibliotecaContexto;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class frmListaPrestamo extends JDialog implements MouseListener {
	private final BibliotecaContexto contexto;
	private final Runnable limpiarSeleccion;

	private static final long serialVersionUID = 1L;
	private JLabel lblcabecera;
	private JLabel lblNewLabel;
	private JTable tbPrestamo;
	
	/**
	 * Create the dialog.
	 */
	public frmListaPrestamo(BibliotecaContexto contexto, Runnable limpiarSeleccion) {
		this.contexto = contexto;
		this.limpiarSeleccion = limpiarSeleccion;
		setResizable(false);
		setBounds(100, 100, 669, 555);
		getContentPane().setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 89, 633, 416);
			getContentPane().add(scrollPane);

			tbPrestamo = new JTable();
			tbPrestamo.addMouseListener(this);
			tbPrestamo.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Cod Prestamo", "Cod Usuario",
					"Usuario", "Fecha prestamo", "Fecha devoluci\u00F3n" }));
			scrollPane.setViewportView(tbPrestamo);
		}

		lblNewLabel = new JLabel("Lista de prestamos");
		lblNewLabel.setForeground(SystemColor.menu);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(30, 11, 210, 30);
		getContentPane().add(lblNewLabel);

		lblcabecera = new JLabel("");
		lblcabecera.setOpaque(true);
		lblcabecera.setBackground(new Color(0, 128, 192));
		lblcabecera.setBounds(0, 0, 653, 55);
		getContentPane().add(lblcabecera);
		cargarListado();
	}

	public void cargarListado() {
		DefaultTableModel modelo = (DefaultTableModel) tbPrestamo.getModel();
		modelo.setRowCount(0);

		for (Prestamo p : contexto.prestamos().listar()) {

			Object[] fila = { p.getCodPrestamo(), p.getCodUsuario(), obtenerNombreUsuario(p.getCodUsuario()),
					p.getFechaPrestamo(), p.getFechaDevolucion() };

			modelo.addRow(fila);
		}
	}

	public String obtenerNombreUsuario(String codUsuario) {
		Usuario u = contexto.usuarios().buscar(codUsuario);
		return u == null ? "Usuario no encontrado" : u.getNomUser();
	}
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == tbPrestamo) {
			mouseClickedTbPrestamo(e);
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
	protected void mouseClickedTbPrestamo(MouseEvent e) {
		int i = tbPrestamo.getSelectedRow();
		
		String codPrestamo = tbPrestamo.getValueAt(i, 0).toString();
		Prestamo p = contexto.prestamos().obtener(codPrestamo);
		
		frmBoleta boleta = new frmBoleta(contexto, p, limpiarSeleccion);
		boleta.setLocationRelativeTo(null);
		boleta.setVisible(true);
	}
}
