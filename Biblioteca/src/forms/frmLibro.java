package forms;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Cursor;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import clases.Categoria;
import clases.Autor;
import clases.Libro;
import util.Constante;
import util.Rutas;
import app.BibliotecaContexto;
import service.LibroService;
import service.CategoriaService;
import service.PortadaService;
import service.AutorService;

import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseEvent;

public class frmLibro extends JDialog implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JLabel lblTitulo;
	private JLabel lblAutor;
	private JLabel lblISBN;
	private JLabel lblCategoría;
	private JTable tbData;
	private JTextField txtTitulo;
	private JComboBox<Autor> cbxAutor;
	private JTextField txtIsbn;
	private JComboBox<Categoria> cbxCategoria;
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
	JFileChooser fileChooser = new JFileChooser();

	// Cargar data de form principal
	private final LibroService dataLibro;
	private final CategoriaService dataCategoria;
	private final AutorService dataAutor;
	private final Runnable actualizarRepisa;
	private final PortadaService portadaService;

	private String rutaPortada = "";

	private JTextField txtCodigo;
	private JLabel lblCodigo;
	private JTextField txtCantDisp;
	private JLabel lblCantDisponible;
	private JButton btnAgregarImagen;
	private JLabel imgAgregado;
	private JLabel lblcabecera;
	private JButton btnLimpiar;

	/**
	 * Create the dialog.
	 */
	public frmLibro(BibliotecaContexto contexto, Runnable actualizarRepisa) {
		this.dataLibro = contexto.libros();
		this.dataCategoria = contexto.categorias();
		this.dataAutor = contexto.autores();
		this.actualizarRepisa = actualizarRepisa;
		this.portadaService = contexto.portadas();
		setTitle("Mantenimiento de Libro");
		setBounds(100, 100, 1180, 743);
		getContentPane().setLayout(null);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 269, 1144, 424);
		getContentPane().add(scrollPane);

		tbData = new JTable();
		tbData.addMouseListener(this);
		tbData.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Imagen", "Codigo", "T\u00EDtulo",
				"Autor", "ISBN", "Categor\u00EDa", "Publicaci\u00F3n", "Disponible", "Cantidad total" }));
		scrollPane.setViewportView(tbData);

		lblTitulo = new JLabel("Título");
		lblTitulo.setBounds(35, 122, 46, 14);
		getContentPane().add(lblTitulo);

		lblAutor = new JLabel("Autor");
		lblAutor.setBounds(35, 169, 46, 14);
		getContentPane().add(lblAutor);

		lblISBN = new JLabel("ISBN");
		lblISBN.setBounds(35, 220, 46, 14);
		getContentPane().add(lblISBN);

		lblCategoría = new JLabel("Categoría");
		lblCategoría.setBounds(398, 78, 69, 14);
		getContentPane().add(lblCategoría);

		txtTitulo = new JTextField();
		txtTitulo.setBounds(109, 119, 197, 20);
		getContentPane().add(txtTitulo);
		txtTitulo.setColumns(10);

		cbxAutor = new JComboBox<>();
		cbxAutor.setBounds(109, 166, 197, 20);
		getContentPane().add(cbxAutor);

		txtIsbn = new JTextField();
		txtIsbn.setBounds(109, 217, 197, 20);
		getContentPane().add(txtIsbn);
		txtIsbn.setColumns(10);

		cbxCategoria = new JComboBox<>();
		cbxCategoria.setBounds(494, 74, 154, 22);

		getContentPane().add(cbxCategoria);

		lblNewLabel_4 = new JLabel("Año publicación");
		lblNewLabel_4.setBounds(398, 122, 86, 14);
		getContentPane().add(lblNewLabel_4);

		lblNewLabel_5 = new JLabel("Cantidad total");
		lblNewLabel_5.setBounds(398, 220, 86, 14);
		getContentPane().add(lblNewLabel_5);

		txtAnioPublicacion = new JTextField();
		txtAnioPublicacion.setBounds(494, 119, 86, 20);
		getContentPane().add(txtAnioPublicacion);
		txtAnioPublicacion.setColumns(10);

		txtCantidadTotal = new JTextField();
		txtCantidadTotal.setBounds(494, 217, 86, 20);
		getContentPane().add(txtCantidadTotal);
		txtCantidadTotal.setColumns(10);

		btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(this);
		btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		btnAgregar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAgregar.setForeground(new Color(51, 102, 255));
		btnAgregar.setBackground(SystemColor.menu);
		btnAgregar.setBounds(1065, 80, 89, 23);
		getContentPane().add(btnAgregar);

		btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(this);
		btnActualizar.setForeground(new Color(255, 153, 51));
		btnActualizar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnActualizar.setBounds(1065, 181, 89, 23);
		getContentPane().add(btnActualizar);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(this);
		btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnEliminar.setForeground(new Color(255, 0, 51));
		btnEliminar.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnEliminar.setBounds(1065, 215, 89, 23);
		getContentPane().add(btnEliminar);

		txtBusqueda = new JTextField();
		txtBusqueda.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtBusqueda.addActionListener(this);
		txtBusqueda.setBounds(428, 12, 258, 31);
		getContentPane().add(txtBusqueda);
		txtBusqueda.setColumns(10);

		btnBuscar = new JButton("Buscar");
		btnBuscar.setBorder(null);
		btnBuscar.addActionListener(this);
		btnBuscar.setBounds(720, 16, 89, 23);
		getContentPane().add(btnBuscar);

		txtCodigo = new JTextField();
		txtCodigo.setEnabled(false);
		txtCodigo.setBounds(109, 74, 86, 20);
		getContentPane().add(txtCodigo);
		txtCodigo.setColumns(10);

		lblCodigo = new JLabel("Codigo");
		lblCodigo.setBounds(35, 80, 46, 14);
		getContentPane().add(lblCodigo);

		txtCantDisp = new JTextField();
		txtCantDisp.setBounds(494, 166, 86, 20);
		getContentPane().add(txtCantDisp);
		txtCantDisp.setColumns(10);

		lblCantDisponible = new JLabel("Cant Disponible");
		lblCantDisponible.setBounds(398, 169, 86, 14);
		getContentPane().add(lblCantDisponible);

		btnAgregarImagen = new JButton("Agregar Imagen");
		btnAgregarImagen.addActionListener(this);
		btnAgregarImagen.setBounds(664, 163, 111, 23);
		getContentPane().add(btnAgregarImagen);

		imgAgregado = new JLabel("");
		imgAgregado.setOpaque(true);
		imgAgregado.setBackground(new Color(255, 255, 255));
		imgAgregado.setBounds(779, 74, 120, 180);

		ImageIcon noImage = new ImageIcon(Constante.rutaSinImagenPortada());
		Image imagen = noImage.getImage().getScaledInstance(130, 140, Image.SCALE_SMOOTH);

		imgAgregado.setIcon(new ImageIcon(imagen));

		getContentPane().add(imgAgregado);

		lblcabecera = new JLabel("");
		lblcabecera.setOpaque(true);
		lblcabecera.setBackground(new Color(0, 128, 192));
		lblcabecera.setBounds(0, 0, 1164, 55);
		getContentPane().add(lblcabecera);

		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(this);
		btnLimpiar.setBounds(1065, 118, 89, 23);
		getContentPane().add(btnLimpiar);

		cargarCategorias();
		cargarAutores();
		cargarData();
		addWindowFocusListener(new java.awt.event.WindowAdapter() {
			@Override public void windowGainedFocus(java.awt.event.WindowEvent e) {
				cargarAutores(); cargarData();
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnEliminar) {
			actionPerformedBtnEliminar(e);
		}
		if (e.getSource() == btnLimpiar) {
			actionPerformedBtnLimpiar(e);
		}
		if (e.getSource() == btnActualizar) {
			actionPerformedBtnActualizar(e);
		}
		if (e.getSource() == btnAgregarImagen) {
			actionPerformedBtnAgregarImagen(e);
		}
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

		Categoria categoria = (Categoria) cbxCategoria.getSelectedItem();

		try {
			String titulo = txtTitulo.getText();
			Autor autorSeleccionado = (Autor) cbxAutor.getSelectedItem();
			if (autorSeleccionado == null) throw new IllegalArgumentException("Seleccione un autor registrado");
			String autor = autorSeleccionado.getNombre();
			String isbn = txtIsbn.getText();
			int publicacion = Integer.parseInt(txtAnioPublicacion.getText());
			String codCategoria = categoria.getCodCategoria();
			int cant = Integer.parseInt(txtCantDisp.getText());
			int total = Integer.parseInt(txtCantidadTotal.getText());

			if (!guardarImagen()) {
				return;
			}

			if (rutaPortada.isEmpty()) {
				rutaPortada = Constante.SIN_IMAGEN_PORTADA;
			}

			Libro l = new Libro(titulo, autor, isbn, codCategoria, publicacion, cant, total, rutaPortada);

			String mensaje = dataLibro.agregar(l);

			JOptionPane.showMessageDialog(null, mensaje);


			cargarData();

			limpiar();
			
			actualizarRepisa.run();
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}

	}

	public void cargarData() {
		modelo = (DefaultTableModel) tbData.getModel();
		modelo.setRowCount(0);

		tbData.getColumnModel().getColumn(0).setCellRenderer(new ImagenRenderer());
		tbData.setRowHeight(80);

		cargarDataTabla(dataLibro.listar());
	}

	protected void actionPerformedBtnBuscar(ActionEvent e) {
		modelo.setRowCount(0);
		var a = dataLibro.buscar(txtBusqueda.getText());
		cargarDataTabla(a);
	}

	public void cargarCategorias() {
		cbxCategoria.removeAllItems();
		for (int i = 0; i < dataCategoria.longitud(); i++) {
			cbxCategoria.addItem(dataCategoria.obtener(i));
		}
	}

	public void cargarAutores() {
		Autor seleccionado = (Autor) cbxAutor.getSelectedItem();
		cbxAutor.removeAllItems();
		for (Autor autor : dataAutor.listar()) cbxAutor.addItem(autor);
		cbxAutor.setSelectedIndex(-1);
		if (seleccionado != null) seleccionarAutor(seleccionado.getCodigo());
	}

	private void seleccionarAutor(String codigo) {
		for (int i = 0; i < cbxAutor.getItemCount(); i++) {
			if (cbxAutor.getItemAt(i).getCodigo().equals(codigo)) {
				cbxAutor.setSelectedIndex(i); return;
			}
		}
	}

	public void cargarDataTabla(ArrayList<Libro> data) {
		DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
		centrado.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < tbData.getColumnCount(); i++) {
			if (i != 0) { // Excluir la columna de portada
				tbData.getColumnModel().getColumn(i).setCellRenderer(centrado);
			}
		}

		for (int i = 0; i < data.size(); i++) {
			Libro l = data.get(i);
			String c = dataCategoria.nombreDe(l.getCodCategoria());

			Object[] fila = { l.getRutaPortada(), l.getCodLibro(), l.getTitulo(), l.getAutor(), l.getIsnb(), c,
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

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == tbData) {
			mouseClickedTbData(e);
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

	protected void mouseClickedTbData(MouseEvent e) {
		int i = tbData.getSelectedRow();
		rutaPortada = tbData.getValueAt(i, 0).toString();
		ImageIcon portada = new ImageIcon(Rutas.imagen(rutaPortada).toString());
		Image imagen = portada.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);

		imgAgregado.setIcon(new ImageIcon(imagen));

		txtCodigo.setText(tbData.getValueAt(i, 1).toString());
		txtTitulo.setText(tbData.getValueAt(i, 2).toString());
		Autor autor = dataAutor.autorDeLibro(txtCodigo.getText());
		if (autor != null) seleccionarAutor(autor.getCodigo());
		txtIsbn.setText(tbData.getValueAt(i, 4).toString());

		String nomCategoria = tbData.getValueAt(i, 5).toString();

		obtenerNombreComboCategoria(nomCategoria);

		txtAnioPublicacion.setText(tbData.getValueAt(i, 6).toString());
		txtCantDisp.setText(tbData.getValueAt(i, 7).toString());
		txtCantidadTotal.setText(tbData.getValueAt(i, 8).toString());
	}

	private void obtenerNombreComboCategoria(String nomCategoria) {
		for (int j = 0; j < cbxCategoria.getItemCount(); j++) {
			Categoria categoria = cbxCategoria.getItemAt(j);

			if (categoria.getNombre().equals(nomCategoria)) {
				cbxCategoria.setSelectedIndex(j);
				break;
			}
		}
	}

	protected void actionPerformedBtnAgregarImagen(ActionEvent e) {

		fileChooser.setDialogTitle("Seleccionar portada");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imágenes (*.jpg, *.jpeg, *.png)", "jpg", "jpeg",
				"png");

		fileChooser.setFileFilter(filtro);

		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File archivo = fileChooser.getSelectedFile();

			ImageIcon icono = new ImageIcon(archivo.getAbsolutePath());

			Image imagen = icono.getImage().getScaledInstance(130, 140, Image.SCALE_SMOOTH);

			imgAgregado.setIcon(new ImageIcon(imagen));

		}
	}

