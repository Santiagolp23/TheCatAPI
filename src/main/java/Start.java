import service.catService;

import javax.swing.*;

public class Start {
    public static void main(String[] args) {
        int menuOption = -1;
        String[] buttons = {
                "1. See Cats" , "2. Exit"
        };
        //start menu
        do {
            String option = (String) JOptionPane.showInputDialog(null, "Java Cats", "Home",
                    JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
            //validate which option the user selects
            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])) {
                    menuOption = i;
                }
            }

            switch (menuOption) {
                case 0:
                    catService.seeCats();
                    break;
                default: break;
            }

        } while (menuOption!=1);

    }
}
