package es.us.isa.ppinot.repository;

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
    public List<String> listProcesses() {
        List<String> processes = new ArrayList<String>();
        File dir = new File(directory);
        File[] files = dir.listFiles();

        if (files != null) {
            for(File f : files) {
                String filename = f.getName();
                if (filename.endsWith("bpmn20.xml")) {
                    processes.add(filename.replace(".bpmn20.xml", ""));
                }
            }
        }

        return processes;
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

    private File getProcessFile(String id) {
        String filename = directory + File.separator + id + ".bpmn20.xml";
        return new File(filename);
    }


    @Override
    public OutputStream getProcessWriter(String id) {
        File processFile = getProcessFile(id);
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
