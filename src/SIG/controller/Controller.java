package SIG.controller;

import SIG.model.FileOperations;
import SIG.model.ShowInvTabel;
import SIG.model.ShowLineTabel;
import SIG.model.sigHeader;
import SIG.model.sigItem;
import SIG.view.InvoiceFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import SIG.view.addInvoiceDialog;
import SIG.view.addLineDialog;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Controller implements ActionListener, ListSelectionListener {

    private InvoiceFrame frame;
    private sigHeader header;
    private sigItem item;
    private String name ;
    private addInvoiceDialog invDialog;
    private addLineDialog itemDialog;

    public Controller(InvoiceFrame frame) {
        this.frame = frame;

    }

    @Override
    //find which obj has been selected then selects the suitable algorithm to be performed
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {

            case "New Invoice":
                newInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "New Line":
                newLine();
                break;
            case "Delete Line":
                deleteLine();
                break;
                   
            case "createInvoice":
                addInvOk();
                break;
                
            case "cancelInvoice":
                cancelInvoice();
                break;
            case "createLine":
                createLine();
                break;
            case "cancelLine":
                cancelLine();
                break;  
                
            case "Open File":
                //create an obk from fileoperations and return a list of onvoices then updates the to tables
                //the below line cause if the user wants to read the files after booting
                frame.setInvoices(null);
                FileOperations fileOperations = new FileOperations(frame);
                ArrayList<sigHeader> inv= fileOperations.readFile();
                frame.setInvoices(inv);
                ShowInvTabel invoiceTable = new ShowInvTabel(inv);
                frame.setHeaderTabel(invoiceTable);
                frame.getTableInvoiceHeader().setModel(invoiceTable);
                frame.getHeaderTabel().fireTableDataChanged();
           
                break;

            case "Save File":
                //create an obk from fileoperations and write the data to the files
                FileOperations fileOperations1 = new FileOperations(frame);

            
                fileOperations1.saveFile(frame.getInvoices());
            
            
                break;

                
        }
    }

    private void newInvoice() {
        invDialog = new addInvoiceDialog(frame);
        invDialog.setVisible(true);

    }
//get the selected invoice from the tabel and delete it then updates the tabel
    private void deleteInvoice() {
         int row = frame.getTableInvoiceHeader().getSelectedRow();
        if(row!= -1){
            frame.getInvoices().remove(row);
            frame.getHeaderTabel().fireTableDataChanged();
            
        }
    }
//create obj dialoge of classaddLineDialoge and set it visible
    private void newLine() {
        itemDialog = new addLineDialog(frame);
        itemDialog.setVisible(true);
        
    }
//get the selected invoice from the header tabel then get the selected line from the items tabel
    //then delete the selected item then updates the items and invoices header
    private void deleteLine() {
        int invoiceSelected= frame.getTableInvoiceHeader().getSelectedRow();
          int row = frame.getTableInvoiceLines().getSelectedRow();
          //only delete if the user select an invoice from the header tabel then a item from items tabel
        if((invoiceSelected!=-1) && (row!= -1)){
            sigHeader invoice = frame.getInvoices().get(invoiceSelected);
            invoice.getItems().remove(row);
            frame.getHeaderTabel().fireTableDataChanged();
            ShowLineTabel line = new ShowLineTabel(invoice.getItems());
            frame.getTableInvoiceLines().setModel(line);
            line.fireTableDataChanged();
    }
    }

//create two arrays from sigHeader and sigItem then save them in the desired file
   
//get the invoice customer and date then create a new invoice from sigHeader class then update the tabel
    public void addInvOk() {
      String date= invDialog.getInvoiceDate().getText();
      String customer = invDialog.getCustomerName().getText();
      //get the total invoices number
      int num= frame.getTotalInvNum();
      num++;
        sigHeader newInvoice = new sigHeader(num,customer,date);
        frame.getInvoices().add(newInvoice);
        frame.getHeaderTabel().fireTableDataChanged();
        //close the dialoge
        invDialog.setVisible(false);
        invDialog.dispose();
        invDialog=null;
        
    }
//get the invoice from by searching through its number
public sigHeader getInvoiceByNum(int num){
    for(sigHeader inv: frame.getInvoices()){
        if(num==inv.getNum()){
            return inv;
        }
    } 
        return null;
}

    @Override
    public void valueChanged(ListSelectionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
//close the addInvoiceDialoge
    private void cancelInvoice() {
        invDialog.setVisible(false);
        invDialog.dispose();
        invDialog=null;
    }
/*add the item line data to the table and updates it  and add this line to its invoice
    and also updates the header table by get the selected invoice from the header table
    */
    private void createLine() {
        
      int invoiceSelected= frame.getTableInvoiceHeader().getSelectedRow();
        if(invoiceSelected!=-1){
            sigHeader invoice = frame.getInvoices().get(invoiceSelected);
            String item= itemDialog.getItemName().getText();
            String unitPrice = itemDialog.getUnitPrice().getText();
            String quantity = itemDialog.getQuantity().getText();
            double itemUnitPrice = Double.parseDouble(unitPrice);
            int itemQuantity = Integer.parseInt(quantity);
            sigItem newLine = new sigItem(item,itemQuantity,itemUnitPrice,invoice);
            invoice.getItems().add(newLine);
            ShowLineTabel line = new ShowLineTabel(invoice.getItems());
            frame.getHeaderTabel().fireTableDataChanged();
            frame.getTableInvoiceLines().setModel(line);
            line.fireTableDataChanged();

        }
        itemDialog.setVisible(false);
        itemDialog.dispose();
        itemDialog=null;
        
    }
//close the addLineDialoge
    private void cancelLine() {
        itemDialog.setVisible(false);
        itemDialog.dispose();
        itemDialog=null;
    }
}
