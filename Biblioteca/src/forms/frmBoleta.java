package forms;


import javax.swing.JButton;
import javax.swing.JDialog;

import clases.DetallePrestamo;
import clases.Libro;
import clases.Prestamo;
import clases.Usuario;
import app.BibliotecaContexto;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;

public class frmBoleta extends JDialog implements ActionListener {
	private final BibliotecaContexto contexto;
	private final Runnable limpiarSeleccion;

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JButton btnAceptar;
	private JTextArea txtBoleta;

	Prestamo prestamo;

	/**
	 * Create the dialog.
	 */
	public frmBoleta(BibliotecaContexto contexto, Prestamo prestamo, Runnable limpiarSeleccion) {
		this.contexto = contexto;
		this.limpiarSeleccion = limpiarSeleccion;
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
		// boleta.append(String.format("%-15s %s%n", "Estado:", prestamo.estado));

		boleta.append("----------------------------------------\n");
		boleta.append("DETALLE DE LIBROS\n");
		boleta.append("----------------------------------------\n");

		boleta.append(String.format("%-10s %-22s %5s%n", "CODIGO", "TITULO", "CANT."));

		boleta.append("----------------------------------------\n");

		// Detalles de los libros.
		for (DetallePrestamo detalle : contexto.prestamos().detallesDe(prestamo.getCodPrestamo())) {

				boleta.append(String.format("%-8s %-24s %6d%n", detalle.getCodLibro(),
						recortarTitulo(obtenerTituloLibro(detalle.getCodLibro()), 24), detalle.getCantPrestamo()));
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
		limpiarSeleccion.run();
		this.dispose();
	}

	public String obtenerNombreUsuario(String codUsuario) {
		Usuario u = contexto.usuarios().buscar(codUsuario);
		return u == null ? "Usuario no encontrado" : u.getNomUser();
	}

	private void disposeAsientos() {
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				limpiarSeleccion.run();
			}

			public void windowClosed(WindowEvent e) {
				limpiarSeleccion.run();
			}
		});
	}

	public String obtenerTituloLibro(String codLibro) {
		for (int i = 0; i < contexto.libros().longitud(); i++) {
			Libro l = contexto.libros().obtener(i);

			if (l.getCodLibro().equals(codLibro)) {
				return l.getTitulo();
			}

		}
		return "";
	}
}
