package forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import clases.DetallePrestamo;
import clases.Libro;
import clases.Prestamo;
import clases.Usuario;
import colecciones.DatosDetallePrestamo;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;

public class frmBoleta extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JButton btnAceptar;
	private JTextArea txtBoleta;

	Prestamo prestamo;
	DetallePrestamo detalle;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			frmBoleta dialog = new frmBoleta("");
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public frmBoleta(Prestamo prestamo) {
		setTitle("Boleta");
		setBounds(100, 100, 394, 536);
		getContentPane().setLayout(null);

		this.prestamo = prestamo;
		//this.detalle = detalle;

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 358, 441);
		getContentPane().add(scrollPane);

		txtBoleta = new JTextArea();
		txtBoleta.setEditable(false);

		scrollPane.setViewportView(txtBoleta);

		btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(this);
		btnAceptar.setBounds(154, 463, 89, 23);
		getContentPane().add(btnAceptar);

		generarBoleta();

		disposeAsientos();
	}

	private void generarBoleta() {
		
		StringBuilder boleta = new StringBuilder();

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		boleta.append("========================================\n");
		boleta.append("              BIBLIOTECA\n");
		boleta.append("          BOLETA DE PRESTAMO\n");
		boleta.append("========================================\n");

		boleta.append(String.format("%-15s %s%n", "Prestamo:", prestamo.getCodPrestamo()));
		boleta.append(String.format("%-15s %s%n", "Cod Usuario:", prestamo.getCodUsuario()));
		boleta.append(String.format("%-15s %s%n", "Usuario:", obtenerNombreUsuario(prestamo.getCodUsuario())));
		boleta.append(String.format("%-15s %s%n", "Fecha prestamo:", prestamo.getFechaPrestamo().format(formato)));
		boleta.append(String.format("%-15s %s%n", "Fecha devolucion:", prestamo.getFechaDevolucion().format(formato)));
		//boleta.append(String.format("%-15s %s%n", "Estado:", prestamo.estado));

		boleta.append("----------------------------------------\n");
		boleta.append("DETALLE DE LIBROS\n");
		boleta.append("----------------------------------------\n");

		boleta.append(String.format("%-10s %-22s %5s%n", "CODIGO", "TITULO", "CANT."));

		boleta.append("----------------------------------------\n");

		// Detalles de los libros.
		for (int i = 0; i < frmPrincipal.detalles.longitud(); i++) {
			detalle = frmPrincipal.detalles.obtener(i);

			if (detalle.getCodPrestamo().equals(prestamo.getCodPrestamo())) {

				boleta.append(String.format("%-8s %-24s %6d%n", detalle.getCodLibro(),
						recortarTitulo(obtenerTituloLibro(detalle.getCodLibro()), 24), detalle.getCantPrestamo()));
			}
		}

		boleta.append("========================================\n");
		boleta.append("       Gracias por usar la biblioteca\n");
		boleta.append("========================================\n");

		txtBoleta.setText(boleta.toString());
	}

	private String recortarTitulo(String titulo, int limite) {
		if (titulo.length() > limite) {
			return titulo.substring(0, limite - 3) + "...";
		}

		return titulo;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAceptar) {
			actionPerformedBtnAceptar(e);
		}
	}

	protected void actionPerformedBtnAceptar(ActionEvent e) {
		frmPrincipal.limpiarAgregados();
		this.dispose();
	}

	public String obtenerNombreUsuario(String codUsuario) {
		Usuario u = frmPrincipal.usuarios.buscarPorCodigoODni(codUsuario);
		return u.nomUser;
	}

	private void disposeAsientos() {
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				frmPrincipal.limpiarAgregados();
			}

			public void windowClosed(WindowEvent e) {
				frmPrincipal.limpiarAgregados();
			}
		});
	}

	public String obtenerTituloLibro(String codLibro) {
		for (int i = 0; i < frmPrincipal.dataLibro.longitud(); i++) {
			Libro l = frmPrincipal.dataLibro.obtener(i);

			if (l.getCodLibro().equals(codLibro)) {
				return l.getTitulo();
			}

		}
		return "";
	}
}
