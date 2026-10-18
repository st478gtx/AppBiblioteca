package forms;


import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import colecciones.DatosUsuario;
import app.BibliotecaContexto;
import service.UsuarioService;
import clases.Usuario;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.Color;



public class frmUsuario extends JDialog implements ActionListener, MouseListener {
	private final UsuarioService usuarios;
	private final Runnable actualizarUsuarios;

	private static final long serialVersionUID = 1L;
	private JLabel lblNombres;
	private JLabel lblDNI;
	private JLabel lblEmail;
	private JTextField txtNombres;
	private JTextField txtDni;
	private JTextField txtEmail;
	private JLabel lblTelefono;
	private JTextField txtFono;
	private JLabel lblEstado;
	private JButton btnAgregar;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnLimpiar;
	private JScrollPane scrollPane;
	private JTable tbUsuario;
	private JComboBox<String> cboEstado;
	private JButton btnGuardar;
	private JLabel lblTitulo;
	private JLabel lblBusqueda;
	private JTextField txtBuscar;
	private JButton btnBuscar;
	
	

	
	/**
	 * Launch the application.
	 */
	/**
	 * Create the dialog.
	 */
	public frmUsuario(BibliotecaContexto contexto, Runnable actualizarUsuarios) {
		this.usuarios = contexto.usuarios();
		this.actualizarUsuarios = actualizarUsuarios;
		setBackground(new Color(240, 240, 240));
		setTitle("Mantenimiento de Usuario");
		setBounds(100, 100, 748, 640);
		getContentPane().setLayout(null);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(this);
		btnBuscar.setBounds(598, 23, 89, 23);
		getContentPane().add(btnBuscar);
		
		txtBuscar = new JTextField();
		txtBuscar.setBackground(new Color(255, 255, 255));
		txtBuscar.setBounds(469, 24, 121, 20);
		getContentPane().add(txtBuscar);
		txtBuscar.setColumns(10);
		
		lblNombres = new JLabel("Nombres");
		lblNombres.setBounds(37, 103, 57, 14);
		getContentPane().add(lblNombres);
		
		lblDNI = new JLabel("DNI");
		lblDNI.setBounds(37, 142, 46, 14);
		getContentPane().add(lblDNI);
		
		lblEmail = new JLabel("Email");
		lblEmail.setBounds(258, 142, 46, 14);
		getContentPane().add(lblEmail);
		
		txtNombres = new JTextField();
		txtNombres.setBounds(97, 100, 372, 20);
		getContentPane().add(txtNombres);
		txtNombres.setColumns(10);
		
		txtDni = new JTextField();
		txtDni.setBounds(96, 140, 110, 20);
		getContentPane().add(txtDni);
		txtDni.setColumns(10);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(301, 139, 168, 20);
		getContentPane().add(txtEmail);
		txtEmail.setColumns(10);
		
		lblTelefono = new JLabel("Telefono");
		lblTelefono.setBounds(37, 190, 57, 14);
		getContentPane().add(lblTelefono);
		
		txtFono = new JTextField();
		txtFono.setBounds(98, 187, 108, 20);
		getContentPane().add(txtFono);
		txtFono.setColumns(10);
		
		lblEstado = new JLabel("Estado");
		lblEstado.setBounds(258, 190, 57, 14);
		getContentPane().add(lblEstado);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(this);
		btnAgregar.setBounds(529, 84, 121, 23);
		getContentPane().add(btnAgregar);
		
		btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(this);
		btnModificar.setBounds(529, 118, 121, 23);
		getContentPane().add(btnModificar);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(this);
		btnEliminar.setBounds(529, 152, 121, 23);
		getContentPane().add(btnEliminar);
		
		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(this);
		btnLimpiar.setBounds(529, 185, 121, 23);
		getContentPane().add(btnLimpiar);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 276, 681, 287);
		getContentPane().add(scrollPane);
		
