package service;

import com.google.gson.Gson;
import model.APIKey;
import model.Cats;
import okhttp3.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CatService {
    public static Cats seeCats() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .get()
                .build();
        Response response = client.newCall(request).execute();


        String theJson = response.body().string();
        theJson = theJson.substring(1, theJson.length() - 1);

        //converts java object to JSON
        Gson gson = new Gson();
        Cats cats = gson.fromJson(theJson, Cats.class);
        ImageIcon background;

        try {
            URL url = new URL(cats.getUrl());
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
            background = new ImageIcon(bufferedImage);

            if (background.getIconWidth() > 800) {
                Image modified = background.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                background = new ImageIcon(modified);
            }

            String menu = "Options: \n" +
                    "1. See another image\n" +
                    "2. Favorite\n" +
                    "3. Go back";

            String[] buttons = {"see another image", "favorite", "go back"};
            String catID = String.valueOf(cats.getId());
            String option = (String) JOptionPane.showInputDialog(null, menu, catID, JOptionPane.INFORMATION_MESSAGE, background, buttons, buttons[0]);

            int selection = -1;

            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])) {
                    selection = i;
                }
            }

            switch (selection) {
                case 0:
                    seeCats();
                    break;
                case 1:
                    favoriteThisCat(cats);
                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        return cats;
    }

    public static void favoriteThisCat(Cats cat) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\": \"" + cat.getId() + "\"\r\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", APIKey.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

}
