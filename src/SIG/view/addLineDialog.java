/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SIG.view;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author ahmed
 */
public class addLineDialog extends JDialog {
    private JTextField itemName;
    private JTextField UnitPrice;
    private JTextField Quantity;
    private JLabel itemNameLabel;
    private JLabel UnitPriceLabel;
    private JLabel QuantityLabel;
    private JButton ok;
    private JButton cancel;

    public addLineDialog(InvoiceFrame frame) {
        itemNameLabel= new JLabel("Item Name:");
        itemName = new JTextField(30);
        UnitPriceLabel = new JLabel("Unit Price:");
        UnitPrice = new JTextField(30);
        QuantityLabel = new JLabel("Quantity:");
        Quantity = new JTextField(30);
        ok= new JButton("OK");
        cancel = new JButton("Cancel");
        ok.setActionCommand("createLine");
        cancel.setActionCommand("cancelLine");
        ok.addActionListener(frame.getController());
        cancel.addActionListener(frame.getController());
        setLayout(new GridLayout(4, 4));
        add(itemNameLabel);
        add(itemName);
        add(UnitPriceLabel);
        add(UnitPrice);
        add(QuantityLabel);
        add(Quantity);
        add(ok);
        add(cancel);
        
        pack();
    }

    public JTextField getItemName() {
        return itemName;
    }

    public JTextField getUnitPrice() {
        return UnitPrice;
    }

    public JTextField getQuantity() {
        return Quantity;
    }
    
}
