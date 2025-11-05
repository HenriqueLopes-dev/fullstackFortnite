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
    private JsonNode fetchCosmetics(String path){
        String url = BASE_URL + path;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return objectMapper.readTree(response.getBody()).get("data");

    }

    public List<CosmeticDTO> getAllCosmetics() {

        JsonNode data = fetchCosmetics("/cosmetics/br");

        List<CosmeticDTO> result = new ArrayList<>();

        for (JsonNode node : data) {
            CosmeticDTO dto = new CosmeticDTO(
                    node.get("id").asText(),
                    node.get("name").asText(),
                    node.get("description").asText(),
                    node.get("images").get("smallIcon").asText(),
                    node.get("rarity").get("value").asText(),
                    node.get("added").asText(),
                    node.get("type").get("value").asText(),
                    false,
                    false,
                    0,
                    0
            );

            result.add(dto);
        }

        return result;
    }

    public ArrayList<String> getNewCosmeticsId() {
        JsonNode data = fetchCosmetics("/cosmetics/new");

        ArrayList<String> list = new ArrayList<>();

        for (JsonNode node : data){
            list.add(node.get("id").asText());
        }

        return list;
    }

    public List<GetCosmeticsOnShopDTO> getShopCosmetics() {
        JsonNode entries = fetchCosmetics("/shop/br").get("entries");

        List<GetCosmeticsOnShopDTO> cosmeticsOnShop = new ArrayList<>();

        for (JsonNode node : entries) {

            JsonNode bundle = node.get("bundle");

            // 🔹 Caso seja um bundle
            if (bundle != null && !bundle.isNull()) {

                String bundleName = bundle.get("name").asText();
                String bundleImage = bundle.get("image").asText();

                List<GetCosmeticsOnShopDTO> bundleCosmetics = new ArrayList<>();

                for (JsonNode item : node.get("brItems")) {
                    bundleCosmetics.add(
                            new GetCosmeticsOnShopDTO(
                                    item.get("id").asText(),
                                    node.get("regularPrice").asInt(0),
                                    node.get("finalPrice").asInt(0)
                            )
                    );
                }

                // Aqui você pode decidir:
                // 👉 ou salvar o bundle agora, ou só retornar
                CosmeticBundleDTO bundleDTO = new CosmeticBundleDTO(
                        bundleName,
                        bundleImage,
                        bundleCosmetics
                );

                // Adiciona todos os itens do bundle à lista geral
                cosmeticsOnShop.addAll(bundleCosmetics);

            } else {
                JsonNode item = node.get("brItems").get(0);

                cosmeticsOnShop.add(
                        new GetCosmeticsOnShopDTO(
                                item.get("id").asText(),
                                node.get("regularPrice").asInt(0),
                                node.get("finalPrice").asInt(0)
                        )
                );
            }
        }

        return cosmeticsOnShop;
    }



}
