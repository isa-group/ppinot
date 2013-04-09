package es.us.isa.ppinot.repository;

/**
 * User: resinas
 * Date: 09/04/13
 * Time: 08:54
 */
public class ProcessInfo {

    private String name;
    private String url;
    private String editor;

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public ProcessInfo() {

    }

    public ProcessInfo(String name, String url) {
        super();
        this.name = name;
        this.url = url;
    }

    public ProcessInfo(String name, String url, String editor) {
        this(name,url);
        this.editor = editor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

