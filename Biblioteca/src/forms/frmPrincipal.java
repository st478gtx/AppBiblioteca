package forms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import clases.DetallePrestamo;
import clases.Libro;
import clases.Prestamo;
import clases.Usuario;
import colecciones.*;
import util.DatosLibroDetalle;
import util.DetalleLibro;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.MouseListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

public class frmPrincipal extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/* publicar todos los arreglos de alcance static */
	public static DatosUsuario usuarios = new DatosUsuario();
	public static DatosLibro dataLibro = new DatosLibro();
	public static DatosCategoria dataCategoria = new DatosCategoria();
	public static DatosPrestamo dataPrestamo = new DatosPrestamo();

	public static DatosLibroDetalle agregados = new DatosLibroDetalle();

	public static DatosDetallePrestamo detalles = new DatosDetallePrestamo();

	private JMenuBar menuBar;
	private JMenu mnMantenimiento;
	private JMenuItem mntmUsuario;
	private JMenuItem mntmCategoria;
	private JMenuItem mntmLibro;
	private JLabel lblCabecera;
	private static JTextField txtBusqueda;
	private JButton btnBuscar;
	private JScrollPane scrollPane;
	private static JPanel panelEstanteria;
	private JComboBox<Usuario> cbxUsuario;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JFormattedTextField txtFechaPrestamo;
	private JLabel lblNewLabel_2;
	private JFormattedTextField txtFechaDevolucion;
	private static JButton btnEjecutarPrestamo;
	private JScrollPane scrollPane_1;
	private JLabel lblNewLabel_3;
	private static JPanel panelAgregado;
	private static JLabel lblContadorAgregado;

	public LocalDate hoy = LocalDate.now();
	private JMenu mnNewMenu;
	private JMenuItem mntmNewMenuItem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmPrincipal frame = new frmPrincipal();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public frmPrincipal() {
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

		mnNewMenu = new JMenu("Proceso");
		menuBar.add(mnNewMenu);

		mntmNewMenuItem = new JMenuItem("Listado prestado");
		mnNewMenu.add(mntmNewMenuItem);
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
		frmLibro libro = new frmLibro();
		libro.setLocationRelativeTo(null);
		libro.setVisible(true);
	}

	public void mostrarMatenimientoUsuarios() {
		frmUsuario usuario = new frmUsuario();
		usuario.setLocationRelativeTo(null);
		usuario.setVisible(true);
	}

	public void mostrarMatenimientoCategorias() {
		frmCategoria categoria = new frmCategoria();
		categoria.setLocationRelativeTo(null);
		categoria.setVisible(true);
	}

	public static void cargarRepisa(String buscar) {
		panelEstanteria.removeAll();
		var filtroLibro = dataLibro.buscar(txtBusqueda.getText());

		for (int i = 0; i < filtroLibro.size(); i++) {
			panelEstanteria.add(cardLibro(filtroLibro.get(i)));
		}

		panelEstanteria.revalidate();
		panelEstanteria.repaint();
	}

	public static JPanel cardLibro(Libro libro) {
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setPreferredSize(new Dimension(220, 380));
		card.setBackground(new Color(240, 220, 200)); // Ajuste de color al card
		card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		ImageIcon original = new ImageIcon(libro.getRutaPortada());
		Image scale = original.getImage().getScaledInstance(160, 200, Image.SCALE_SMOOTH);
		JLabel img = new JLabel(new ImageIcon(scale));
		img.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel titulo = new JLabel(libro.getTitulo());
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		titulo.setPreferredSize(new Dimension(220, 55));
		titulo.setMinimumSize(new Dimension(200, 55));
		titulo.setMaximumSize(new Dimension(200, 55));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel autor = new JLabel(libro.getAutor());
		autor.setForeground(Color.GRAY);
		autor.setFont(new Font("Segoe UI", Font.BOLD, 16));
		autor.setPreferredSize(new Dimension(220, 55));
		autor.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton agregar = new JButton(" + AGREGAR ");
		agregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		agregar.setPreferredSize(new Dimension(123, 23));
		agregar.setFocusPainted(false);
		agregar.setBorder(null);
		agregar.setBackground(Color.ORANGE);
		agregar.setAlignmentX(Component.CENTER_ALIGNMENT);
		agregar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		agregar.addActionListener(e -> agregarLibro(libro));

		card.add(Box.createVerticalGlue());
		card.add(img);
		card.add(Box.createVerticalStrut(5));
		card.add(titulo);
		card.add(Box.createVerticalStrut(5));
		card.add(autor);
		card.add(Box.createVerticalStrut(5));
		card.add(agregar);
		card.add(Box.createVerticalGlue());

		return card;
	}

	private static void libroAgregado(Libro libro, int cantidad) {
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setPreferredSize(new Dimension(100, 200));
		card.setBackground(new Color(240, 220, 200));
		card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		ImageIcon original = new ImageIcon(libro.getRutaPortada());
		Image scale = original.getImage().getScaledInstance(90, 110, Image.SCALE_SMOOTH);
		JLabel img = new JLabel(new ImageIcon(scale));
		img.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblCantidad = new JLabel("x" + cantidad);
		lblCantidad.setForeground(Color.ORANGE);
		lblCantidad.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblCantidad.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton quitar = new JButton("- Quitar");
		quitar.setFont(new Font("Segoe UI", Font.BOLD, 12));
		quitar.setFocusPainted(false);
		quitar.setBorder(null);
		quitar.setBackground(Color.ORANGE);
		quitar.setAlignmentX(Component.CENTER_ALIGNMENT);
		quitar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		quitar.addActionListener(e -> removerLibro(libro));

		card.add(Box.createVerticalGlue());
		card.add(img);
		card.add(Box.createVerticalStrut(5));
		card.add(lblCantidad);
		card.add(Box.createVerticalStrut(5));
		card.add(quitar);
		card.add(Box.createVerticalGlue());

		panelAgregado.add(card);
	}

	public static void agregarLibro(Libro l) {
		agregados.actualizarLista(l);
		actualizarPanelAgregados();
	}

	public static void removerLibro(Libro l) {
		agregados.eliminar(l);
		actualizarPanelAgregados();
	}

	private static void actualizarPanelAgregados() {
		panelAgregado.removeAll();

		for (int i = 0; i < agregados.longitud(); i++) {
			Libro l = agregados.obtener(i).getLibro();
			int cantidad = agregados.obtener(i).getCantidad();

			libroAgregado(l, cantidad);
		}

		updateAgregados();
	}

	public static void updateAgregados() {

		int total = 0;

		for (int i = 0; i < agregados.longitud(); i++) {
			total = total + agregados.obtener(i).getCantidad();
		}
		if (agregados.longitud() != 0) {
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
		for (int i = 0; i < usuarios.longitud(); i++) {
			cbxUsuario.addItem(usuarios.obtener(i));
		}
	}

	public void actionPerformed(ActionEvent e) {
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

			Prestamo p = new Prestamo(codUsuario.codUser, fechaPrestamo, fechaDevolucion);

			String codPrestamo = frmPrincipal.dataPrestamo.agregar(p);

			for (int i = 0; i < agregados.longitud(); i++) {
				DetalleLibro a = agregados.obtener(i);

				detalles.agregar(a.getLibro().getCodLibro(), codPrestamo, a.getCantidad());
			}

			frmBoleta boleta = new frmBoleta(p);
			boleta.setLocationRelativeTo(null);
			boleta.setVisible(true);

			agregados.detalleLibros.clear();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	public static void limpiarAgregados() {
		panelAgregado.removeAll();
		panelAgregado.revalidate();
		panelAgregado.repaint();
		lblContadorAgregado.setText("");
	}
}
