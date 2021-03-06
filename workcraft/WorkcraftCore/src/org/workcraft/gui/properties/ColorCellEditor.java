package org.workcraft.gui.properties;

import org.workcraft.gui.controls.FlatComboBox;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ColorCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private final FlatComboBox comboBox;
    private final Color[] colors = {
            Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE,
            Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.YELLOW, Color.MAGENTA,
            Color.ORANGE, Color.PINK,
            };

    public ColorCellEditor() {
        comboBox = new FlatComboBox();
        for (Color color: colors) {
            comboBox.addItem(color);
        }
        comboBox.setEditable(true);
        comboBox.setFocusable(false);
        comboBox.setRenderer(new ColorComboBoxRenderer());

        Color color = (Color) comboBox.getSelectedItem();
        comboBox.setEditor(new ColorComboBoxEditor(color));
        comboBox.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        comboBox.setSelectedItem(value);
        return comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        fireEditingStopped();
    }

}
