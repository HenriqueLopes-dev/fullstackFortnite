package io.github.HenriqueLopes_dev.FortniteAPI;

import io.github.HenriqueLopes_dev.dto.cosmetic.GetCosmeticsOnShopDTO;
import io.github.HenriqueLopes_dev.dto.cosmeticBundle.CosmeticBundleDTO;
import io.github.HenriqueLopes_dev.mapper.CosmeticBundleMapper;
import io.github.HenriqueLopes_dev.mapper.CosmeticMapper;
import io.github.HenriqueLopes_dev.model.Cosmetic;
import io.github.HenriqueLopes_dev.model.CosmeticBundle;
import io.github.HenriqueLopes_dev.repository.CosmeticBundleRepository;
import io.github.HenriqueLopes_dev.repository.CosmeticRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FortniteSyncService {

    private static final int THREE_HOUR_CLOCK = 10800000;

    private final FortniteApiService apiService;
    private final CosmeticRepository repository;
    private final CosmeticBundleRepository bundleRepository;
    private final CosmeticMapper mapper;
    private final CosmeticBundleMapper bundleMapper;

    @Transactional
    @Scheduled(fixedRate = THREE_HOUR_CLOCK)
    public void syncCosmetics() {
        System.out.println("[SYNC] Iniciando sincronização Fortnite...");

        repository.updateAllByDefault();
        bundleRepository.deleteAll();

        // ===== Base de cosméticos =====
        List<Cosmetic> cosmetics = new ArrayList<>();
        apiService.getAllCosmetics().forEach(dto ->
                repository.findByExternalId(dto.externalId())
                        .ifPresentOrElse(existing -> {
                            existing.setName(dto.name());
                            existing.setDescription(dto.description());
                            existing.setImageUrl(dto.imageUrl());
                            existing.setRarity(dto.rarity());
                            existing.setType(dto.type());
                            existing.setAdded(dto.added());
                            cosmetics.add(existing);
                        }, () -> cosmetics.add(mapper.toEntity(dto)))
        );
        repository.saveAllAndFlush(cosmetics);

        // ===== Novos cosméticos =====
        List<String> newIds = apiService.getNewCosmeticsId();
        repository.findAllByExternalIdIn(newIds).forEach(c -> c.setIsNew(true));
        repository.saveAllAndFlush(cosmetics);

        // ===== Loja =====
        System.out.println("[SYNC] Sincronizando loja (bundles e itens)...");
        List<CosmeticBundleDTO> shopBundlesDTO = apiService.getShopBundles();

        for (CosmeticBundleDTO bundleDTO : shopBundlesDTO) {
            CosmeticBundle bundle = bundleMapper.toEntity(bundleDTO);
            bundle = bundleRepository.saveAndFlush(bundle);

            List<Cosmetic> cosmeticsToSave = new ArrayList<>();
            for (GetCosmeticsOnShopDTO itemDTO : bundleDTO.cosmeticDTOs()) {
                CosmeticBundle finalBundle = bundle;
                repository.findByExternalId(itemDTO.externalId()).ifPresent(cosmetic -> {
                    cosmetic.setBundle(finalBundle);
                    cosmeticsToSave.add(cosmetic);
                });
            }

            repository.saveAllAndFlush(cosmeticsToSave);
        }

        System.out.println("[SYNC] Concluída às " + LocalDateTime.now());
    }
}
