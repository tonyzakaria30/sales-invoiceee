


//model of invoices table 

package SIG.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
/**
 *
 * @author ahmed
 */
public class ShowInvTabel extends AbstractTableModel {

    private String[] colums = {"Num", "Date", "Customer", "Total"};
    private ArrayList<sigHeader> invoices;

    public ShowInvTabel(ArrayList<sigHeader> invoices) {
        this.invoices = invoices;
    }

    @Override
    public int getRowCount() {
        return invoices.size();
    }

    @Override
    public int getColumnCount() {
        return colums.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        sigHeader invoice = invoices.get(rowIndex);
        switch(columnIndex){
            case 0:
                return invoice.getNum();
                
            case 1:
                return invoice.getDate();
                
            case 2:
                return invoice.getName();
                
            case 3: 
                return invoice.getTotalInvoice();
            
        }
        
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return colums[column];
    }

}
