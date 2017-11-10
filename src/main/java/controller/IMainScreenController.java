package controller;
import model.Photo;

//Used to interface with the MainScreenController
public interface IMainScreenController {

    public abstract Controller openScreen (String screen, String header);
    public abstract void createGraphForPhoto(Photo photo);
}