		tbUsuario = new JTable();
		tbUsuario.addMouseListener(this);
		tbUsuario.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"C\u00F3digo", "Nombres", "DNI", "Email", "Tel\u00E9fono", "Estado"
			}
		));
		scrollPane.setViewportView(tbUsuario);
		
		cboEstado = new JComboBox<>();
		cboEstado.setBounds(301, 186, 168, 22);
		getContentPane().add(cboEstado);
		
		btnGuardar = new JButton("Guardar datos");
		btnGuardar.addActionListener(this);
		btnGuardar.setBounds(529, 219, 121, 23);
		getContentPane().add(btnGuardar);
		
		lblTitulo = new JLabel("Mantenimiento de usuario");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblTitulo.setBounds(97, 24, 326, 45);
		getContentPane().add(lblTitulo);
		
		lblBusqueda = new JLabel("");
		lblBusqueda.setLabelFor(this);
		lblBusqueda.setBackground(new Color(0, 128, 255));
		lblBusqueda.setBounds(452, 11, 254, 45);
		lblBusqueda.setOpaque(true);
		getContentPane().add(lblBusqueda);
		cargarUsuario();
		listarEstado();
		
	}
	
	void cargarUsuario() {
		DefaultTableModel modelo=(DefaultTableModel)tbUsuario.getModel();
		modelo.setRowCount(0);
		for(int i=0; i<usuarios.longitud(); i++) {
			Usuario u=usuarios.obtener(i);
			modelo.addRow(new Object[] {
					u.getCodUser(), u.getNomUser(),u.getDniUser(),u.getEmailUser(),u.getFonoUser(),u.getEstadoUser()
			});
		}
	}
	

	void limpiar() {
        txtNombres.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtFono.setText("");
        cboEstado.setSelectedIndex(-1);
        txtNombres.requestFocus();
    }	
	
	void listarEstado() {
	    cboEstado.removeAllItems(); // Limpia por si acaso tiene elementos previos
	    cboEstado.addItem("-- Seleccione --");
	    cboEstado.addItem("Registrado");
	    cboEstado.addItem("Con Prestamo");
	    cboEstado.addItem("Penalizado");
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnBuscar) {
			actionPerformedBtnBuscar(e);
		}
		if (e.getSource() == btnGuardar) {
			actionPerformedBtnGuardar(e);
		}
		if (e.getSource() == btnModificar) {
			actionPerformedBtnModificar(e);
		}
		if (e.getSource() == btnLimpiar) {
			actionPerformedBtnLimpiar(e);
		}
		if (e.getSource() == btnEliminar) {
			actionPerformedBtnEliminar(e);
		}
		if (e.getSource() == btnAgregar) {
			actionPerformedBtnAgregar(e);
		}
	}
