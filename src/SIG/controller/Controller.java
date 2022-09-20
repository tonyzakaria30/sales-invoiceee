package SIG.controller;

import SIG.model.ShowInvTabel;
import SIG.model.ShowLineTabel;
import SIG.model.sigHeader;
import SIG.model.sigItem;
import SIG.view.InvoiceFrame;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import SIG.view.addInvoiceDialog;
import SIG.view.addLineDialog;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
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
            {
                try {
                    openFile(null, null);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                break;

            case "Save File":
            {
                try {
                    saveFile();
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
/*this function takes two file name if the item path and hader path is null
    then open file chosser for the two tables then updates the tables
    */
    public void openFile(String headerPath, String itemPath) throws IOException {
        File headerFile = null;
        File itemFile = null;
        if (headerPath == null && itemPath == null) {
            JFileChooser file = new JFileChooser();
            int result = file.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                headerFile = file.getSelectedFile();
                result = file.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    itemFile = file.getSelectedFile();

                }
            }
        } else {
            headerFile = new File(headerPath);
            itemFile = new File(itemPath);
        }
        if (headerFile != null && itemFile != null) {
            try {
                //ready each line in each file as string then save them in array of strings
                List<String> headerLines = Files.lines(Paths.get(headerFile.getAbsolutePath())).collect(Collectors.toList());

                List<String> itemLines = Files.lines(Paths.get(itemFile.getAbsolutePath())).collect(Collectors.toList());
                ArrayList<sigHeader> invArray = new ArrayList<>();
                
                for (String headerLine : headerLines) {
                    /*split each element in header arrays that has the invoices from header file
                    then dipose ',' and save each string in another array of strings
                    */
                    String[] arr = headerLine.split(",");
                    String numToString = arr[0];
                    String date = arr[1];
                    String customerName = arr[2];
                    int num = Integer.parseInt(numToString);
                    //create new invoice and add it in  invArray
                    sigHeader invoice = new sigHeader(num, customerName, date);
                    invArray.add(invoice);
                    frame.getInvoices().add(invoice);
                   
                }
                /*split each element in itemLine arrays that has the invoices from items file
                    then dipose ',' and save each string in another array of strings
                    */
                for (String itemLine : itemLines) {
                    String[] arr = itemLine.split(",");
                    int num = Integer.parseInt(arr[0]);
                    String name1 = arr[1];
                    double unitPrice = Double.parseDouble(arr[2]);
                    int quantity = Integer.parseInt(arr[3]);
                    //create new line and add it inside its invoice
                    sigHeader invoice=getInvoiceByNum(num);
                    sigItem line = new sigItem(name1,quantity,unitPrice,invoice);
                    invoice.getItems().add(line);

                    
                 
                }
                //update the two tables
                frame.setInvoices(invArray);
                ShowInvTabel invoiceTable = new ShowInvTabel(invArray);
                frame.setHeaderTabel(invoiceTable);
                frame.getTableInvoiceHeader().setModel(invoiceTable);
                frame.getHeaderTabel().fireTableDataChanged();
                        
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
      
    }
//create two arrays from sigHeader and sigItem then save them in the desired file
    private void saveFile() throws IOException {
        /*create new array of sigHeader to get the current invoices
        create two strings to stroe in them the final data as string data
        */
        ArrayList<sigHeader> headers = frame.getInvoices();
        String invoices = "";
        String items = "";
        /*read each invoice from the header table and save it in a new line inside invoices string
        then read each item line inside the invoice and save it i a new line inside items string
        */
        for(sigHeader header: headers){
            String headerFile = header.getInvoicesFromTabel();
            invoices=invoices+headerFile;
            invoices=invoices+"\n";
            
            for(sigItem item : header.getItems()){
                String itemFile = item.getItemsFromTabel();
                items = items+itemFile;
                items = items+"\n";
            }
        }
        //open two file chooser to save these two strings inside the desired files
        JFileChooser file = new  JFileChooser();
        int result = file.showSaveDialog(frame);
        if(result == JFileChooser.APPROVE_OPTION){
            File headerFile = file.getSelectedFile();
            FileWriter headFileWriter = new FileWriter(headerFile);
            headFileWriter.write(invoices);
            headFileWriter.flush();
            headFileWriter.close();
            result = file.showSaveDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION){
                File lineFile= file.getSelectedFile();
                FileWriter lineFileWriter = new FileWriter(lineFile);
                lineFileWriter.write(items);
                lineFileWriter.flush();
                lineFileWriter.close();
            }
        }
    }
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
