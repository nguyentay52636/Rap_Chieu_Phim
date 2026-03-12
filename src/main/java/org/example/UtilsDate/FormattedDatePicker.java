package org.example.UtilsDate;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class FormattedDatePicker extends JDatePickerImpl{
    private UtilDateModel model;

    public FormattedDatePicker(Date defaultDate){
        super(createDatePanel(), new DateComponentFormatter(){
            @Override
            public String valueToString(Object value) throws ParseException {
                Calendar cal = (Calendar) value;
                return cal == null ? "" : UtilsDateFormat.formatDate(cal.getTime());
            }

            @Override
            public Object stringToValue(String text) throws ParseException {
                if(text != null && !text.equals("")) {
                    Date date = UtilsDateFormat.stringToDate(text);
                    Calendar calender = Calendar.getInstance();
                    calender.setTime(date);
                    return calender;
                } else {
                    return null;
                }
            }
        });
        
        this.model = (UtilDateModel) this.getModel();
        setDate(defaultDate);
    }

    private static JDatePanelImpl createDatePanel(){
        UtilDateModel model = new UtilDateModel();
        
        Properties p = new Properties();
        p.put("text.today", "Hôm nay");
        p.put("text.month", "Tháng");
        p.put("text.year", "Năm");

        return new JDatePanelImpl(model, p);
    }

    public Date getDate(){
        return model.getValue();
    }

    public void setDate(Date date){
        model.setValue(date);
        model.setSelected(date != null);
    }
}
