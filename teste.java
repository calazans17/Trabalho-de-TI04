import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;



public class Main {
  
    private static final String MODEL_URL = "";
    
    
    private static final String API_KEY = "7abff618-8905-4cbd-a8e6-65188f0cc210";

    public static void main(String[] Args) throws Exception {
       
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(MODEL_URL))
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(sampleData()))
                .build();

        try {
            // Realiza-se a chamada HTTP para o servidor do modelo. O objeto `client` definido na linha 40
            // chama o método `#send` passando o request da linha 41, que é quem contém as informações da URL, 
            // autenticação com a API_KEY e os dados a serem classificados.
            HttpResponse<String> response  = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Convertemos a reposta para uma List de objetos de HashMap. Nas linhas 86-111 há um exemplo de retorno
            // da função `.responseMapBody`.
            List<Map<String, Object>> classification = responseMapBody(response.body());
            System.out.println(classification);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String sampleData() {
        JSONArray array = new JSONArray();
        JSONObject item = new JSONObject();
        item.put("class", "");
        item.put("valence", 3.9394);
        item.put("energy", 1);
        item.put("liveness", 1);
        item.put("speechiness", 1);
        item.put("instrumentalness", 1);
        item.put("tempo", 150);
        item.put("danceability", 8.393);
        item.put("acousticness", 0.992);

        array.add(item);

        return array.toString();
    }

  
    private static List<Map<String, Object>> responseMapBody(String body) {
        Map<String, Object> hm;
        List<Map<String, Object>> res = new ArrayList<>();

        // Parseia a resposta em string para JSON
        Object obj = JSONValue.parse(body);
        JSONObject jsonObject = (JSONObject) obj;
        
        // O retorno do modelo vem dentro da chave `result` 
        JSONArray objs = (JSONArray) jsonObject.get("result"); 

        // Iterar sobre os objetos do `result`, onde cada um representa o resultado da classificação de um set
        // de características de áudio do Spotify
        for (Object _obj : objs) {
            hm = new HashMap<>();
            // Obtém o set de chaves do objeto e itera sobre eles, acessando o valor de cada um 
            // e o adicionando no dicionário em Java a ser retornado
            for (Object o : ((JSONObject) _obj).keySet()) {
                String key = (String) o;
                hm.put(key, ((JSONObject) _obj).get(key));
            }
            res.add(hm);
        }

        return res;
    }
}