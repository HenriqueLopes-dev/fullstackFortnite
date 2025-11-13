package io.github.HenriqueLopes_dev.FortniteAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;
import io.github.HenriqueLopes_dev.dto.cosmetic.GetCosmeticsOnShopDTO;
import io.github.HenriqueLopes_dev.dto.cosmeticBundle.CosmeticBundleDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FortniteApiService {

    private static final String BASE_URL = "https://fortnite-api.com/v2";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    private JsonNode fetchCosmetics(String path) {
        String url = BASE_URL + path;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return objectMapper.readTree(response.getBody());
    }

    public List<CosmeticDTO> getAllCosmetics() {
        JsonNode data = fetchCosmetics("/cosmetics/br").get("data");
        List<CosmeticDTO> result = new ArrayList<>();

        for (JsonNode node : data) {
            result.add(new CosmeticDTO(
                    node.path("id").asText(),
                    node.path("name").asText(),
                    node.path("description").asText(),
                    node.path("images").path("smallIcon").asText(),
                    node.path("rarity").path("value").asText(),
                    node.path("added").asText(),
                    node.path("type").path("value").asText(),
                    false
            ));
        }
        return result;
    }

    public ArrayList<String> getNewCosmeticsId() {
        JsonNode data = fetchCosmetics("/cosmetics/new").get("data").get("items").get("br");
        ArrayList<String> list = new ArrayList<>();
        for (JsonNode node : data) list.add(node.path("id").asText());
        return list;
    }

    public List<CosmeticBundleDTO> getShopBundles() {
        JsonNode entries = fetchCosmetics("/shop").get("data").get("entries");
        List<CosmeticBundleDTO> bundles = new ArrayList<>();

        for (JsonNode node : entries) {
            JsonNode bundle = node.path("bundle");

            List<GetCosmeticsOnShopDTO> cosmetics = new ArrayList<>();
            for (JsonNode item : node.path("brItems")) {
                cosmetics.add(new GetCosmeticsOnShopDTO(
                        item.path("id").asText(),
                        node.path("regularPrice").asInt(),
                        node.path("finalPrice").asInt()
                ));
            }

            bundles.add(new CosmeticBundleDTO(
                    bundle.path("name").asText(),
                    bundle.path("image").asText(),
                    node.path("regularPrice").asInt(),
                    node.path("finalPrice").asInt(),
                    cosmetics
            ));
        }

        return bundles;
    }

}
