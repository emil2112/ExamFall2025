package app;

import app.config.ApplicationConfig;
import app.config.Populate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Populate.populate();
        ApplicationConfig.startServer(7070);
    }
}