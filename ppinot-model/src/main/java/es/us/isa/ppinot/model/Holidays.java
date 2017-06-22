package es.us.isa.ppinot.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.joda.time.DateTime;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author feserafim
 * @version 1.0
 */
public class Holidays {
    
    @JsonDeserialize(as=ArrayList.class, contentAs=DateTime.class)
    private List<DateTime> list;
    
    public List<DateTime> getList() {
        return this.list;
    }
    
    public void setList(List<DateTime> list) {
        this.list = list;
    }
    
    public static List<DateTime> getHolidaysFromJson() {
        
        ObjectMapper mapper = new ObjectMapper();
        Holidays h = new Holidays();
        String json;
        
        try {
            json = loadFile(Holidays.class.getResourceAsStream("/holidays.json"));
            h = mapper.readValue(json, Holidays.class);
        } catch (IOException ex) {
            Logger.getLogger(Schedule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return h.getList();
    }

    private static String loadFile(InputStream is) throws FileNotFoundException, IOException {
        String result = "";
        result = getStringFromInputStream(is);
        is.close();

        return result;
        
    }

    private static String getStringFromInputStream(InputStream in) throws IOException {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line.replaceAll("	", "\t")).append("\n");
        }
        
        return sb.toString();
        
    }

}
