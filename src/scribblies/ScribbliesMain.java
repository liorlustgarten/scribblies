package scribblies;

/**
 * Created by dgli on 16/02/15.
 */
public class ScribbliesMain implements Runnable{

    MainWindow mainWindow;

    public static void main(String args[]){

        Thread GUIThread = new Thread(new ScribbliesMain());
        GUIThread.start();

    }

    @Override
    public void run() {
        mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }


}