protected void actionPerformedBtnAgregar(ActionEvent e) {
		Usuario usuario = new Usuario(txtNombres.getText().trim(), txtDni.getText().trim(),
				txtEmail.getText().trim(), txtFono.getText().trim(), (String) cboEstado.getSelectedItem());
		try {
			String mensaje = usuarios.agregar(usuario);
			JOptionPane.showMessageDialog(this, mensaje);
			cargarUsuario();
			actualizarUsuarios.run();
		} catch (IllegalArgumentException error) {
			JOptionPane.showMessageDialog(this, error.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == tbUsuario) {
			mouseClickedTbUsuario(e);
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
	protected void mouseClickedTbUsuario(MouseEvent e) {
		//al hacer click a la fila seleccionada (SelectedRow), visualizamos los datos
				int i=tbUsuario.getSelectedRow();
				txtNombres.setText(tbUsuario.getValueAt(i, 1).toString());
				txtDni.setText(tbUsuario.getValueAt(i, 2).toString());
				txtEmail.setText(tbUsuario.getValueAt(i, 3).toString());
				txtFono.setText(tbUsuario.getValueAt(i, 4).toString());
				cboEstado.setSelectedItem(tbUsuario.getValueAt(i, 5).toString());
		
		
	}
	protected void actionPerformedBtnEliminar(ActionEvent e) {
		int posFila = tbUsuario.getSelectedRow();

	    // 1. Verificar si hay una fila seleccionada
	    if (posFila == -1) {
	        JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    String codUser = tbUsuario.getValueAt(posFila, 0).toString();
	    try {
	        usuarios.validarEliminacion(codUser);
	    } catch (IllegalArgumentException error) {
	        JOptionPane.showMessageDialog(this, error.getMessage(), "Restricción de eliminación", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    // 4. Pedir confirmación
	    int ok = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este usuario?", "Confirmación", JOptionPane.YES_NO_OPTION);
	    if (ok == JOptionPane.YES_OPTION) {
	        // Obtener el código de la columna 0 de la tabla (ej. "U1001")
	        
	        // Llamar a tu método eliminar pasando el código
	        String mensaje = usuarios.eliminar(codUser);
	        
	        // Mostrar el mensaje devuelto, recargar la tabla y limpiar campos
	        JOptionPane.showMessageDialog(this, mensaje);
	        cargarUsuario();
	        actualizarUsuarios.run();
	        limpiar();
	    }
		
		
	}
	protected void actionPerformedBtnLimpiar(ActionEvent e) {
		
		txtNombres.setText("");
		txtDni.setText("");
		txtEmail.setText("");
		txtFono.setText("");
		cboEstado.setSelectedItem(null);
	}
protected void actionPerformedBtnModificar(ActionEvent e) {
		int fila = tbUsuario.getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario de la tabla.");
			return;
		}
		Usuario usuario = new Usuario(tbUsuario.getValueAt(fila, 0).toString(),
				txtNombres.getText().trim(), txtDni.getText().trim(), txtEmail.getText().trim(),
				txtFono.getText().trim(), (String) cboEstado.getSelectedItem());
		try {
			String mensaje = usuarios.actualizar(usuario);
			JOptionPane.showMessageDialog(this, mensaje);
			cargarUsuario();
			actualizarUsuarios.run();
		} catch (IllegalArgumentException error) {
			JOptionPane.showMessageDialog(this, error.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
		}
	}
	protected void actionPerformedBtnGuardar(ActionEvent e) {
		
		String mensaje=usuarios.guardar();
		JOptionPane.showMessageDialog(null, mensaje);
		
		
	}
	protected void actionPerformedBtnBuscar(ActionEvent e) {
		// 1. Obtener el texto ingresado en el buscador limpiando espacios
	    String busqueda = txtBuscar.getText().trim();
	    
	    // 2. Validar que el campo no esté vacío
	    if (busqueda.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Ingrese un código o DNI para buscar.");
	        txtBuscar.requestFocus();
	        return;
	    }
	    
	    // 3. Llamar al método de búsqueda que creamos en DatosUsuario
	    Usuario u = usuarios.buscar(busqueda);
	    
	    // 4. Verificar si se encontró el usuario
	    if (u != null) {
	        // Mostrar los datos en las cajas de texto de la interfaz
	        txtNombres.setText(u.getNomUser());
	        txtDni.setText(u.getDniUser());
	        txtEmail.setText(u.getEmailUser());
	        txtFono.setText(u.getFonoUser());
	        cboEstado.setSelectedItem(u.getEstadoUser());
	        
	        // Opcional: Seleccionar automáticamente la fila correspondiente en la JTable
	        for (int i = 0; i < tbUsuario.getRowCount(); i++) {
	            String codigoTabla = tbUsuario.getValueAt(i, 0).toString();
	            if (codigoTabla.equalsIgnoreCase(u.getCodUser())) {
	                tbUsuario.setRowSelectionInterval(i, i);
	                break;
	            }
	        }
	    } else {
	        JOptionPane.showMessageDialog(this, "No se encontró ningún usuario con el código o DNI ingresado.");
	        limpiar(); // Limpia los campos si no existe
	    }
		
	}
}
