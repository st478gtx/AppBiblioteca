package forms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import clases.Libro;
import colecciones.*;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseListener;
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

public class frmPrincipal extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/* publicar todos los arreglos de alcance static */
	public static DatosUsuario usuarios = new DatosUsuario();
	public static DatosLibro dataLibro = new DatosLibro();
	public static DatosCategoria dataCategoria = new DatosCategoria();
	
	private JMenuBar menuBar;
	private JMenu mnMantenimiento;
	private JMenuItem mntmUsuario;
	private JMenuItem mntmCategoria;
	private JMenuItem mntmLibro;
	private JLabel lblCabecera;
	private JTextField txtBusqueda;
	private JButton btnBuscar;
	private JScrollPane scrollPane;
	private JPanel panelEstanteria;	

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
		setBounds(100, 100, 1173, 736);

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
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(this);
		btnBuscar.setBorder(null);
		btnBuscar.setBounds(770, 16, 89, 23);
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
		scrollPane.setBounds(10, 66, 942, 598);
		contentPane.add(scrollPane);

		panelEstanteria = new JPanel();
		panelEstanteria.setLayout(new GridLayout(0, 4, 5, 5));

		cargarRepisa("");

		scrollPane.setViewportView(panelEstanteria);

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

	public void cargarRepisa(String buscar) {
		panelEstanteria.removeAll();
		var filtroLibro = dataLibro.buscar(txtBusqueda.getText());

		for (int i = 0; i < filtroLibro.size(); i++) {
			panelEstanteria.add(cardAlimento(filtroLibro.get(i)));
		}
		
		panelEstanteria.revalidate();
		panelEstanteria.repaint();
	}

	public JPanel cardAlimento(Libro libro) {
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

	public void agregarLibro(Libro libro) {

	}

	public void actionPerformed(ActionEvent e) {
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
}
