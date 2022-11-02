package com.github.mmm1245.mcserverpinger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.nurio.minecraft.pinger.MinecraftServerPinger;
import me.nurio.minecraft.pinger.beans.MinecraftServerStatus;
import me.nurio.minecraft.pinger.beans.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainServerPinger extends Thread {
    public static AtomicInteger countDone;
    private static int len;
    private static ConcurrentLinkedQueue<String> ips;
    private static FileWriter writer;

    public static void main(String[] args) throws IOException, InterruptedException {
        writer = new FileWriter("outputg.txt");
        JsonArray input = (JsonArray) JsonParser.parseString(Files.readString(new File("outg.json").toPath()).replace("]\n[", ","));
        countDone = new AtomicInteger(0);
        len = input.size();
        ips = new ConcurrentLinkedQueue<>();
        for (JsonElement part : input) {
            try {
                ips.add(((JsonObject) part).get("ip").getAsString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 30; i++)
            new MainServerPinger().start();
    }

    @Override
    public void run() {
        while (!ips.isEmpty()) {
            try {
                String ip = ips.poll();
                MinecraftServerStatus server = MinecraftServerPinger.ping(ip);
                if (server.isOffline())
                    continue;
                String outText = ip + " -> ";
                try {
                    outText = outText + server.getVersion().getName() + " | ";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    outText = outText + server.getMotd().replace("\n", "") + " | ";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    outText = outText + server.getPlayers().getOnline() + "/" + server.getPlayers().getMax() + " | " + server.getPlayers().getSample().stream().map(Player::getName).collect(Collectors.joining(","));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(outText);
                write(outText + "\n");
                int cnt = countDone.addAndGet(1);
                if (cnt % 10 == 0)
                    System.out.println(cnt + "/" + len);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized static void write(String input) {
        try {
            writer.write(input);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
