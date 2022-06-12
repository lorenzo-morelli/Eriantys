package it.polimi.ingsw.utils.gui;


import it.polimi.ingsw.server.model.characters.CharacterCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    public static List<ImageView> toImageCharacters(List<String> stringList) {
        List<ImageView> images = new ArrayList<>();
        stringList.forEach((student) -> {
            switch (student) {
                case "rosso":
                    images.add(new ImageView("graphics/pieces/students/student_red.png"));
                    break;
                case "blu":
                    images.add(new ImageView("graphics/pieces/students/student_blue.png"));
                    break;
                case "verde":
                    images.add(new ImageView("graphics/pieces/students/student_green.png"));
                    break;
                case "giallo":
                    images.add(new ImageView("graphics/pieces/students/student_giallo.png"));
                    break;
            }
        });
        return images;
    }

    public static void toImageCharacters(ArrayList<CharacterCard> characterCard, List<ImageView> images) {
        for (int i = 0; i < 12; i++) { //todo debug
            Image character = null;
            switch (characterCard.get(i).getName()) {
                case "MONK":
                    character = new Image("graphics/characters/monk.jpg");
                    break;
                case "PRINCESS":
                    character = new Image("graphics/characters/princess.jpg");
                    break;
                case "MUSHROOMHUNTER":
                    character = new Image("graphics/characters/mushroom_hunter.jpg");
                    break;
                case "THIEF":
                    character = new Image("graphics/characters/thief.jpg");
                    break;
                case "FARMER":
                    character = new Image("graphics/characters/farmer.jpg");
                    break;
                case "CENTAUR":
                    character = new Image("graphics/characters/centaur.jpg");
                    break;
                case "KNIGHT":
                    character = new Image("graphics/characters/knight.jpg");
                    break;
                case "POSTMAN":
                    character = new Image("graphics/characters/postman.jpg");
                    break;
                case "GRANNY":
                    character = new Image("graphics/characters/granny.jpg");
                    break;
                case "JESTER":
                    character = new Image("graphics/characters/jester.jpg");
                    break;
                case "HERALD":
                    character = new Image("graphics/characters/herald.jpg");
                    break;
                case "MINSTRELL":
                    character = new Image("graphics/characters/minstrell.jpg");
                    break;
            }
            images.get(i).setImage(character);
        }
    }

    public static void toImageAssistants(List<Integer> values, List<ImageView> images) {
//        for (int i = 0; i < values.size(); i++) {
//            if (values.get(i) == 0) {
//                images.get(i).setVisible(false);
//            } else {
//                Image assistant = new Image("graphics/assistants/assistantCard" + values.get(i) + ".png");
//                images.get(i).setImage(assistant);
//            }
//        }
        values.forEach(value -> {
            if (value == 0) {
                images.get(values.indexOf(value)).setVisible(false);
            } else {
                Image assistantImage = new Image("graphics/assistants/assistantCard" + values.get(values.indexOf(value)) + ".png");
                images.get(values.indexOf(value)).setImage(assistantImage);
            }
        });
    }

    public static int getColorPlace(String color) {
        int n = -1;
        switch (color) {
            case "green": n = 0; break;
            case "red": n = 1; break;
            case "yellow": n = 2; break;
            case "pink": n = 3; break;
            case "blue": n = 4; break;

        }
        return n;
    }

}
