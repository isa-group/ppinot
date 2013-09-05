package es.us.isa.bpms.repository;

import es.us.isa.bpms.model.Model;
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
public class FileSystemRepository implements ModelRepository {
    private String directory;
    @Autowired
    private UserService userService;

    public FileSystemRepository() {   }

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
    public Model getModelInfo(String id) {
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
        InputStream processReader = getModelReader(id);
        String json = IOUtils.toString(processReader);

        return Model.createModel(new JSONObject(json));
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
    public List<String> listModels() {
        return genericListProcesses("json");
    }

    @Override
    public boolean removeModel(String id) {
        File processFile = getModelFile(id);
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
    public boolean addModel(Model model) {
        boolean result = false;

        if (model.getModel() == null)
            model.setEmptyModel();

        File newModelFile = getModelFile(model.getModelId());
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
    public void saveModel(Model model) {
        OutputStream modelWriter = getModelWriter(model.getModelId());
        try {
            IOUtils.write(model.getJSON(), modelWriter);
            modelWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException("The model metadata is not valid", e);
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

    private File getModelFile(String id) {
        String filename = getBaseDirectory() + File.separator + id + ".json";
        return new File(filename);
    }

    public InputStream getModelReader(String id) {
        File processFile = getModelFile(id);

        try {
            return new FileInputStream(processFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Process with id: " + id + " not found ", e);
        }
    }

    private OutputStream getModelWriter(String id) {
        File processFile = getModelFile(id);
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
