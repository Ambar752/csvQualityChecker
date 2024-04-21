package org.ambar.utils;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomerLogFormatter extends Formatter {
    public static enum format {
        BASIC,
        DETAILED;
        @Override
        public String toString() {
            switch (this) {
                case BASIC:
                    return "[Log Level Name] [Message]";
                case DETAILED:
                    return "[Log Level Name] [Timestamp] [Message]";
                default:
                    return super.toString();
            }
        }
    };

    private format inputFormat;
    public CustomerLogFormatter() {
        this.inputFormat = format.BASIC;
    }
    public CustomerLogFormatter(format formatName) {
        this.inputFormat = formatName;
    }

    public static void printFormatStyle(format inputFormatName) {
        System.out.println(inputFormatName);
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        if(this.inputFormat == format.BASIC) {
            sb.append("[").append(record.getLevel()).append("] ");
            sb.append(record.getMessage()).append("\n");
        } else if (this.inputFormat == format.DETAILED) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sb.append("[").append(record.getLevel()).append("] ");
            sb.append(dateFormat.format(record.getMillis())).append(" ");
            sb.append(record.getMessage()).append("\n");
        }
        return sb.toString();
    }
}
