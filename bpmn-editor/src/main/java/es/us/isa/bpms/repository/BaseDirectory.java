package es.us.isa.bpms.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BaseDirectory {
    private final String baseDirectory;

    public BaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public List<String> listModels() {
        return genericListProcesses("json");
    }

    private List<String> genericListProcesses(String ext) {
        List<String> processes = new ArrayList<String>();
        File dir = new File(baseDirectory);
        File[] files = dir.listFiles();

        if (files != null) {
            for (File f : files) {
                String filename = f.getName();
                if (filename.endsWith(ext)) {
                    processes.add(filename.replace("." + ext, ""));
                }
            }
        }

        return processes;
    }

    public File getModelFile(String id) {
        String filename = baseDirectory + File.separator + id + ".json";
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

    public boolean remove(String id) {
        File modelFile = getModelFile(id);
        boolean result = false;

        if (modelFile.exists())
            result = modelFile.delete();

        return result;
    }


}