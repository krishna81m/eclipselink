/*******************************************************************************
 * Copyright (c) 1998, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
package org.eclipse.persistence.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.exceptions.ValidationException;
import org.eclipse.persistence.internal.helper.Helper;

/**
 * <p><b>Purpose</b>: Default log used for the session when message logging is enabled.
 * The session can log information such as,<ul>
 * <li> all SQL executed
 * <li> informational messages
 * <li> debugging information
 * <li> all exceptions that occur within TopLink
 * </ul>
 * As well information about the message can be logged such as,<ul>
 * <li> the session logging the message
 * <li> the connection executing the SQL
 * <li> the thread in which the log entry occurred
 * <li> the exact time (to milliseconds) that the log entry occurred
 * <li> the stack trace to the exception
 * </ul>
 * @see SessionLog
 * @see AbstractSessionLog
 *
 * @author Big Country
 */
public class DefaultSessionLog extends AbstractSessionLog implements Serializable {

    /** The filename associated with this DefaultSessionLog, if it is being written out to a file **/
    protected String fileName;

    /**
     * Represents the Map that stores log levels per the name space strings.
     * The keys are category names. The values are log levels.
     */
    private Map<String, Integer> categoryLogLevelMap = new HashMap();

    /**
     * PUBLIC:
     * Create a new default session log.
     */
    public DefaultSessionLog() {
        super();
        for (int i = 0; i < loggerCatagories.length; i++) {
            String loggerCategory = loggerCatagories[i]; 
            categoryLogLevelMap.put(loggerCategory, null);
        }
    }

    /**
     * PUBLIC:
     * Create a new default session log for the given writer.
     */
    public DefaultSessionLog(Writer writer) {
        this();
        this.initialize(writer);
    }

    @Override
    public void setLevel(int level, String category) {
        if(category == null) {
            this.level = level;
        } else if(categoryLogLevelMap.containsKey(category)) {
            categoryLogLevelMap.put(category, level);
        }
    }

    /**
     * Return the logging level for the provided category. If the category is null or 
     * the level in the categoryLogLevelMap is null return the default level from
     * {@link AbstractSessionLog#getLevel(String)}.
     */
    @Override
    public int getLevel(String category) {
       if (category != null) {
           Integer catLevel = this.categoryLogLevelMap.get(category);

           if (catLevel != null) {
               return catLevel;
           }
       }

       return super.getLevel(category);
    }

    /**
     * PUBLIC:
     * <p>
     * Check if a message of the given level would actually be logged by the logger
     * with name space built from the given session and category.
     * </p><p>
     * @return true if the given message level will be logged for the given category
     * </p>
     */
    @Override
    public boolean shouldLog(int level, String category) {
        return (getLevel(category) <= level);
    }

    /**
     * Initialize the log to be backward-compatible with
     * the original TopLink log.
     */
    protected void initialize() {
        this.setShouldPrintSession(true);
        this.setShouldPrintConnection(true);
    }

    /**
     * Initialize the log.
     */
    protected void initialize(Writer writer) {
        this.writer = writer;
    }

    /**
     * PUBLIC:
     * Log the entry.
     * This writes the log entries information to a writer such as System.out or a file.
     */
    public void log(SessionLogEntry entry) {
        if (!shouldLog(entry.getLevel(), entry.getNameSpace())) {
            return;
        }

        synchronized (this) {
            try {
                printPrefixString(entry.getLevel());
                this.getWriter().write(getSupplementDetailString(entry));
    
                if (entry.hasException()) {
                    if (entry.getLevel() == SEVERE) {
                        entry.getException().printStackTrace(new PrintWriter(getWriter()));
                    } else if (entry.getLevel() <= WARNING) {
                        if (shouldLogExceptionStackTrace()) {
                            entry.getException().printStackTrace(new PrintWriter(getWriter()));
                        } else {
                            writeMessage(entry.getException().toString());
                        }
                    }
                } else {
                    writeMessage(formatMessage(entry));
                }
                getWriter().write(Helper.cr());
                getWriter().flush();
            } catch (IOException exception) {
                throw ValidationException.logIOError(exception);
            }
        }
    }

    /**
     * PUBLIC:
     * Set the writer that will receive the
     * formatted log entries for a file name.
     */
    public void setWriter(String aFileName) {
        if (aFileName != null) {
            try {
                this.writer = new FileWriter(aFileName);
                this.fileName = aFileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * PUBLIC:
     * For the given writer, return it's associated filename.
     * If associated writer does not have a filename, return null.
     */
    //Added for F2104: Properties.XML  .. gn
    public String getWriterFilename() {
        return fileName;
    }

    /**
     * Append the specified message information to the writer.
     */
    protected void writeMessage(String message) throws IOException {
        this.getWriter().write(message);
    }

    /**
     * Append the separator string to the writer.
     */
    protected void writeSeparator() throws IOException {
        this.getWriter().write("--");
    }
}
