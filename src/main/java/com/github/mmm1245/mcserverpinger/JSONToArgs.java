package com.github.mmm1245.mcserverpinger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class JSONToArgs {
    public static void main(String[] args) throws IOException {
        FileWriter writer = new FileWriter("outg.txt");
        JsonArray json = (JsonArray) JsonParser.parseString(Files.readString(new File("german.json").toPath()));
        int skipped = 0;
        for(JsonElement elem : json){
            JsonObject part = (JsonObject) elem;
            int range = Integer.parseInt(part.get("range").getAsString().replace(",",""));
            float log = (float) (Math.log(range) / Math.log(2));
            if(log%1!=0) {
                log = (float) Math.ceil(log);
                skipped++;
            }
            //if(log > 8)
            //   continue;
            String ipWhole = part.get("ip").getAsString() + "/" + (32-((int)log));
            writer.write(ipWhole + "\n");
            System.out.println(ipWhole);
        }
        writer.close();
        System.out.println("ceiled: " + skipped);
    }
}
