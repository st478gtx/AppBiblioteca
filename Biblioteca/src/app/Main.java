package app;

import forms.frmPrincipal;

import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

/** Punto de entrada de la aplicación de escritorio. */
public final class Main {
    private Main() {}

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ignored) {
            // Swing conserva su apariencia predeterminada si el tema no está disponible.
        }
        EventQueue.invokeLater(() -> {
            try {
                frmPrincipal frame = new frmPrincipal(new BibliotecaContexto());
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (RuntimeException error) {
                JOptionPane.showMessageDialog(null, error.getMessage(), "No se pudo abrir Biblioteca",
                        JOptionPane.ERROR_MESSAGE);
                error.printStackTrace();
            }
        });
    }
}
