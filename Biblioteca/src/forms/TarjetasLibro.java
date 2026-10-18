package forms;

import clases.Libro;
import util.Rutas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/** Tarjetas visuales del catálogo y de la selección de préstamo. */
public final class TarjetasLibro {
	private TarjetasLibro() {}

	public static JPanel catalogo(Libro libro, Consumer<Libro> agregarLibro) {
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setPreferredSize(new Dimension(220, 380));
		card.setBackground(new Color(240, 220, 200)); // Ajuste de color al card
		card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		ImageIcon original = new ImageIcon(Rutas.imagen(libro.getRutaPortada()).toString());
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
		agregar.addActionListener(e -> agregarLibro.accept(libro));

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

	public static JPanel seleccionado(Libro libro, int cantidad, Consumer<Libro> removerLibro) {
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setPreferredSize(new Dimension(100, 200));
		card.setBackground(new Color(240, 220, 200));
		card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		ImageIcon original = new ImageIcon(Rutas.imagen(libro.getRutaPortada()).toString());
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
		quitar.addActionListener(e -> removerLibro.accept(libro));

		card.add(Box.createVerticalGlue());
		card.add(img);
		card.add(Box.createVerticalStrut(5));
		card.add(lblCantidad);
		card.add(Box.createVerticalStrut(5));
		card.add(quitar);
		card.add(Box.createVerticalGlue());

		return card;
	}
}
