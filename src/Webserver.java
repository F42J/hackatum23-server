import io.javalin.Javalin;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Webserver {
    Database data =new Database();
    public void run() {
        data.connect();
        var app= Javalin.create()
                .get("/profile/{id}",ctx -> {
                    var json=fetchProfile(ctx.pathParam("id"));
                    if (json!=null) {ctx.result(json);ctx.status(200);}
                    else {ctx.status(500);ctx.result("Internal Server Error");}
                })
                .get("/profile/{id}/picture", ctx->ctx.result("TODO"))
                .get("/innovation/{id}",ctx -> {
                    var json=fetchInnovation(ctx.pathParam("id"));
                    if (json!=null) {ctx.result(json);ctx.status(200);}
                    else {ctx.status(500);ctx.result("Internal Server Error");}
                })
                .start(8812);

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        app.stop();
    }

    private String fetchInnovation(String id) {
        try {
            ResultSet res= data.fetchInnovation(id);
            if (res==null||!res.next()) return null;
            JSONObject json=new JSONObject();
            json.put("id",res.getInt("id"));
            json.put("title",res.getString("title"));
            json.put("description",res.getString("bio"));
            ArrayList<String> skills = new ArrayList<>();
            do {
                skills.add(res.getString("skill"));
            } while (res.next());
            json.put("skills",skills);
            return json.toString();
        } catch (SQLException e) {
            return null;
        }
    }

    private String fetchProfile(String id) {
        try {
            ResultSet res= data.fetchProfile(id);
            if (res==null||!res.next()) return null;

            JSONObject json=new JSONObject();
            json.put("id",res.getInt("id"));
            json.put("name",res.getString("name"));
            json.put("bio",res.getString("bio"));
            json.put("verified",res.getInt("verified"));
            ArrayList<String> skills = new ArrayList<>();
            while (res.next()) {
                skills.add(res.getString("description"));
            }
            json.put("skills",skills);
            return json.toString();
        } catch (SQLException e) {
            return null;
        }
    }

    private String fetchProfilePicture(String id) {
        return "TODO";
    }
}


