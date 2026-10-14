package forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class frmUsuarioBuscar extends JDialog {

	private static final long serialVersionUID = 1L;
	private JRadioButton rdbtnDni;
	private JRadioButton rdbtnCodigo;
	private JTextField textField;
	private JButton btnBuscar;
	private JButton btnLimpiar;
	private JButton btnEliminar;
	private JButton btnGuardar;
	private JScrollPane scrollPane;
	private JTable tbUsuario;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			frmUsuarioBuscar dialog = new frmUsuarioBuscar();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public frmUsuarioBuscar() {
		setTitle("Buscar Usuario");
		setBounds(100, 100, 632, 366);
		getContentPane().setLayout(null);
		{
			JLabel lblTitulo = new JLabel("Buscar Usuario");
			lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
			lblTitulo.setBounds(210, 11, 198, 39);
			getContentPane().add(lblTitulo);
		}
		
		rdbtnDni = new JRadioButton("Por DNI");
		rdbtnDni.setBounds(31, 63, 109, 23);
		getContentPane().add(rdbtnDni);
		
		rdbtnCodigo = new JRadioButton("Por Codigo");
		rdbtnCodigo.setBounds(166, 63, 109, 23);
		getContentPane().add(rdbtnCodigo);
		
		textField = new JTextField();
		textField.setBounds(54, 106, 122, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(308, 63, 115, 23);
		getContentPane().add(btnBuscar);
		
		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.setBounds(308, 105, 115, 23);
		getContentPane().add(btnLimpiar);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(447, 63, 120, 23);
		getContentPane().add(btnEliminar);
		
		btnGuardar = new JButton("Guardar Datos");
		btnGuardar.setBounds(447, 105, 120, 23);
		getContentPane().add(btnGuardar);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(24, 157, 543, 139);
		getContentPane().add(scrollPane);
		
		tbUsuario = new JTable();
		tbUsuario.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"C\u00F3digo", "Nombres", "DNI", "Email", "Tel\u00E9fono", "Estado"
			}
		));
		scrollPane.setViewportView(tbUsuario);
	}
}
