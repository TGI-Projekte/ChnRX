/*
 * Copyright (C) 2018 Zisis Relas, Florian Rost, Julian Blazek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.nailuj.chnrx;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.ACCEPT_ENCODING;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Networking.java Zweck: Dient als Schnittstelle zwischen Programm und dem
 * jeweiligen Spiel-Server.
 */
public class Networking {

    private String serverurl;

    public Networking(String url) {
        serverurl = url;
    }

    /**
     * Liest das aktuelle Spiel vom Webserver und deserialisiert es zu einem
     * Objekt der Klasse "Spiel"
     *
     * @return Ein Objekt der Klasse Spiel
     */
    public Spiel getGameWeb() {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverurl + "get");
            request.addHeader(ACCEPT, "application/json");
            request.addHeader(USER_AGENT, "Mozilla/5.0");
            request.addHeader(ACCEPT_ENCODING, "gzip, deflate");
            HttpResponse res = client.execute(request);
            if (res.getStatusLine().getStatusCode() != 200) {
                System.out.println("Failed to get the game from " + serverurl + " (" + res.getStatusLine().getStatusCode() + ")");
                client.close();
                return null;
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            client.close();
            return new Gson().fromJson(result.toString(), Spiel.class);
        } catch (IOException ex) {
            Logger.getLogger(SpielSteuerung.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Serialisiert und postet das gegebene Spiel auf den Spiel-Server
     *
     * @param spiel Ein Objekt der Klasse Spiel das gepostet werden soll.
     * @return Boolean ob das posten erfolgreich war
     */
    public boolean postGameWeb(Spiel spiel) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(serverurl + "post");
            post.addHeader(ACCEPT, "application/json");
            post.addHeader(USER_AGENT, "Mozilla/5.0");
            post.addHeader(ACCEPT_ENCODING, "gzip, deflate");
            String spielJson = new Gson().toJson(spiel);
            post.setEntity(new StringEntity(spielJson, ContentType.APPLICATION_JSON));
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() != 200) {
                System.out.println("Failed to post the game to " + serverurl + " (" + res.getStatusLine().getStatusCode() + ")");
                client.close();
                return false;
            }
            client.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(SpielSteuerung.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
