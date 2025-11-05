package io.github.HenriqueLopes_dev.FortniteAPI;

import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;
import io.github.HenriqueLopes_dev.mapper.CosmeticMapper;
import io.github.HenriqueLopes_dev.model.Cosmetic;
import io.github.HenriqueLopes_dev.repository.CosmeticRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FortniteSyncService {

    private static final int TREE_HOUR_CLOCK = 10800000;

    private final FortniteApiService apiService;
    private final CosmeticRepository repository;
    private final CosmeticMapper mapper;

    @Transactional
    @Scheduled(fixedRate = TREE_HOUR_CLOCK)
    public void syncCosmetics() {
        System.out.println("[SYNC] Iniciando sincronização Fortnite...");

        repository.updateAllByDefault();

        List<Cosmetic> cosmetics = apiService.getAllCosmetics()
                        .stream()
                        .map(mapper::toEntity)
                        .toList();

        repository.saveAll(cosmetics);

        List<String> newCosmeticsId = apiService.getNewCosmeticsId();

        List<Cosmetic> newCosmetics = repository.findAllByExternalId(newCosmeticsId);

        newCosmetics.forEach(c -> c.setNew(true));
        repository.saveAll(newCosmetics);

        List<CosmeticDTO> shopItems = apiService.getShopCosmetics();



        System.out.println("[SYNC] Concluída às " + LocalDateTime.now());
    }
}

