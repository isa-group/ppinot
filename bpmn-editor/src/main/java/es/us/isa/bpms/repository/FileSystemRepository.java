package es.us.isa.bpms.repository;

import es.us.isa.bpms.model.Model;
import es.us.isa.bpms.users.UserService;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * User: resinas
 * Date: 09/04/13
 * Time: 09:31
 */
public class FileSystemRepository implements ModelRepository {

    private static final Logger log = Logger.getLogger(FileSystemRepository.class.toString());

    @Autowired
    private UserService userService;
    private String directory;

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
    public Model getModel(String id) {
        Model m;

        try {
            m = Model.createModel(createJSONObject(getModelReader(id)));
        } catch (Exception e) {
            throw new RuntimeException("Unable to get model " + id, e);
        }

        return m;
    }

    private JSONObject createJSONObject(InputStream modelReader) throws IOException, JSONException {
        String json = IOUtils.toString(modelReader);
        return new JSONObject(json);
    }

    @Override
    public InputStream getModelReader(String id) {
        BaseDirectory baseDirectory = createBaseDirectory();
        InputStream modelReader = baseDirectory.getModelReader(id);

        try {
            JSONObject jsonObject = createJSONObject(modelReader);
            if (SharedModel.is(jsonObject)) {
                SharedModel shared = SharedModel.createFrom(jsonObject);
                modelReader = createBaseDirectory(shared.getOwner()).getModelReader(shared.getModelId());
            } else {
                modelReader = baseDirectory.getModelReader(id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to get model " + id, e);
        }

        return modelReader;
    }

    @Override
    public List<String> listModels() {
        BaseDirectory baseDirectory = createBaseDirectory();
        return baseDirectory.listModels();
    }

    @Override
    public boolean removeModel(String id) {
        BaseDirectory baseDirectory = createBaseDirectory();
        InputStream modelReader = baseDirectory.getModelReader(id);
        boolean result;

        try {
            JSONObject jsonObject = createJSONObject(modelReader);
            if (! SharedModel.is(jsonObject)) {
                Model m = Model.createModel(jsonObject);
                removeShared(m, m.getShared());
            }
            result = baseDirectory.remove(id);
        } catch (JSONException e) {
            log.warning(e.toString());
            throw new RuntimeException("Invalid model stored " + id, e);
        } catch (IOException e) {
            log.warning(e.toString());
            throw new RuntimeException("Data access error for model " + id, e);
        }

        return result;
    }

    @Override
    public boolean addModel(Model model) {
        boolean result = false;
        BaseDirectory baseDirectory = createBaseDirectory();

        if (model.getModel() == null)
            model.loadEmptyModel();

        File newModelFile = baseDirectory.getModelFile(model.getModelId());
        if (!newModelFile.exists()) {
            try {
                newModelFile.createNewFile();
                saveModelToFile(model, newModelFile);
                addShared(model, model.getShared());

                result = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        return result;
    }

    private void addShared(Model model, Set<String> shared) {
        for (String sharedUser : shared) {
            File file = createNewSharedFileForUser(sharedUser);
            SharedModel sharedModel = new SharedModel(model.getModelId(), getLoggedUser());
            saveModelToFile(sharedModel, file);
        }
    }

    private void saveModelToFile(Storeable modelToStore, File file) {
        try {
            Writer writer = new FileWriter(file);
            writer.write(modelToStore.getJSON());
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not save model to file " + file.getAbsolutePath(), e);
        }
    }

    private File createNewSharedFileForUser(String sharedUser) {
        BaseDirectory baseDirectory = createBaseDirectory(sharedUser);
        File file;
        try {
            do {
                String id = ModelUUID.generate();
                file = baseDirectory.getModelFile(id);
            } while (! file.createNewFile());
        } catch (IOException e) {
            throw new RuntimeException("Could not create new file", e);
        }
        return file;
    }

    @Override
    public void saveModel(String id, Model model) {
        if (model == null)
            throw new IllegalArgumentException("Unable to save empty model");

        BaseDirectory baseDirectory = createBaseDirectory();
        String modelId = id;
        boolean needsUpdateShare = true;

        try {
            JSONObject jsonObject = createJSONObject(baseDirectory.getModelReader(id));

            if (SharedModel.is(jsonObject)) {
                SharedModel shared = SharedModel.createFrom(jsonObject);
                baseDirectory = createBaseDirectory(shared.getOwner());
                modelId = shared.getModelId();
                needsUpdateShare = false;
            }

            if (modelId.equals(model.getModelId())) {
                if (needsUpdateShare)
                    updateShared(model);
                File modelFile = baseDirectory.getModelFile(modelId);
                saveModelToFile(model, modelFile);
            } else {
                throw new RuntimeException("Model id is not valid. Expected " + modelId + ", but found " + model.getModelId());
            }
        } catch (IOException e) {
            log.warning(e.toString());
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException("The model metadata is not valid", e);
        }
    }

    private void updateShared(Model model) {
        Model storedModel = getModel(model.getModelId());

        if (storedModel == null) {
            addShared(model, model.getShared());
        } else if ( ! model.sameSharedAs(storedModel)) {
            addShared(model, model.differenceShared(storedModel));
            removeShared(model, storedModel.differenceShared(model));
        }
    }

    private void removeShared(Model model, Set<String> shared) {
        for (String sharedUser : shared) {
            BaseDirectory baseDirectory = createBaseDirectory(sharedUser);
            List<String> models = baseDirectory.listModels();
            for (String modelId : models) {
                if (represents(baseDirectory, modelId, model)) {
                    baseDirectory.remove(modelId);
                }
            }
        }
    }

    private boolean represents(BaseDirectory baseDirectory, String modelId, Model model) {
        boolean represents = false;

        try {
            JSONObject jsonObject = createJSONObject(baseDirectory.getModelReader(modelId));
            if (SharedModel.is(jsonObject)) {
                SharedModel sharedModel = SharedModel.createFrom(jsonObject);
                if (sharedModel.represents(model.getModelId(), getLoggedUser())) {
                    represents = true;
                }
            }
        } catch (Exception e) {
            // Ignores the model
        }

        return represents;
    }


    private String getLoggedUser() {
        String email = "";
        try {
            email = userService.getLoggedUser();
        } catch (Exception e) {
            // Not logged in
        }
        return email;
    }

    private BaseDirectory createBaseDirectory() {
        String email = getLoggedUser();
        return createBaseDirectory(email);
    }

    private BaseDirectory createBaseDirectory(String email) {
        String baseDirectory = directory;

        if (email != null && !"".equals(email)) {
            baseDirectory = baseDirectory + File.separator + email.hashCode();
            File dir = new File(baseDirectory);
            if (!dir.exists()) {
                dir.mkdir();
            }
        }

        return new BaseDirectory(baseDirectory);
    }


}