public boolean guardarImagen() {
		File archivo = fileChooser.getSelectedFile();
		if (archivo == null) return true;
		try {
			rutaPortada = portadaService.guardar(archivo.toPath());
			return true;
		} catch (IOException error) {
			JOptionPane.showMessageDialog(this, "No se pudo guardar la imagen: " + error.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	protected void actionPerformedBtnActualizar(ActionEvent e) {
		try {
			Libro l = new Libro();

			Categoria categoria = (Categoria) cbxCategoria.getSelectedItem();

			l.setCodLibro(txtCodigo.getText());
			l.setTitulo(txtTitulo.getText());
			Autor autor = (Autor) cbxAutor.getSelectedItem();
			if (autor == null) throw new IllegalArgumentException("Seleccione un autor registrado");
			l.setAutor(autor.getNombre());
			l.setIsnb(txtIsbn.getText());
			l.setAnioPublicacion(Integer.parseInt(txtAnioPublicacion.getText()));
			l.setCodCategoria(categoria.getCodCategoria());
			l.setCantidadDisponible(Integer.parseInt(txtCantDisp.getText()));
			l.setCantidadTotal(Integer.parseInt(txtCantidadTotal.getText()));

			if (!guardarImagen()) {
				return;
			}
			l.setRutaPortada(rutaPortada);

			String mensaje = dataLibro.actualizar(l);

			JOptionPane.showMessageDialog(null, mensaje);


			cargarData();
			
			limpiar();
			
			actualizarRepisa.run();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	void limpiar() {
		txtCodigo.setText("");

		ImageIcon noImage = new ImageIcon(Constante.rutaSinImagenPortada());
		Image imagen = noImage.getImage().getScaledInstance(130, 140, Image.SCALE_SMOOTH);

		imgAgregado.setIcon(new ImageIcon(imagen));
		txtTitulo.setText("");
		cbxAutor.setSelectedIndex(-1);
		txtIsbn.setText("");
		cbxCategoria.setSelectedIndex(-1);
		txtAnioPublicacion.setText("");
		txtCantDisp.setText("");
		txtCantidadTotal.setText("");
		rutaPortada = "";
		fileChooser.setSelectedFile(null);
	}

	protected void actionPerformedBtnLimpiar(ActionEvent e) {
		limpiar();
	}

	protected void actionPerformedBtnEliminar(ActionEvent e) {

		try {

			String mensaje = dataLibro.eliminar(txtCodigo.getText());

			JOptionPane.showMessageDialog(null, mensaje);


			cargarData();

			limpiar();
			
			actualizarRepisa.run();
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}
}
