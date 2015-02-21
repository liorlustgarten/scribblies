package scribblies;


/**
 * Created by Lior on 21/02/2015.
 */
public class Prompt {
    private String promptMessage;
    private String saveFileName;

    public Prompt (String promptMessage, String saveFileName)
    {
        this.promptMessage = promptMessage;
        this.saveFileName = saveFileName;
    }

    public String getPromptMessage() {
        return promptMessage;
    }

    public void setPromptMessage(String promptMessage) {
        this.promptMessage = promptMessage;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }
}
