package service;

import com.google.gson.Gson;
import model.APIKey;
import model.Cat;
import model.FavoriteCat;

import okhttp3.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CatService {
    public static void seeCats() throws IOException {
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

        //converts JSON to  java object
        Gson gson = new Gson();
        Cat cat = gson.fromJson(theJson, Cat.class);
        ImageIcon background;

        try {
            URL url = new URL(cat.getUrl());
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
            String catID = String.valueOf(cat.getId());
            String option = (String) JOptionPane.showInputDialog(null, menu, catID, JOptionPane.INFORMATION_MESSAGE, background, buttons, buttons[0]);

            int selection1 = -1;

            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])) {
                    selection1 = i;
                }
            }

            switch (selection1) {
                case 0:
                    seeCats();
                    break;
                case 1:
                    favoriteThisCat(cat);
                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void favoriteThisCat(Cat cat) {
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

    public static void seeFavoriteCats() {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request2 = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .get()
                    .addHeader("x-api-key", APIKey.getApikey())
                    .build();
            Response response2 = client.newCall(request2).execute();
            String theJson2 = response2.body().string();

            Gson gson2 = new Gson();
            FavoriteCat[] favoriteCatsArray = gson2.fromJson(theJson2, FavoriteCat[].class);

            if (favoriteCatsArray.length > 0) {
                for (FavoriteCat currentCat : favoriteCatsArray) {
                    try {
                        URL url = new URL(currentCat.image.getUrl());
                        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                        httpcon.addRequestProperty("User-Agent", "");
                        BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
                        ImageIcon background = new ImageIcon(bufferedImage);

                        if (background.getIconWidth() > 800) {
                            Image modified = background.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                            background = new ImageIcon(modified);
                        }

                        String menu = "Options: \n" +
                                "1. Delete as favorite\n" +
                                "2. See next cat\n" +
                                "3. Go back";

                        String[] buttons = {"Delete as favorite", "See next cat", "Go back"};
                        String catID = String.valueOf(currentCat.image.getId());
                        String option = (String) JOptionPane.showInputDialog(null, menu, catID, JOptionPane.INFORMATION_MESSAGE, background, buttons, buttons[0]);

                        int selection = -1;

                        for (int i = 0; i < buttons.length; i++) {
                            if (option.equals(buttons[i])) {
                                selection = i;
                            }
                        }

                        switch (selection) {
                            case 0:
                                deleteCatAsFavorite(currentCat);
                                break;
                            case 1: break;
                            default: System.exit(0);
                        }


                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void deleteCatAsFavorite(FavoriteCat favoriteCat) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/" + favoriteCat.getId())
                    .method("DELETE", body)
                    .addHeader("x-api-key", APIKey.getApikey())
                    .build();
            Response response = client.newCall(request).execute();


        } catch (IOException e) {
            System.out.println(e);
        }


    }

}



