package forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import colecciones.DatosUsuario;
import forms.frmPrincipal;
import clases.Usuario;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;



public class frmUsuario extends JDialog implements ActionListener, MouseListener {

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
	private JTextField txtEstado;
	private JButton btnAgregar;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnLimpiar;
	private JScrollPane scrollPane;
	private JTable tbUsuario;
	
	

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			frmUsuario dialog = new frmUsuario();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public frmUsuario() {
		setTitle("Mantenimiento de Usuario");
		setBounds(100, 100, 748, 535);
		getContentPane().setLayout(null);
		
		lblNombres = new JLabel("Nombres");
		lblNombres.setBounds(25, 51, 57, 14);
		getContentPane().add(lblNombres);
		
		lblDNI = new JLabel("DNI");
		lblDNI.setBounds(25, 90, 46, 14);
		getContentPane().add(lblDNI);
		
		lblEmail = new JLabel("Email");
		lblEmail.setBounds(246, 90, 46, 14);
		getContentPane().add(lblEmail);
		
		txtNombres = new JTextField();
		txtNombres.setBounds(85, 48, 372, 20);
		getContentPane().add(txtNombres);
		txtNombres.setColumns(10);
		
		txtDni = new JTextField();
		txtDni.setBounds(84, 88, 110, 20);
		getContentPane().add(txtDni);
		txtDni.setColumns(10);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(289, 87, 168, 20);
		getContentPane().add(txtEmail);
		txtEmail.setColumns(10);
		
		lblTelefono = new JLabel("Telefono");
		lblTelefono.setBounds(25, 138, 57, 14);
		getContentPane().add(lblTelefono);
		
		txtFono = new JTextField();
		txtFono.setBounds(86, 135, 108, 20);
		getContentPane().add(txtFono);
		txtFono.setColumns(10);
		
		lblEstado = new JLabel("Estado");
		lblEstado.setBounds(246, 138, 57, 14);
		getContentPane().add(lblEstado);
		
		txtEstado = new JTextField();
		txtEstado.setBounds(289, 135, 168, 20);
		getContentPane().add(txtEstado);
		txtEstado.setColumns(10);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(this);
		btnAgregar.setBounds(523, 28, 89, 23);
		getContentPane().add(btnAgregar);
		
		btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(this);
		btnModificar.setBounds(523, 72, 89, 23);
		getContentPane().add(btnModificar);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(this);
		btnEliminar.setBounds(523, 119, 89, 23);
		getContentPane().add(btnEliminar);
		
		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(this);
		btnLimpiar.setBounds(523, 166, 89, 23);
		getContentPane().add(btnLimpiar);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 211, 681, 260);
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
		cargarUsuario();
		
	}
	
	void cargarUsuario() {
		DefaultTableModel modelo=(DefaultTableModel)tbUsuario.getModel();
		modelo.setRowCount(0);
		for(int i=0; i<frmPrincipal.usuarios.longitud(); i++) {
			Usuario u=frmPrincipal.usuarios.obtener(i);
			modelo.addRow(new Object[] {
					u.codUser, u.nomUser,u.dniUser,u.emailUser,u.fonoUser,u.estadoUser
			});
		}
	}
	

	void limpiar() {
        txtNombres.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtFono.setText("");
        txtEstado.setText("");
        txtNombres.requestFocus();
    }	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
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
		// 1. Obtener los textos quitando espacios al inicio y al final
	    String nombres = txtNombres.getText().trim();
	    String dni = txtDni.getText().trim();
	    String email = txtEmail.getText().trim();
	    String fono = txtFono.getText().trim();
	    String estado = txtEstado.getText().trim();

	    // 2. Validar que no haya campos vacíos
	    if (nombres.isEmpty() || dni.isEmpty() || email.isEmpty() || fono.isEmpty() || estado.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Debe completar todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    // 3. Validar que Nombres solo contenga letras (incluye espacios, acentos y ñ)
	    if (!nombres.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
	        JOptionPane.showMessageDialog(this, "El nombre solo debe contener letras y espacios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
	        txtNombres.requestFocus();
	        return;
	    }

	    // 4. Validar formato básico de Email (debe contener '@' y un punto de dominio)
	    if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
	        JOptionPane.showMessageDialog(this, "Ingrese un correo electrónico válido (ejemplo: usuario@correo.com).", "Error de validación", JOptionPane.ERROR_MESSAGE);
	        txtEmail.requestFocus();
	        return;
	    }

	    // 5. Validar DNI (8 dígitos)
	    if (!dni.matches("\\d{8}")) {
	        JOptionPane.showMessageDialog(this, "El DNI debe tener 8 dígitos.", "Error de validación", JOptionPane.ERROR_MESSAGE);
	        txtDni.requestFocus();
	        return;
	    }

	    // 6. Validar Teléfono (9 dígitos)
	    if (!fono.matches("^9\\d{8}$")) {
	        JOptionPane.showMessageDialog(this, "El teléfono debe empezar con 9 y tener 9 dígitos.", "Error de validación", JOptionPane.ERROR_MESSAGE);
	        txtFono.requestFocus();
	        return;
	    }

	    // 7. Guardar los datos si pasa todas las validaciones
	    Usuario reg = new Usuario(nombres, dni, email, fono, estado);
	    
	    String mensaje = frmPrincipal.usuarios.agregar(reg);
	    JOptionPane.showMessageDialog(null, mensaje);

	    // Volver a listar
	    cargarUsuario();
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
				txtEstado.setText(tbUsuario.getValueAt(i, 5).toString());
		
		
	}
	protected void actionPerformedBtnEliminar(ActionEvent e) {
		int posFila = tbUsuario.getSelectedRow();

	    // 1. Verificar si hay una fila seleccionada
	    if (posFila == -1) {
	        JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    // 2. Obtener el objeto usuario para validar su estado
	    Usuario u = frmPrincipal.usuarios.obtener(posFila);

	    // 3. Validar que el estado sea "0"
	    if (!u.estadoUser.trim().equals("0")) {
	        JOptionPane.showMessageDialog(this, "Solo se pueden eliminar los usuarios que se encuentren en estado 0.", "Restricción de eliminación", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    // 4. Pedir confirmación
	    int ok = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este usuario en estado 0?", "Confirmación", JOptionPane.YES_NO_OPTION);
	    if (ok == JOptionPane.YES_OPTION) {
	        // Obtener el código de la columna 0 de la tabla (ej. "U1001")
	        String codUser = tbUsuario.getValueAt(posFila, 0).toString();
	        
	        // Llamar a tu método eliminar pasando el código
	        String mensaje = frmPrincipal.usuarios.eliminar(codUser);
	        
	        // Mostrar el mensaje devuelto, recargar la tabla y limpiar campos
	        JOptionPane.showMessageDialog(this, mensaje);
	        cargarUsuario();
	        limpiar();
	    }
		
		
	}
	protected void actionPerformedBtnLimpiar(ActionEvent e) {
		
		txtNombres.setText("");
		txtDni.setText("");
		txtEmail.setText("");
		txtFono.setText("");
		txtEstado.setText("");
	}
	protected void actionPerformedBtnModificar(ActionEvent e) {
	}
}