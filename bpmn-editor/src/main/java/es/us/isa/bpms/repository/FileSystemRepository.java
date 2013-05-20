package es.us.isa.bpms.repository;

import es.us.isa.bpms.users.UserService;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: resinas
 * Date: 09/04/13
 * Time: 09:31
 */
public class FileSystemRepository implements ProcessRepository {
    private String directory;
    @Autowired
    private UserService userService;

    public FileSystemRepository() {

    }

    public FileSystemRepository(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }


    @Override
    public Model getProcessModelInfo(String id) {
        Model m;
        try {
            m = getProcessJsonModelInfo(id);
        } catch (Exception e) {
            if (getProcessFile(id).exists())
                m = new Model(id, id);
            else
                throw new RuntimeException(e);
        }

        return m;
    }

    private Model getProcessJsonModelInfo(String id) throws IOException, JSONException {
        InputStream processReader = getProcessJsonReader(id);
        String json = IOUtils.toString(processReader);

        Model m = Model.createModel(new JSONObject(json));

        return m;
    }

    private List<String> genericListProcesses(String ext) {
        List<String> processes = new ArrayList<String>();
        File dir = new File(getBaseDirectory());
        File[] files = dir.listFiles();

        if (files != null) {
            for(File f : files) {
                String filename = f.getName();
                if (filename.endsWith(ext)) {
                    processes.add(filename.replace("."+ext, ""));
                }
            }
        }

        return processes;
    }

    @Override
    public List<String> listProcesses() {
        return genericListProcesses("bpmn20.xml");
    }

    @Override
    public List<String> listJsonProcesses() {
        return genericListProcesses("json");
    }

    @Override
    public boolean removeProcess(String id) {
        File processFile = getProcessJsonFile(id);
        boolean result = false;

        if (processFile.exists())
            result = processFile.delete();
        else {
            processFile = getProcessFile(id);
            result = processFile.delete();
        }

        return result;
    }

    @Override
    public boolean addProcess(Model model) {
        boolean result = false;

        if (model.getModel() == null)
            model.setEmptyModel();

        File newModelFile = getProcessJsonFile(model.getModelId());
        if (newModelFile.exists())
            return false;

        try {
            newModelFile.createNewFile();
            Writer writer = new FileWriter(newModelFile);
            String json = model.getJSON();
            writer.write(json);
            writer.close();
            result = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return result;
    }



    @Override
    public InputStream getProcessReader(String id) {
        File processFile = getProcessFile(id);

        try {
            return new FileInputStream(processFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Process with id: " + id + " not found ", e);
        }
    }

    private String getBaseDirectory() {
        String result = directory;
        try {
            String email = userService.getLoggedUser();
            result = result + File.separator + email.hashCode();
            File dir = new File(result);
            if (!dir.exists()) {
                dir.mkdir();
            }
        } catch (Exception e) {
            // Not logged in
        }

        return result;
    }

    private File getProcessFile(String id) {
        String filename = getBaseDirectory() + File.separator + id + ".bpmn20.xml";
        return new File(filename);
    }

    private File getProcessJsonFile(String id) {
        String filename = getBaseDirectory() + File.separator + id + ".json";
        return new File(filename);
    }

    public InputStream getProcessJsonReader(String id) {
        File processFile = getProcessJsonFile(id);

        try {
            return new FileInputStream(processFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Process with id: " + id + " not found ", e);
        }
    }

    @Override
    public OutputStream getProcessJsonWriter(String id) {
        File processFile = getProcessJsonFile(id);
        return getOutputStream(processFile);
    }

    @Override
    public OutputStream getProcessWriter(String id) {
        File processFile = getProcessFile(id);
        return getOutputStream(processFile);
    }

    private OutputStream getOutputStream(File processFile) {
        if (!processFile.exists()) {
            try {
                processFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Could not create new file " + processFile.getAbsolutePath(), e);
            }
        }

        try {
            return new FileOutputStream(processFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + processFile.getAbsolutePath(), e);
        }
    }
}
