package com.company;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public class Translation {
        tran [] translations;
        public class tran {
            String text;
        }
    }

    public static void main(String[] args) throws IOException {  // вместо перехвата исключений используем throws
        Gson gson = new Gson();
        String API_URL = "https://api.cognitive.microsofttranslator.com//translate";
        Scanner sc = new Scanner(new File("text.txt"));
        String POSTData = "";
        String lang = "";
        String key = "b30b07d776cc433b94e9e3f3d57978de";
        String region = "westeurope";
        String result = "";
        String nameFile = "";

        while (sc.hasNextLine())
        {
            POSTData += sc.nextLine() + " ";

        }
        sc.close();
        Scanner in = new Scanner(System.in);
        Scanner name = new Scanner(System.in);
        System.out.println("Введите язык для перевода");
        while (lang.isEmpty())
            lang = in.next();

        System.out.println("Введите название файла");
        while (nameFile.isEmpty())
            nameFile = name.next();

        URL url = new URL(API_URL+"?api-version=3.0&to="+lang);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Ocp-Apim-Subscription-Key", key);
        urlConnection.setRequestProperty("Ocp-Apim-Subscription-Region", region);
        urlConnection.setRequestProperty("Content-Type","application/json");
        urlConnection.setRequestMethod( "POST");

        urlConnection.setDoOutput(true);
        OutputStream out = urlConnection.getOutputStream();
        out.write(POSTData.getBytes());

        Scanner date = new Scanner(urlConnection.getInputStream());
        if (date.hasNext()) {
            result = date.nextLine();
            result = result.substring(1,result.length()-1);
            Translation tr = gson.fromJson(result, Translation.class);
            try(FileWriter writer = new FileWriter(nameFile, false))
            {
                writer.write(tr.translations[0].text);
            }
        } else System.out.println("Запрос не вернул результата");
        urlConnection.disconnect();
    }
}
