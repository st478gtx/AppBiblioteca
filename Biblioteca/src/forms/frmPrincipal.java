package forms;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import clases.Libro;
import clases.Prestamo;
import clases.Usuario;
import colecciones.*;
import util.DetalleLibro;
import app.BibliotecaContexto;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import java.awt.Color;

import javax.swing.JTextField;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

public class frmPrincipal extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private final BibliotecaContexto contexto;

	private JMenuBar menuBar;
	private JMenu mnMantenimiento;
	private JMenuItem mntmUsuario;
	private JMenuItem mntmCategoria;
	private JMenuItem mntmLibro;
	private JMenuItem mntmAutor;
	private JLabel lblCabecera;
	private JTextField txtBusqueda;
	private JButton btnBuscar;
	private JScrollPane scrollPane;
	private JPanel panelEstanteria;
	private JComboBox<Usuario> cbxUsuario;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JFormattedTextField txtFechaPrestamo;
	private JLabel lblNewLabel_2;
	private JFormattedTextField txtFechaDevolucion;
	private JButton btnEjecutarPrestamo;
	private JScrollPane scrollPane_1;
	private JLabel lblNewLabel_3;
	private JPanel panelAgregado;
	private JLabel lblContadorAgregado;

	public LocalDate hoy = LocalDate.now();
	private JMenu mnProceso;
	private JMenuItem mntmEntregaPrestamo;

	/**
	 * Create the frame.
	 */
	public frmPrincipal(BibliotecaContexto contexto) {
		this.contexto = contexto;
		setResizable(false);
		setTitle("Biblioteca");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1173, 786);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnMantenimiento = new JMenu("Mantenimiento");
		menuBar.add(mnMantenimiento);

		mntmUsuario = new JMenuItem("Usuario");
		mnMantenimiento.add(mntmUsuario);

		mntmUsuario.addActionListener(e -> mostrarMatenimientoUsuarios());

		mntmCategoria = new JMenuItem("Categoría");
		mnMantenimiento.add(mntmCategoria);

		mntmCategoria.addActionListener(e -> mostrarMatenimientoCategorias());

		mntmLibro = new JMenuItem("Libro");

		mntmLibro.addActionListener(e -> mostrarMantenimientoLibros());

		mnMantenimiento.add(mntmLibro);
		mntmAutor = new JMenuItem("Autor");
		mnMantenimiento.add(mntmAutor);
		mntmAutor.addActionListener(e -> mostrarMantenimientoAutores());

		mnProceso = new JMenu("Proceso");
		menuBar.add(mnProceso);

		mntmEntregaPrestamo = new JMenuItem("Entrega de prestamo");
		mntmEntregaPrestamo.addActionListener(this);
		mnProceso.add(mntmEntregaPrestamo);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnBuscar = new JButton("Buscar libros");
		btnBuscar.addActionListener(this);
		btnBuscar.setBorder(null);
		btnBuscar.setBounds(770, 16, 126, 23);
		contentPane.add(btnBuscar);

		txtBusqueda = new JTextField();
		txtBusqueda.addActionListener(this);
		txtBusqueda.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtBusqueda.setColumns(10);
		txtBusqueda.setBounds(328, 11, 409, 31);
		contentPane.add(txtBusqueda);

		lblCabecera = new JLabel("");
		lblCabecera.setBackground(new Color(30, 144, 255));
		lblCabecera.setOpaque(true);
		lblCabecera.setBounds(0, 0, 1157, 55);
		contentPane.add(lblCabecera);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 144, 942, 530);
		contentPane.add(scrollPane);

		panelEstanteria = new JPanel();
		panelEstanteria.setLayout(new GridLayout(0, 4, 5, 5));

		cargarRepisa("");

		scrollPane.setViewportView(panelEstanteria);

		cbxUsuario = new JComboBox<>();
		cbxUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cbxUsuario.setBounds(133, 90, 175, 22);
		contentPane.add(cbxUsuario);

		agregarUsuariosCombo();

		lblNewLabel = new JLabel("Usuarios registrados");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(23, 94, 116, 14);
		contentPane.add(lblNewLabel);

		lblNewLabel_1 = new JLabel("Fecha préstamo");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(350, 94, 94, 14);
		contentPane.add(lblNewLabel_1);

		MaskFormatter mascaraFecha;
		try {
			mascaraFecha = new MaskFormatter("##/##/####");
			mascaraFecha.setPlaceholderCharacter('_');

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			txtFechaPrestamo = new JFormattedTextField(mascaraFecha);
			txtFechaPrestamo.setFont(new Font("Tahoma", Font.PLAIN, 14));
			txtFechaPrestamo.setEditable(false);

			txtFechaDevolucion = new JFormattedTextField(mascaraFecha);
			txtFechaDevolucion.setFont(new Font("Tahoma", Font.PLAIN, 16));
			txtFechaPrestamo.setText(hoy.format(formatter));
			txtFechaDevolucion.setText(hoy.plusDays(5).format(formatter));

		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		txtFechaPrestamo.setBounds(468, 91, 103, 20);
		contentPane.add(txtFechaPrestamo);

		lblNewLabel_2 = new JLabel("Fecha devolución");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(655, 94, 103, 14);
		contentPane.add(lblNewLabel_2);

		txtFechaDevolucion.setBounds(768, 91, 103, 20);
		contentPane.add(txtFechaDevolucion);

		btnEjecutarPrestamo = new JButton("Realizar préstamo");
		btnEjecutarPrestamo.setEnabled(false);
		btnEjecutarPrestamo.addActionListener(this);
		btnEjecutarPrestamo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEjecutarPrestamo.setBounds(965, 81, 151, 36);
		contentPane.add(btnEjecutarPrestamo);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(965, 215, 182, 459);
		contentPane.add(scrollPane_1);

		panelAgregado = new JPanel();
		scrollPane_1.setViewportView(panelAgregado);
		panelAgregado.setLayout(new BoxLayout(panelAgregado, BoxLayout.Y_AXIS));

		lblNewLabel_3 = new JLabel("Seleccionados");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(969, 189, 94, 14);
		contentPane.add(lblNewLabel_3);

		lblContadorAgregado = new JLabel("");
		lblContadorAgregado.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblContadorAgregado.setBounds(1101, 191, 46, 14);
		contentPane.add(lblContadorAgregado);

	}

	public void mostrarMantenimientoLibros() {
		frmLibro libro = new frmLibro(contexto, () -> cargarRepisa(""));
		libro.setLocationRelativeTo(null);
		libro.setVisible(true);
	}

	public void mostrarMantenimientoAutores() {
		frmAutor autor = new frmAutor(contexto, () -> cargarRepisa(""));
		autor.setLocationRelativeTo(null);
		autor.setVisible(true);
	}

	public void mostrarMatenimientoUsuarios() {
		frmUsuario usuario = new frmUsuario(contexto, this::agregarUsuariosCombo);
		usuario.setLocationRelativeTo(null);
		usuario.setVisible(true);
	}

	public void mostrarMatenimientoCategorias() {
		frmCategoria categoria = new frmCategoria(contexto);
		categoria.setLocationRelativeTo(null);
		categoria.setVisible(true);
	}

	public void cargarRepisa(String buscar) {
		panelEstanteria.removeAll();
		var filtroLibro = contexto.libros().buscar(buscar);

		for (int i = 0; i < filtroLibro.size(); i++) {
			panelEstanteria.add(TarjetasLibro.catalogo(filtroLibro.get(i), this::agregarLibro));
		}

		panelEstanteria.revalidate();
		panelEstanteria.repaint();
	}



	public void agregarLibro(Libro l) {
		contexto.prestamos().seleccionar(l);
		actualizarPanelAgregados();
	}

	public void removerLibro(Libro l) {
		contexto.prestamos().quitar(l);
		actualizarPanelAgregados();
	}

	private void actualizarPanelAgregados() {
		panelAgregado.removeAll();

		for (DetalleLibro elegido : contexto.prestamos().seleccionados()) {
			Libro l = elegido.getLibro();
			int cantidad = elegido.getCantidad();

			panelAgregado.add(TarjetasLibro.seleccionado(l, cantidad, this::removerLibro));
		}

		updateAgregados();
	}

	public void updateAgregados() {

		int total = 0;

		total = contexto.prestamos().cantidadSeleccionada();
		if (total != 0) {
			btnEjecutarPrestamo.setEnabled(true);
		} else {
			btnEjecutarPrestamo.setEnabled(false);
		}

		lblContadorAgregado.setText(total + " ");
		panelAgregado.revalidate();
		panelAgregado.repaint();

	}

	public void agregarUsuariosCombo() {
		cbxUsuario.removeAll();
		for (Usuario usuario : contexto.usuarios().listar()) {
			cbxUsuario.addItem(usuario);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mntmEntregaPrestamo) {
			actionPerformedMntmEntregaPrestamo(e);
		}
		if (e.getSource() == btnEjecutarPrestamo) {
			actionPerformedBtnEjecutarPrestamo(e);
		}
		if (e.getSource() == txtBusqueda) {
			actionPerformedTxtBusqueda(e);
		}
		if (e.getSource() == btnBuscar) {
			actionPerformedBtnBuscar(e);
		}
	}

	protected void actionPerformedBtnBuscar(ActionEvent e) {
		cargarRepisa(txtBusqueda.getText());
	}

	protected void actionPerformedTxtBusqueda(ActionEvent e) {
		cargarRepisa(txtBusqueda.getText());
	}

	protected void actionPerformedBtnEjecutarPrestamo(ActionEvent e) {

		try {

			Usuario codUsuario = (Usuario) cbxUsuario.getSelectedItem();

			String fechaPrest = txtFechaPrestamo.getText();
			String fechaDev = txtFechaDevolucion.getText();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			LocalDate fechaPrestamo = LocalDate.parse(fechaPrest, formatter);
			LocalDate fechaDevolucion = LocalDate.parse(fechaDev, formatter);

			Prestamo p = contexto.prestamos().registrar(codUsuario, fechaPrestamo, fechaDevolucion);

			frmBoleta boleta = new frmBoleta(contexto, p, this::limpiarAgregados);
			boleta.setLocationRelativeTo(null);
			boleta.setVisible(true);
			

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	public void limpiarAgregados() {
		contexto.prestamos().limpiarSeleccion();
		panelAgregado.removeAll();
		panelAgregado.revalidate();
		panelAgregado.repaint();
		lblContadorAgregado.setText("");
	}
	protected void actionPerformedMntmEntregaPrestamo(ActionEvent e) {
		frmListaPrestamo listaPrestamo = new frmListaPrestamo(contexto, this::limpiarAgregados);
		listaPrestamo.setLocationRelativeTo(null);
		listaPrestamo.setVisible(true);
	}
}
